#!/usr/bin/env python3
"""
游戏素材自动生成器 - 通过浏览器批量生成游戏图片
使用 Pollinations.ai 的免费图像生成服务
"""
import os
import time
import urllib.parse
from playwright.sync_api import sync_playwright

# 游戏素材列表
ASSETS = [
    # 武将/角色 (64x64 像素风格)
    {"prompt": "Ancient Chinese warrior general pixel art 64x64 game sprite", "name": "warrior_1", "w": 512, "h": 512},
    {"prompt": "Chinese female warrior goddess pixel art game sprite", "name": "goddess_1", "w": 512, "h": 512},
    {"prompt": "Old wise sage pixel art 8-bit game character", "name": "sage_1", "w": 512, "h": 512},
    {"prompt": "Dark evil demon lord pixel art game enemy", "name": "demon_1", "w": 512, "h": 512},
    {"prompt": "Golden armored knight pixel art retro game", "name": "knight_1", "w": 512, "h": 512},
    
    # 场景 (背景)
    {"prompt": "Ancient Chinese palace pixel art game background", "name": "scene_palace", "w": 800, "h": 600},
    {"prompt": "Dark dungeon prison pixel art game background", "name": "scene_dungeon", "w": 800, "h": 600},
    {"prompt": "Mountain temple pixel art landscape background", "name": "scene_temple", "w": 800, "h": 600},
    {"prompt": "Cherry blossom garden pixel art game background", "name": "scene_garden", "w": 800, "h": 600},
    
    # 物品/道具
    {"prompt": "Golden ancient sword pixel art item icon", "name": "item_sword", "w": 256, "h": 256},
    {"prompt": "Blue magic potion bottle pixel art", "name": "item_potion_blue", "w": 256, "h": 256},
    {"prompt": "Red healing potion pixel art item", "name": "item_potion_red", "w": 256, "h": 256},
    {"prompt": "Ancient Chinese seal stamp pixel art", "name": "item_seal", "w": 256, "h": 256},
    {"prompt": "Magic scroll ancient rune pixel art", "name": "item_scroll", "w": 256, "h": 256},
    
    # 特效
    {"prompt": "Fire explosion effect pixel art animation frame", "name": "effect_fire", "w": 256, "h": 256},
    {"prompt": "Lightning bolt effect pixel art", "name": "effect_lightning", "w": 256, "h": 256},
    {"prompt": "Healing magic effect green pixels", "name": "effect_heal", "w": 256, "h": 256},
    {"prompt": "Ice freeze effect blue pixels game", "name": "effect_ice", "w": 256, "h": 256},
    
    # UI元素
    {"prompt": "Pixel art gold coin icon game UI", "name": "ui_coin", "w": 128, "h": 128},
    {"prompt": "Health bar heart pixel art", "name": "ui_heart", "w": 128, "h": 128},
    {"prompt": "Magic mana crystal pixel art icon", "name": "ui_mana", "w": 128, "h": 128},
]

def generate_assets(output_dir, browser, delay=8):
    """批量生成游戏素材"""
    page = browser.new_page()
    
    # 创建输出目录
    os.makedirs(output_dir, exist_ok=True)
    
    success = 0
    failed = 0
    
    for i, asset in enumerate(ASSETS):
        print(f"\n[{i+1}/{len(ASSETS)}] 生成: {asset['name']}")
        print(f"  提示词: {asset['prompt'][:50]}...")
        
        # 构建URL
        prompt_encoded = urllib.parse.quote(asset['prompt'])
        width = asset['w']
        height = asset['h']
        url = f"https://gen.pollinations.ai/image/{prompt_encoded}?width={width}&height={height}&nologo=true"
        
        try:
            # 访问生成页面
            page.goto(url, timeout=30)
            
            # 等待图片生成
            time.sleep(delay)
            
            # 等待图片加载完成
            page.wait_for_load_state("networkidle", timeout=15000)
            
            # 尝试找到生成的图片
            # Pollinations的图片通常在特定的img元素中
            
            # 截图保存
            screenshot_path = os.path.join(output_dir, f"{asset['name']}.png")
            page.screenshot(path=screenshot_path, full_page=False)
            
            # 检查文件大小
            if os.path.exists(screenshot_path):
                size = os.path.getsize(screenshot_path)
                if size > 5000:  # 大于5KB认为成功
                    print(f"  ✅ 成功! ({size/1024:.1f}KB)")
                    success += 1
                else:
                    print(f"  ⚠️ 文件太小，可能未生成")
                    failed += 1
            else:
                print(f"  ❌ 截图失败")
                failed += 1
                
        except Exception as e:
            print(f"  ❌ 错误: {str(e)[:50]}")
            failed += 1
        
        # 简短延时避免被限流
        time.sleep(2)
    
    page.close()
    
    print(f"\n" + "="*50)
    print(f"📊 完成! 成功: {success}, 失败: {failed}")
    print(f"📁 保存位置: {output_dir}")
    print("="*50)
    
    return success, failed

def main():
    output_dir = "/root/.openclaw/workspace/fengshen-game/assets/generated"
    
    print("="*50)
    print("🎮 游戏素材自动生成器")
    print("="*50)
    print(f"将生成 {len(ASSETS)} 个素材...")
    print(f"输出目录: {output_dir}")
    print("="*50)
    
    with sync_playwright() as p:
        # 启动浏览器 (headless模式)
        browser = p.chromium.launch(headless=True)
        
        generate_assets(output_dir, browser, delay=6)
        
        browser.close()
    
    print("\n✨ 所有素材生成完成!")

if __name__ == "__main__":
    main()