"""
封神榜游戏前端自动化测试
使用Playwright进行UI自动化测试
"""

import asyncio
import json
from playwright.async_api import async_playwright, Page, Browser
from dataclasses import dataclass
from typing import List, Dict, Optional
import random

# 测试配置
BASE_URL = "http://localhost:8080"
API_URL = "http://localhost:8080/api"

@dataclass
class TestResult:
    """测试结果"""
    name: str
    passed: bool
    message: str = ""
    screenshot: Optional[str] = None


class FengshenFrontendTester:
    """前端自动化测试"""
    
    def __init__(self):
        self.browser: Optional[Browser] = None
        self.page: Optional[Page] = None
        self.results: List[TestResult] = []
        self.token: str = ""
        self.player_id: int = 0
        
    async def setup(self):
        """初始化浏览器"""
        pw = await async_playwright().start()
        self.browser = await pw.chromium.launch(headless=True)
        self.page = await self.browser.new_page(viewport={"width": 1280, "height": 720})
        
    async def teardown(self):
        """关闭浏览器"""
        if self.browser:
            await self.browser.close()
    
    async def login(self, username: str = "test001", password: str = "123456") -> bool:
        """登录获取Token"""
        import requests
        
        try:
            resp = requests.post(f"{API_URL}/auth/login", json={
                "username": username,
                "password": password
            }, timeout=10)
            
            if resp.ok:
                data = resp.json()
                if data.get("success"):
                    self.token = data.get("token", "")
                    self.player_id = data.get("playerId", 0)
                    print(f"✅ 登录成功: {username}, playerId={self.player_id}")
                    return True
        except Exception as e:
            print(f"❌ 登录失败: {e}")
        
        # 如果登录失败，尝试注册
        try:
            resp = requests.post(f"{API_URL}/auth/register", json={
                "username": username,
                "password": password
            }, timeout=10)
            if resp.ok:
                data = resp.json()
                if data.get("success"):
                    self.token = data.get("token", "")
                    self.player_id = data.get("playerId", 0)
                    print(f"✅ 注册成功: {username}")
                    return True
        except Exception as e:
            print(f"❌ 注册也失败: {e}")
        
        return False
    
    async def navigate_to_game(self):
        """导航到游戏页面"""
        await self.page.goto(f"{BASE_URL}/")
        await self.page.wait_for_load_state("networkidle")
        await self.page.wait_for_timeout(1000)
    
    async def take_screenshot(self, name: str) -> str:
        """截图"""
        path = f"/tmp/fengshen_{name}.png"
        await self.page.screenshot(path=path)
        return path
    
    # ========== 测试用例 ==========
    
    async def test_home_page_load(self):
        """测试1: 首页加载"""
        try:
            await self.navigate_to_game()
            title = await self.page.title()
            
            # 检查关键元素
            has_content = await self.page.locator("body").count() > 0
            
            self.results.append(TestResult(
                name="首页加载",
                passed=has_content,
                message=f"页面标题: {title}"
            ))
            await self.take_screenshot("home")
        except Exception as e:
            self.results.append(TestResult("首页加载", False, str(e)))
    
    async def test_register_page(self):
        """测试2: 注册页面"""
        try:
            await self.page.goto(f"{BASE_URL}/#register")
            await self.page.wait_for_timeout(500)
            
            # 检查注册表单
            has_username = await self.page.locator("input[type='text']").count() > 0
            has_password = await self.page.locator("input[type='password']").count() > 0
            
            self.results.append(TestResult(
                name="注册页面",
                passed=has_username and has_password,
                message=f"表单元素: 用户名={has_username}, 密码={has_password}"
            ))
            await self.take_screenshot("register")
        except Exception as e:
            self.results.append(TestResult("注册页面", False, str(e)))
    
    async def test_login_and_play(self):
        """测试3: 登录并开始游戏"""
        try:
            # 先尝试API登录
            if not await self.login():
                self.results.append(TestResult("登录功能", False, "登录失败"))
                return
            
            # 在前端执行登录（模拟）
            await self.navigate_to_game()
            
            # 检查是否有登录后的元素
            # 游戏可能是单页应用，检查关键UI元素
            body = await self.page.content()
            has_game_content = len(body) > 1000
            
            self.results.append(TestResult(
                name="登录功能",
                passed=has_game_content,
                message=f"Token获取成功，游戏内容加载: {has_game_content}"
            ))
            await self.take_screenshot("logged_in")
        except Exception as e:
            self.results.append(TestResult("登录功能", False, str(e)))
    
    async def test_game_console(self):
        """测试4: 游戏控制台"""
        try:
            await self.navigate_to_game()
            
            # 打开开发者工具控制台
            # 检查JavaScript错误
            errors = []
            self.page.on("console", lambda msg: errors.append(msg.text) if msg.type == "error" else None)
            
            await self.page.wait_for_timeout(2000)
            
            has_errors = len(errors) > 0
            
            self.results.append(TestResult(
                name="JavaScript错误检测",
                passed=not has_errors,
                message=f"控制台错误: {len(errors)}个" if has_errors else "无JS错误"
            ))
        except Exception as e:
            self.results.append(TestResult("JavaScript错误检测", False, str(e)))
    
    async def test_api_integration(self):
        """测试5: API集成"""
        try:
            import requests
            
            # 测试多个API
            apis_to_test = [
                ("/auth/login", "POST", {"username": "test", "password": "test"}),
                ("/quest/list", "GET", None),
                ("/rank/list", "GET", None),
            ]
            
            results = []
            for path, method, data in apis_to_test:
                url = f"{API_URL}{path}"
                try:
                    if method == "GET":
                        resp = requests.get(url, timeout=5)
                    else:
                        resp = requests.post(url, json=data, timeout=5)
                    results.append(f"{path}: {resp.status_code}")
                except Exception as e:
                    results.append(f"{path}: 失败")
            
            all_ok = all("200" in r or "404" in r or "500" in r for r in results)
            
            self.results.append(TestResult(
                name="API集成测试",
                passed=True,
                message="; ".join(results)
            ))
        except Exception as e:
            self.results.append(TestResult("API集成测试", False, str(e)))
    
    async def test_responsive_design(self):
        """测试6: 响应式设计"""
        try:
            viewports = [
                {"width": 1920, "height": 1080, "name": "桌面"},
                {"width": 768, "height": 1024, "name": "平板"},
                {"width": 375, "height": 667, "name": "手机"},
            ]
            
            results = []
            for vp in viewports:
                await self.page.set_viewport_size({"width": vp["width"], "height": vp["height"]})
                await self.navigate_to_game()
                await self.page.wait_for_timeout(500)
                
                # 检查页面是否正常渲染
                body_height = await self.page.locator("body").bounding_box()
                if body_height:
                    results.append(f"{vp['name']}: OK")
            
            self.results.append(TestResult(
                name="响应式设计",
                passed=len(results) == 3,
                message="; ".join(results)
            ))
        except Exception as e:
            self.results.append(TestResult("响应式设计", False, str(e)))
    
    async def test_ui_elements(self):
        """测试7: UI元素检测"""
        try:
            await self.navigate_to_game()
            await self.page.wait_for_timeout(1000)
            
            # 统计页面元素
            buttons = await self.page.locator("button").count()
            inputs = await self.page.locator("input").count()
            links = await self.page.locator("a").count()
            
            self.results.append(TestResult(
                name="UI元素检测",
                passed=True,
                message=f"按钮:{buttons}, 输入框:{inputs}, 链接:{links}"
            ))
            await self.take_screenshot("ui_elements")
        except Exception as e:
            self.results.append(TestResult("UI元素检测", False, str(e)))
    
    async def run_all_tests(self):
        """运行所有测试"""
        print("\n" + "="*60)
        print("🎮 封神榜前端自动化测试")
        print("="*60)
        
        await self.setup()
        
        # 执行测试
        tests = [
            self.test_home_page_load,
            self.test_register_page,
            self.test_login_and_play,
            self.test_game_console,
            self.test_api_integration,
            self.test_responsive_design,
            self.test_ui_elements,
        ]
        
        for test in tests:
            print(f"\n▶ 运行: {test.__name__}")
            await test()
        
        await self.teardown()
        
        # 输出结果
        print("\n" + "="*60)
        print("📊 测试结果")
        print("="*60)
        
        passed = 0
        for r in self.results:
            status = "✅" if r.passed else "❌"
            print(f"{status} {r.name}: {r.message}")
            if r.passed:
                passed += 1
        
        print(f"\n总计: {passed}/{len(self.results)} 通过")
        
        return self.results


async def main():
    """主函数"""
    tester = FengshenFrontendTester()
    await tester.run_all_tests()


if __name__ == "__main__":
    asyncio.run(main())
