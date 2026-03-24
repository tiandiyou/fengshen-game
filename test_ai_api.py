"""
封神榜API测试框架
AI驱动的自动化测试 - 智能场景识别与链路生成
"""

import requests
import json
import time
from typing import Dict, List, Optional
from dataclasses import dataclass, field
from enum import Enum

BASE_URL = "http://localhost:8080/api"

class HTTPMethod(Enum):
    GET = "GET"
    POST = "POST"
    PUT = "PUT"
    DELETE = "DELETE"

@dataclass
class APIEndpoint:
    """API端点定义"""
    controller: str
    path: str
    method: HTTPMethod
    requires_auth: bool = True
    depends_on: List[str] = field(default_factory=list)
    description: str = ""

@dataclass
class TestContext:
    """测试上下文 - 存储认证Token和关键数据"""
    token: Optional[str] = None
    player_id: Optional[int] = None
    user_data: Dict = field(default_factory=dict)

class FengshenAPITester:
    """封神榜API测试器"""
    
    # API清单 - 从Controller自动提取
    APIS = {
        # === 认证模块 ===
        "auth_register": APIEndpoint("Auth", "/auth/register", HTTPMethod.POST, False, 
            description="用户注册"),
        "auth_login": APIEndpoint("Auth", "/auth/login", HTTPMethod.POST, False,
            description="用户登录"),
        "auth_verify": APIEndpoint("Auth", "/auth/verify", HTTPMethod.POST, True,
            description="Token验证"),
        
        # === 玩家模块 ===
        "player_create": APIEndpoint("Player", "/player/create", HTTPMethod.POST, True,
            depends_on=["auth_login"], description="创建角色"),
        "player_info": APIEndpoint("Player", "/player/{id}", HTTPMethod.GET, True,
            depends_on=["player_create"], description="获取玩家信息"),
        
        # === 抽卡模块 ===
        "gacha_draw": APIEndpoint("Gacha", "/gacha/draw", HTTPMethod.POST, True,
            depends_on=["player_create"], description="抽卡"),
        "gacha_heroes": APIEndpoint("Gacha", "/gacha/heroes", HTTPMethod.GET, True,
            description="获取卡池"),
        
        # === 战斗模块 ===
        "battle_start": APIEndpoint("Battle", "/battle/start", HTTPMethod.POST, True,
            depends_on=["player_create", "gacha_draw"], description="开始战斗"),
        "battle_result": APIEndpoint("Battle", "/battle/result", HTTPMethod.POST, True,
            depends_on=["battle_start"], description="战斗结果"),
        
        # === 联盟模块 ===
        "alliance_create": APIEndpoint("Alliance", "/alliance/create", HTTPMethod.POST, True,
            depends_on=["player_create"], description="创建联盟"),
        "alliance_join": APIEndpoint("Alliance", "/alliance/join", HTTPMethod.POST, True,
            depends_on=["alliance_create"], description="加入联盟"),
        "alliance_list": APIEndpoint("Alliance", "/alliance/list", HTTPMethod.GET, True,
            description="联盟列表"),
        
        # === 资源模块 ===
        "resource_init": APIEndpoint("Resource", "/resource/init", HTTPMethod.POST, True,
            depends_on=["player_create"], description="初始化资源"),
        "resource_my": APIEndpoint("Resource", "/resource/my", HTTPMethod.GET, True,
            depends_on=["resource_init"], description="我的资源"),
        "resource_produce": APIEndpoint("Resource", "/resource/produce", HTTPMethod.POST, True,
            depends_on=["resource_init"], description="生产资源"),
        
        # === 任务模块 ===
        "quest_list": APIEndpoint("Quest", "/quest/list", HTTPMethod.GET, True,
            description="任务列表"),
        "quest_claim": APIEndpoint("Quest", "/quest/claim", HTTPMethod.POST, True,
            depends_on=["quest_list"], description="领取奖励"),
        
        # === 排行榜 ===
        "rank_list": APIEndpoint("Rank", "/rank/list", HTTPMethod.GET, True,
            description="排行榜"),
        
        # === 商店模块 ===
        "shop_list": APIEndpoint("Shop", "/shop/list", HTTPMethod.GET, True,
            description="商店列表"),
        "shop_buy": APIEndpoint("Shop", "/shop/buy", HTTPMethod.POST, True,
            depends_on=["shop_list"], description="购买商品"),
        
        # === 赛季模块 ===
        "season_current": APIEndpoint("Season", "/season/current", HTTPMethod.GET, True,
            description="当前赛季"),
        "season_ranking": APIEndpoint("Season", "/season/ranking", HTTPMethod.GET, True,
            depends_on=["season_current"], description="赛季排名"),
        
        # === 建筑模块 ===
        "building_init": APIEndpoint("Building", "/building/init", HTTPMethod.POST, True,
            depends_on=["player_create"], description="初始化建筑"),
        "building_list": APIEndpoint("Building", "/building/list", HTTPMethod.GET, True,
            depends_on=["building_init"], description="建筑列表"),
        "building_upgrade": APIEndpoint("Building", "/building/upgrade", HTTPMethod.POST, True,
            depends_on=["building_list"], description="升级建筑"),
        
        # === 城市模块 ===
        "city_my": APIEndpoint("City", "/city/my", HTTPMethod.GET, True,
            depends_on=["player_create"], description="我的城市"),
        "city_upgrade": APIEndpoint("City", "/city/upgrade", HTTPMethod.POST, True,
            depends_on=["city_my"], description="升级城市"),
        "city_collect": APIEndpoint("City", "/city/collect", HTTPMethod.POST, True,
            depends_on=["city_my"], description="收集资源"),
        
        # === 境界/修炼模块 ===
        "cultivation_info": APIEndpoint("Cultivation", "/cultivation/info", HTTPMethod.GET, True,
            description="修炼信息"),
        "cultivation_choose": APIEndpoint("Cultivation", "/cultivation/choose", HTTPMethod.POST, True,
            depends_on=["cultivation_info"], description="选择修炼方向"),
        "cultivation_practice": APIEndpoint("Cultivation", "/cultivation/practice", HTTPMethod.POST, True,
            depends_on=["cultivation_choose"], description="开始修炼"),
        
        # === 地图模块 ===
        "map_init": APIEndpoint("Map", "/map/init", HTTPMethod.POST, True,
            depends_on=["player_create"], description="初始化地图"),
        "map_zones": APIEndpoint("Map", "/map/zones", HTTPMethod.GET, True,
            depends_on=["map_init"], description="地图区域"),
        "map_move": APIEndpoint("Map", "/map/move", HTTPMethod.POST, True,
            depends_on=["map_zones"], description="移动"),
        
        # === 坐骑模块 ===
        "mount_list": APIEndpoint("Mount", "/mount/list", HTTPMethod.GET, True,
            description="坐骑列表"),
        "mount_explore": APIEndpoint("Mount", "/mount/explore", HTTPMethod.POST, True,
            depends_on=["mount_list"], description="探索坐骑"),
        "mount_equip": APIEndpoint("Mount", "/mount/equip", HTTPMethod.POST, True,
            depends_on=["mount_explore"], description="装备坐骑"),
        
        # === 成就模块 ===
        "achievement_list": APIEndpoint("Achievement", "/achievement/list", HTTPMethod.GET, True,
            description="成就列表"),
        "achievement_claim": APIEndpoint("Achievement", "/achievement/claim", HTTPMethod.POST, True,
            depends_on=["achievement_list"], description="领取成就奖励"),
        
        # === 成长模块 ===
        "growth_info": APIEndpoint("Growth", "/growth/info", HTTPMethod.GET, True,
            description="成长信息"),
        "growth_level_up": APIEndpoint("Growth", "/growth/level-up", HTTPMethod.POST, True,
            depends_on=["growth_info"], description="升级"),
        "growth_star_up": APIEndpoint("Growth", "/growth/star-up", HTTPMethod.POST, True,
            depends_on=["growth_info"], description="升星"),
        
        # === 签到模块 ===
        "signin_status": APIEndpoint("Signin", "/signin/status", HTTPMethod.GET, True,
            description="签到状态"),
        "signin_do": APIEndpoint("Signin", "/signin/do", HTTPMethod.POST, True,
            depends_on=["signin_status"], description="签到"),
        
        # === 技能交换模块 ===
        "skill_exchange_list": APIEndpoint("SkillExchange", "/skill-exchange/list", HTTPMethod.GET, True,
            description="技能交换列表"),
        "skill_exchange_detail": APIEndpoint("SkillExchange", "/skill-exchange/detail", HTTPMethod.GET, True,
            description="技能详情"),
        "skill_exchange_exchange": APIEndpoint("SkillExchange", "/skill-exchange/exchange", HTTPMethod.POST, True,
            depends_on=["skill_exchange_list"], description="交换技能"),
        
        # === 扫荡模块 ===
        "sweep_check": APIEndpoint("Sweep", "/sweep/check", HTTPMethod.GET, True,
            description="扫荡检查"),
        "sweep_run": APIEndpoint("Sweep", "/sweep/run", HTTPMethod.POST, True,
            depends_on=["sweep_check"], description="执行扫荡"),
        
        # === 队伍模块 ===
        "team_list": APIEndpoint("Team", "/team/list", HTTPMethod.GET, True,
            description="队伍列表"),
        "team_set": APIEndpoint("Team", "/team/set", HTTPMethod.POST, True,
            depends_on=["team_list"], description="设置队伍"),
        
        # === 宝藏模块 ===
        "treasure_list": APIEndpoint("Treasure", "/treasure/list", HTTPMethod.GET, True,
            description="宝藏列表"),
        "treasure_receive": APIEndpoint("Treasure", "/treasure/receive", HTTPMethod.POST, True,
            depends_on=["treasure_list"], description="领取宝藏"),
        
        # === 外交模块 ===
        "diplomacy_set": APIEndpoint("Diplomacy", "/diplomacy/set", HTTPMethod.POST, True,
            description="设置外交"),
        "diplomacy_view": APIEndpoint("Diplomacy", "/diplomacy/view", HTTPMethod.GET, True,
            description="查看外交"),
        "diplomacy_list": APIEndpoint("Diplomacy", "/diplomacy/list", HTTPMethod.GET, True,
            description="外交列表"),
        
        # === 战争模块 ===
        "war_siege": APIEndpoint("War", "/war/siege", HTTPMethod.POST, True,
            description="围攻"),
        "war_simulate": APIEndpoint("War", "/war/simulate", HTTPMethod.POST, True,
            description="模拟战争"),
    }
    
    def __init__(self, base_url: str = BASE_URL):
        self.base_url = base_url
        self.context = TestContext()
        self.test_results = []
        
    def call_api(self, api_key: str, data: Dict = None, params: Dict = None) -> Dict:
        """调用API"""
        api = self.APIS.get(api_key)
        if not api:
            return {"success": False, "error": f"Unknown API: {api_key}"}
        
        url = f"{self.base_url}{api.path}"
        headers = {}
        
        # 添加认证
        if api.requires_auth and self.context.token:
            headers["Authorization"] = f"Bearer {self.context.token}"
        
        # 处理path参数
        if "{id}" in url and params and "id" in params:
            url = url.replace("{id}", str(params["id"]))
        
        # 发送请求
        method = getattr(requests, api.method.value.lower())
        
        try:
            response = method(url, json=data, params=params, headers=headers, timeout=10)
            result = {
                "success": response.ok,
                "status": response.status_code,
                "data": response.json() if response.ok else response.text
            }
            
            # 保存关键数据到上下文
            if response.ok and api_key == "auth_login":
                self.context.token = response.json().get("token")
                self.context.player_id = response.json().get("playerId")
                
        except Exception as e:
            result = {"success": False, "error": str(e)}
        
        self.test_results.append({
            "api": api_key,
            "url": url,
            "result": result
        })
        
        return result
    
    def get_execution_order(self, api_keys: List[str]) -> List[str]:
        """智能排序 - 根据依赖关系生成调用顺序"""
        visited = set()
        ordered = []
        
        def visit(key):
            if key in visited:
                return
            visited.add(key)
            
            api = self.APIS.get(key)
            if api and api.depends_on:
                for dep in api.depends_on:
                    if dep in self.APIS:
                        visit(dep)
            ordered.append(key)
        
        for key in api_keys:
            visit(key)
            
        return ordered
    
    def run_test_scenario(self, scenario_name: str, api_keys: List[str]) -> Dict:
        """运行测试场景"""
        print(f"\n{'='*50}")
        print(f"🧪 运行场景: {scenario_name}")
        print(f"{'='*50}")
        
        # 智能排序
        execution_order = self.get_execution_order(api_keys)
        print(f"📋 执行顺序: {' -> '.join(execution_order)}")
        
        results = {}
        for api_key in execution_order:
            print(f"▶ 调用: {api_key}")
            result = self.call_api(api_key)
            results[api_key] = result
            print(f"  {'✅' if result.get('success') else '❌'} {result.get('status', 'error')}")
            
            # 失败则停止
            if not result.get("success"):
                print(f"  ⚠️ 停止执行后续API")
                break
        
        return results
    
    def run_all_api_test(self) -> Dict:
        """运行全部API测试"""
        # 按模块分组测试
        scenarios = {
            "1_认证模块": ["auth_register", "auth_login"],
            "2_基础创建": ["player_create", "resource_init", "building_init"],
            "3_抽卡战斗": ["gacha_heroes", "gacha_draw", "battle_start", "battle_result"],
            "4_联盟社交": ["alliance_create", "alliance_list"],
            "5_资源收集": ["resource_produce", "resource_my"],
            "6_任务成就": ["quest_list", "quest_claim", "achievement_list"],
            "7_商店购买": ["shop_list", "shop_buy"],
            "8_排行榜": ["rank_list"],
        }
        
        all_results = {}
        for name, apis in scenarios.items():
            results = self.run_test_scenario(name, apis)
            all_results[name] = results
            
        return all_results


# === AI场景识别器 ===
class AISceneRecognizer:
    """AI场景识别器 - 基于关键词识别测试场景"""
    
    SCENE_PATTERNS = {
        "新手引导": ["register", "login", "create", "init"],
        "抽卡体验": ["gacha", "draw", "heroes"],
        "战斗流程": ["battle", "start", "result", "fight"],
        "资源管理": ["resource", "produce", "collect", "gold"],
        "社交互动": ["alliance", "join", "diplomacy"],
        "成长系统": ["growth", "level", "star", "cultivation"],
        "任务活动": ["quest", "achievement", "signin"],
        "PVP竞技": ["rank", "war", "siege"],
        "商店消费": ["shop", "buy", "exchange"],
    }
    
    @classmethod
    def recognize_scenes(cls, description: str) -> List[str]:
        """根据描述识别场景"""
        description = description.lower()
        matched_scenes = []
        
        for scene, keywords in cls.SCENE_PATTERNS.items():
            if any(kw in description for kw in keywords):
                matched_scenes.append(scene)
                
        return matched_scenes
    
    @classmethod
    def generate_test_chain(cls, scene: str) -> List[str]:
        """为场景生成测试链"""
        chains = {
            "新手引导": ["auth_register", "auth_login", "player_create", "resource_init", "building_init"],
            "抽卡体验": ["auth_login", "player_create", "gacha_heroes", "gacha_draw"],
            "战斗流程": ["auth_login", "player_create", "gacha_draw", "battle_start", "battle_result"],
            "资源管理": ["auth_login", "player_create", "resource_init", "resource_produce", "resource_my"],
            "社交互动": ["auth_login", "player_create", "alliance_create", "alliance_join"],
            "成长系统": ["auth_login", "player_create", "growth_info", "growth_level_up", "cultivation_choose"],
            "任务活动": ["auth_login", "player_create", "quest_list", "achievement_list", "signin_status"],
            "PVP竞技": ["auth_login", "player_create", "rank_list", "war_simulate"],
            "商店消费": ["auth_login", "player_create", "shop_list", "shop_buy"],
        }
        
        return chains.get(scene, [])


if __name__ == "__main__":
    # 测试
    tester = FengshenAPITester("http://1.14.139.77")
    
    # 识别场景
    description = "我想测试抽卡和战斗流程"
    scenes = AISceneRecognizer.recognize_scenes(description)
    print(f"🔍 识别场景: {scenes}")
    
    # 生成测试链
    apis = AISceneRecognizer.generate_test_chain("抽卡体验")
    print(f"📋 测试链: {apis}")
    
    # 运行测试
    tester.run_test_scenario("抽卡体验", apis)
