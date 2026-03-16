
# 封神榜 - 武将3D形象设计规范

## 一、设计风格

### 整体风格
- **朝代风格**: 商周时期中国古风
- **美术风格**: 3D低多边形 + 精细贴图
- **适用平台**: H5小游戏/微信小游戏
- **模型格式**: GLB (含骨骼动画)

---

## 二、阵营视觉体系

### 1. 阐教 (正道金光)
- **主色调**: 金色 #FFD700 + 白色 #FFFFFF + 浅蓝 #87CEEB
- **服饰风格**: 道袍飘逸,绣有八卦/太极图案
- **材质**: 丝绸质感,有流光效果
- **特效**: 金光环绕,仙气飘飘
- **代表人物**: 燃灯道人,姜子牙,杨戬

### 2. 截教 (截教紫气)
- **主色调**: 紫色 #9B59B6 + 黑色 #1A1A1A + 暗红 #8B0000
- **服饰风格**: 黑袍神秘,带有魔纹
- **材质**: 暗黑皮革,金属配件
- **特效**: 紫气东来,魔焰环绕
- **代表人物**: 通天教主,多宝道人,闻仲

### 3. 商朝 (商朝血红)
- **主色调**: 红色 #E74C3C + 黑色 #1A1A1A + 金色 #FFD700
- **服饰风格**: 铠甲威武,王袍华丽
- **材质**: 金属铠甲,丝绸王袍
- **特效**: 血腥气场,王权威压
- **代表人物**: 纣王,妲己,黄飞虎

### 4. 周朝 (周朝蓝装)
- **主色调**: 蓝色 #3498DB + 白色 #FFFFFF + 青色 #00CED1
- **服饰风格**: 简洁干练,文雅飘逸
- **材质**: 棉麻布衣,金属轻甲
- **特效**: 浩然正气,周礼之风
- **代表人物**: 姬发,姬昌,姜子牙

---

## 三、兵种外观设计

### 1. 步兵 (剑盾)
- 左手持盾(圆形/方形),右手持剑
- 穿戴皮甲或轻甲
- 颜色随阵营变化

### 2. 弓兵 (弓箭)
- 背负箭囊,手持长弓
- 穿戴轻便皮甲
- 敏捷型外观

### 3. 骑兵 (骑乘)
- 骑乘战马(未建模时可省略)
- 手持长矛或马刀
- 重甲披风

### 4. 策士 (法术)
- 手持法杖/拂尘
- 道袍或法袍
- 发光法器

---

## 四、3D生成Prompt示例

### 燃灯道人 (阐教教主)
```
A majestic Chinese Taoist priest,elderly sage with white beard,wearing flowing golden robe with yin-yang symbols,
holding a Buddhist rosary,慈眉善目,divine light aura,glowing golden halo behind head,
ancient Chinese god style,3D game character,low poly,PBR texture,transparent PNG,
T-pose,clean topology,game-ready model
```

### 妲己 (商朝宠妃)
```
Beautiful Chinese woman with fox spirit aura,elegant hanfu dress in crimson red and gold,
graceful pose,狐狸耳朵subtle,long flowing black hair with gold ornaments,
seductive expression,ancient Chinese beauty style,3D game character,
low poly,PBR texture,transparent PNG,T-pose
```

### 杨戬 (清源妙道)
```
Chinese warrior hero,youthful strong body,wearing silver and blue armor,
three-eyed forehead marking,holding silver spear,三尖二刃刀,
confident expression,ancient Chinese martial artist style,3D game character,
low poly,PBR texture,transparent PNG,T-pose
```

### 哪吒 (三坛海会大神)
```
Young Chinese warrior boy,red lotus themed armor with golden trim,
fire aura surrounding,belt with乾坤圈,holding混天绫,
cheerful brave expression,child warrior god,3D game character,
low poly,PBR texture,transparent PNG,T-pose
```

### 通天教主 (截教教主)
```
Ancient dark Taoist lord,mysterious black and purple robe,
holding sword,menacing aura,dark energy swirling,elvish features,
supreme power expression,ancient Chinese demon god style,3D game character,
low poly,PBR texture,transparent PNG,T-pose
```

---

## 五、品质外观差异

### 红将 (传说)
- 全身金色光环特效
- 武器/装备有符文发光
- 站立时身后有光环
- 攻击时有专属特效

### 橙将 (史诗)
- 重要部位金色装饰
- 武器有进阶特效
- 移动有拖尾效果

### 紫将 (稀有)
- 蓝色/紫色色调
- 基础发光效果

### 蓝将 (普通)
- 基础模型
- 无特殊特效

---

## 六、动画要求

### 基础动画 (必须)
1. **idle** - 待机呼吸动画
2. **walk** - 行走动画
3. **attack** - 攻击动画
4. **hurt** - 受伤动画

### 高级动画 (可选)
5. **skill** - 技能释放动画
6. **die** - 死亡动画
7. **victory** - 胜利动画

### 导出格式
- 格式: GLB (含动画)
- 面数限制: 5000-10000面
- 贴图: 1024x1024 PNG

---

## 七、技术规格

| 项目 | 规格 |
|-----|------|
| 模型格式 | GLB |
| 动画格式 | 内嵌骨骼动画 |
| 面数 | 3000-8000 |
| 贴图 | 1024x1024 PNG |
| 骨骼 | 至少15根 |
| LOD | 建议支持 |

---

## 八、设计工具推荐

1. **AI生成**: Tripo3D, Meshy, CSM
2. **手动建模**: Blender, Maya
3. **免费模型**: Sketchfab, Sketchfab

---

*设计规范版本: 1.0*
*更新时间: 2026-03-16*
