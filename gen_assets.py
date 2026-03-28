#!/usr/bin/env python3
"""
游戏素材生成器 - 使用 Pollinations.ai 免费API
"""
import urllib.parse
import urllib.request
import os

# Pollinations AI 图像生成URL格式
BASE_URL = "https://image.pollinations.ai/prompt/"

def generate_game_asset(prompt, filename=None, width=512, height=512, model="flux"):
    """
    生成游戏素材图片
    
    参数:
        prompt: 图像描述
        filename: 保存文件名（可选）
        width: 图像宽度
        height: 图像高度
        model: 模型选择 (flux, flux-realism, nanobanana-pro, etc.)
    """
    # 编码prompt
    encoded_prompt = urllib.parse.quote(prompt)
    
    # 构建URL
    url = f"{BASE_URL}{encoded_prompt}?width={width}&height={height}&model={model}&nologo=true"
    
    print(f"生成中: {prompt[:50]}...")
    print(f"URL: {url}")
    
    if filename:
        # 下载图片
        import urllib.request
        try:
            urllib.request.urlretrieve(url, filename)
            print(f"✅ 已保存: {filename}")
            return filename
        except Exception as e:
            print(f"❌ 下载失败: {e}")
            return None
    return url

def main():
    # 创建素材目录
    asset_dir = "/root/.openclaw/workspace/fengshen-game/assets/generated"
    os.makedirs(asset_dir, exist_ok=True)
    
    # 游戏素材提示词
    assets = [
        # 武将头像
        ("Ancient Chinese warrior general, pixel art style, 64x64, game sprite", f"{asset_dir}/warrior.png"),
        ("Beautiful Chinese goddess, pixel art, retro game sprite", f"{asset_dir}/goddess.png"),
        ("Old wise sage, pixel art, 8-bit game character", f"{asset_dir}/sage.png"),
        
        # 场景
        ("Ancient Chinese palace, pixel art game background", f"{asset_dir}/palace.png"),
        ("Dark dungeon prison, retro game background", f"{asset_dir}/dungeon.png"),
        ("Mountain temple, pixel art landscape", f"{asset_dir}/temple.png"),
        
        # 物品
        ("Golden ancient sword, pixel art item icon", f"{asset_dir}/sword.png"),
        ("Magic potion bottle, pixel art", f"{asset_dir}/potion.png"),
        ("Ancient Chinese seal, pixel art", f"{asset_dir}/seal.png"),
        
        # 特效
        ("Fire explosion effect, pixel art animation frame", f"{asset_dir}/fire.png"),
        ("Lightning bolt effect, pixel art", f"{asset_dir}/lightning.png"),
        ("Healing magic effect, pixel art", f"{asset_dir}/heal.png"),
    ]
    
    print("="*60)
    print("🎮 开始生成游戏素材!")
    print("="*60)
    
    for prompt, filepath in assets:
        result = generate_game_asset(prompt, filepath)
        if result:
            print(f"✅ 完成: {os.path.basename(filepath)}")
        print("-"*40)

    print("\n🎉 所有素材生成完成!")
    print(f"📁 保存位置: {asset_dir}")

if __name__ == "__main__":
    main()