# 武将头像生成方案

## 方案1: Canvas动态生成（推荐）

无需外部API，前端直接生成。

### 使用方法
```javascript
const avatar = generateHeroAvatar('杨戬', 'red', '阐教');
img.src = avatar;
```

### 效果
- 品质颜色背景（红/橙/紫/蓝）
- 金色边框
- 名字首字显示
- 圆形头像

---

## 方案2: 占位图服务

使用 ui-avatars.com 或 dicebear

```javascript
// 使用DiceBear生成卡通头像
const name = '杨戬';
const url = `https://api.dicebear.com/7.x/avataaars/svg?seed=${name}`;
```

---

## 方案3: 购买/外包

如果需要高质量3D形象，建议:
1. Fiverr - $50-200/个
2. Upwork - 专业3D artist
3. ArtStation - 游戏美术外包
