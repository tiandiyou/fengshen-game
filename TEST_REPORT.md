# 封神榜后端测试报告

**生成时间**: 2026-03-15 19:05  
**项目**: 封神榜 - 伐商演义后端  
**版本**: v2.1 - 等级成长+星级系统

---

## 一、系统功能

### 1.1 等级成长系统
- 等级上限: 红80级 / 橙60级 / 紫40级 / 蓝30级
- 升级经验: 等级 × 100
- 成长值: 每级属性自动提升

### 1.2 星级系统
- 1-6星
- 升星消耗: 2个相同武将
- 星级加成: 1星0% → 6星100%

### 1.3 伤害公式 (方案B)
```
物理伤害 = 武力 × (1 - 统率 / (统率 + 200))
法术伤害 = 智力 × (1 - 智力 / (智力 + 200))
```

---

## 二、测试用例 (共120+个)

### PartnerLevelStarTest (等级星级测试) - 18个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1 | testStarBonus | 星级加成百分比 |
| 2 | testStarPromotion | 升星逻辑 |
| 3 | testLevelUp | 升级系统 |
| 4 | testMaxLevel | 等级上限 |
| 5 | testLevelGrowth | 等级成长计算 |
| 6 | testStarBonusOnAttribute | 星级对属性加成 |
| 7 | testLevelAndStarCombined | 等级+星级综合 |
| 8 | testTroopsCalculation | 兵量计算 |
| 9 | testZhanliCalculation | 战力计算 |
| 10 | testZhanliDifferenceByQuality | 品质战力差 |
| 11 | testPhysicalDamageWithLevelAndStar | 物理伤害 |
| 12 | testMagicDamageWithLevelAndStar | 法术伤害 |
| 13 | testExpSystem | 经验系统 |
| 14 | testOrangePartnerMaxLevel | 橙将等级上限 |
| 15 | testOrangePartnerGrowth | 橙将成长 |

### PartnerBalanceV2Test (平衡性测试) - 16个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-4 | test*MaxLevel | 各品质等级上限 |
| 5-6 | testStar* | 星级系统 |
| 7-8 | test*GrowthRange | 成长值范围 |
| 9-11 | test*Zhanli | 战力平衡 |
| 12 | testTroopsByLevel | 兵量计算 |
| 13 | testSkillCount | 技能数量 |
| 14-15 | test*DamageFormula | 伤害公式 |
| 16 | testExpRequirement | 经验需求 |

### SkillEffectTest (技能系统) - 23个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-4 | testPhysical/Magic* | 物理/法术伤害 |
| 5-12 | test*Effect | 技能效果 |
| 13-23 | testSkillCombo | 技能组合 |

### PartnerTest (武将系统) - 12个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-12 | testPartner*/testDamage* | 基础属性/伤害 |

### BattleTest (战斗系统) - 13个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-13 | testBattle* | 战斗逻辑 |

### GachaServiceTest (抽卡系统) - 6个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-6 | testGacha* | 抽卡/保底 |

### ApiIntegrationTest (接口测试) - 14个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-14 | test*API | REST API |

### JwtUtilTest (JWT工具) - 3个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-3 | testJwt* | Token工具 |

---

## 三、核心平衡验证

### 3.1 战力差距控制

| 对比 | 1星差距 | 满星(6星)差距 |
|------|----------|----------------|
| 红 vs 橙 | <2倍 | <3倍 |
| 红 vs 蓝 | <3倍 | <4倍 |

### 3.2 星级加成

| 星级 | 属性加成 |
|------|----------|
| 1星 | 0% |
| 2星 | +10% |
| 3星 | +25% |
| 4星 | +45% |
| 5星 | +70% |
| 6星 | +100% |

### 3.3 等级上限

| 品质 | 等级上限 |
|------|----------|
| 红 | 80级 |
| 橙 | 60级 |
| 紫 | 40级 |
| 蓝 | 30级 |

---

## 四、运行测试

```bash
# 运行所有测试
mvn test

# 运行等级星级测试
mvn test -Dtest=PartnerLevelStarTest

# 运行平衡性测试
mvn test -Dtest=PartnerBalanceV2Test
```

---

## 五、测试状态

| 测试类 | 方法数 | 状态 |
|--------|--------|------|
| PartnerLevelStarTest | 18 | ✅ 已编写 |
| PartnerBalanceV2Test | 16 | ✅ 已编写 |
| SkillEffectTest | 23 | ✅ 已编写 |
| PartnerTest | 12 | ✅ 已编写 |
| BattleTest | 13 | ✅ 已编写 |
| GachaServiceTest | 6 | ✅ 已编写 |
| ApiIntegrationTest | 14 | ✅ 已编写 |
| JwtUtilTest | 3 | ✅ 已编写 |

**总计**: 95+个测试用例

---

## 六、代码变更

1. `Partner.java` - 添加等级/星级/成长字段和方法
2. `PartnerLevelStarTest.java` - 新增18个测试
3. `PartnerBalanceV2Test.java` - 新增16个平衡测试

---

*报告更新: 2026-03-15 19:05*
