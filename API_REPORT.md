
# 封神榜 - 伐商演义 API文档

## 项目信息
- 仓库: https://github.com/tiandiyou/fengshen-game
- 提交: 03d88f4
- 测试: 全部通过 ✓

---

## 模块总览

| 模块 | Controller数 | Entity数 |
|------|-------------|---------|
| 核心战斗 | 5 | 3 |
| 社交联盟 | 3 | 4 |
| 资源建筑 | 2 | 2 |
| 成长系统 | 5 | 5 |
| 其他 | 12 | 9 |
| **总计** | **27** | **23** |

---

## API接口文档

### 1. 玩家系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/player/create | POST | 创建玩家 |
| /api/player/{id} | GET | 获取玩家信息 |
| /api/player/update | POST | 更新玩家 |

### 2. 抽卡系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/gacha/draw | POST | 抽卡(单抽/十连) |
| /api/gacha/partner/{id} | GET | 伙伴详情 |

### 3. 队伍系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/team/set | POST | 设置队伍 |
| /api/team/list | GET | 队伍列表 |
| /api/team/recommend | GET | 配将推荐 |
| /api/team/best | GET | 最强队伍 |

### 4. 城战系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/war/siege | POST | 攻城 |
| /api/war/simulate | POST | 战斗模拟 |
| /api/war/team-power | GET | 队伍战力 |

### 5. 联盟系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/alliance/create | POST | 创建联盟 |
| /api/alliance/list | GET | 联盟列表 |
| /api/alliance/join | POST | 加入联盟 |
| /api/alliance/my | GET | 我的联盟 |

### 6. 外交系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/diplomacy/set | POST | 设置外交(敌对/友好/中立) |
| /api/diplomacy/view | GET | 查看外交关系 |
| /api/diplomacy/list | GET | 外交列表 |

### 7. 资源系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/resource/my | GET | 获取资源 |
| /api/resource/produce | POST | 资源产出 |

### 8. 建筑系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/building/list | GET | 建筑列表 |
| /api/building/upgrade | POST | 升级建筑 |
| /api/building/bonus | GET | 产出加成 |

### 9. 赛季系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/season/start | POST | 开启赛季 |
| /api/season/current | GET | 当前赛季 |
| /api/season/ranking | GET | 封神榜排行 |
| /api/season/credit | GET | 战功查询 |
| /api/season/addCredit | POST | 增加战功 |
| /api/season/exchange | POST | 兑换装备 |

### 10. 战斗记录

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/battle/record | POST | 记录战斗 |
| /api/battle/log | GET | 战报列表 |
| /api/battle/stats | GET | 战绩统计 |

### 11. 其他系统

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/shop/* | * | 商店系统 |
| /api/quest/* | * | 任务系统 |
| /api/achievement/* | * | 成就系统 |
| /api/signin/* | * | 签到系统 |
| /api/map/* | * | 地图系统 |
| /api/rank/* | * | 排行系统 |

---

## 游戏机制

### 兵种相克
- 骑 → 步 → 弓 → 骑
- 策被所有克制

### 阵营加成
- 阐教: 攻击+5%/人
- 截教: 生命+5%/人
- 商朝: 防御+5%/人
- 周朝: 暴击+5%/人

### 品质差距
- 红: 基准
- 橙: -8.6%
- 紫: -20%
- 蓝: -30%

---

## 测试结果

```
✓ 玩家创建测试
✓ 抽卡测试
✓ 队伍设置测试
✓ 城战测试
✓ 城池测试
✓ 阵营加成测试
✓ 缘分系统测试
```

---

生成时间: 2026-03-16
