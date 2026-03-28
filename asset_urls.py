#!/usr/bin/env python3
"""
使用 OpenClaw 浏览器工具生成游戏素材
"""
import os
import time
import urllib.parse

ASSETS = [
    # 武将
    {"prompt": "Ancient Chinese warrior general pixel art game sprite", "name": "warrior_1"},
    {"prompt": "Chinese goddess warrior pixel art retro game character", "name": "goddess_1"},
    {"prompt": "Old wise sage pixel art 8-bit game character", "name": "sage_1"},
    {"prompt": "Dark demon lord pixel art game enemy", "name": "demon_1"},
    {"prompt": "Golden armored knight pixel art", "name": "knight_1"},
    # 场景
    {"prompt": "Ancient Chinese palace pixel art game background", "name": "scene_palace"},
    {"prompt": "Dark dungeon prison pixel art background", "name": "scene_dungeon"},
    {"prompt": "Mountain temple pixel art landscape", "name": "scene_temple"},
    # 物品
    {"prompt": "Golden ancient sword pixel art item icon", "name": "item_sword"},
    {"prompt": "Blue magic potion pixel art", "name": "item_potion_blue"},
    {"prompt": "Red healing potion pixel art", "name": "item_potion_red"},
    {"prompt": "Ancient Chinese seal pixel art", "name": "item_seal"},
    # 特效
    {"prompt": "Fire explosion effect pixel art", "name": "effect_fire"},
    {"prompt": "Lightning bolt effect pixel art", "name": "effect_lightning"},
    {"prompt": "Healing magic green effect pixel art", "name": "effect_heal"},
]

output_dir = "/root/.openclaw/workspace/fengshen-game/assets/generated"
os.makedirs(output_dir, exist_ok=True)

# 生成URL列表
urls = []
for asset in ASSETS:
    prompt_encoded = urllib.parse.quote(asset['prompt'])
    url = f"https://gen.pollinations.ai/image/{prompt_encoded}?width=512&height=512&nologo=true"
    urls.append((asset['name'], url))

print("生成的URL列表:")
for name, url in urls:
    print(f"{name}: {url}")

print(f"\n共 {len(urls)} 个素材")
print("请使用浏览器工具逐个访问这些URL生成图片")
print(f"输出目录: {output_dir}")