# 封神榜后端测试报告

**生成时间**: 2026-03-15 18:45  
**项目**: 封神榜 - 伐商演义后端  
**伤害公式**: 方案B (百分比穿透)

---

## 一、武将品质系统

### 1.1 品质构成
```
综合战力 = 初始属性 + 成长属性 + 技能属性
```

### 1.2 品质分布

| 品质 | 数量 | 显示 | 初始属性 | 成长值 | 技能数 |
|------|------|------|----------|--------|--------|
| 红色(SSR) | 10 | 橙色 | 最高 | 最高 | 2个 |
| 橙色(SR) | 30 | 橙色 | 高 | 高 | 1-2个 |
| 紫色(R) | 30 | 紫色 | 中 | 中 | 1个 |
| 蓝色(N) | 30 | 蓝色 | 低 | 低 | 0个 |

---

## 二、技能系统

### 2.1 技能类型

| 类型 | 效果 | 示例 |
|------|------|------|
| **伤害类** | 物理/法术伤害加成 | 物理伤害+30% |
| **异常类** | 状态控制 | 石化、流血、沉默 |
| **恢复类** | 治疗/护盾 | 恢复兵量、护盾 |
| **增益类** | 属性提升 | 攻击+%、防御+%、速度+ |

### 2.2 技能效果

| 效果类型 | 值 | 持续 | 描述 |
|----------|-----|------|------|
| physical_damage | 30-60% | - | 物理伤害加成 |
| magic_damage | 30-60% | - | 法术伤害加成 |
| stone | - | 2-3回合 | 石化(无法行动) |
| bleed | 30-50 | 3回合 | 流血(每回合伤害) |
| heal | 200-500 | - | 恢复兵量 |
| shield | 150-300 | 2-3回合 | 护盾 |
| silence | - | 2-3回合 | 沉默(无法释放技能) |
| atk_up | 15-30% | - | 攻击力加成 |
| def_up | 15-30% | - | 防御力加成 |
| speed_up | 20-40 | - | 速度加成 |
| crit_rate | 10-20% | - | 暴击率 |
| dodge | 10-15% | - | 闪避率 |

---

## 三、伤害公式 (方案B)

```
物理伤害 = 武力 × (1 - 统率 / (统率 + 200))
法术伤害 = 智力 × (1 - 智力 / (智力 + 200))
```

---

## 四、测试用例清单 (共78个)

### 4.1 SkillEffectTest (技能系统测试) - 23个

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testPhysicalDamageEffect | 物理伤害效果 | 类型=physical_damage |
| 2 | testMagicDamageEffect | 法术伤害效果 | 类型=magic_damage |
| 3 | testPhysicalDamageCalculation | 物理伤害计算 | 150%加成=150伤害 |
| 4 | testMagicDamageCalculation | 法术伤害计算 | 130%加成=104伤害 |
| 5 | testStoneEffect | 石化效果 | 2回合石化 |
| 6 | testBleedEffect | 流血效果 | 3回合伤害 |
| 7 | testBleedDamageCalculation | 流血总伤害 | 50×3=150 |
| 8 | testHealEffect | 治疗效果 | 恢复200兵量 |
| 9 | testHealCalculation | 治疗计算 | 不超过最大兵量 |
| 10 | testShieldEffect | 护盾效果 | 吸收300伤害 |
| 11 | testShieldCalculation | 护盾计算 | 吸收后剩余伤害 |
| 12 | testSilenceEffect | 沉默效果 | 2回合沉默 |
| 13 | testAtkUpEffect | 攻击提升 | +20% |
| 14 | testDefUpEffect | 防御提升 | +15% |
| 15 | testSpeedUpEffect | 速度提升 | +30 |
| 16 | testCritRateEffect | 暴击率 | +15% |
| 17 | testDodgeEffect | 闪避率 | +10% |
| 18 | testSkillCombo_DamageAndDebuff | 技能组合 | 伤害+异常 |
| 19 | testSkillCombo_HealAndShield | 技能组合 | 治疗+护盾 |
| 20 | testSkillTiming | 技能时效 | 时长验证 |
| 21 | testCritWithDamageBonus | 暴击+伤害 | 180%伤害 |
| 22 | testQualitySkillPoints | 品质技能点 | 红2/橙1/紫1/蓝0 |
| 23 | testSkillEffectValuesByQuality | 技能效果值 | 品质越高效果越好 |

### 4.2 PartnerBalanceTest (武将平衡性测试) - 19个

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1-5 | test*PartnerCount | 各品质数量 | 10/30/30/30 |
| 6-9 | test*PartnerAttributes | 各品质属性范围 | 符合范围 |
| 10-11 | testSkillDistribution | 技能分布 | 2/1/0技能 |
| 12 | testSkillTypes | 技能类型 | 验证技能存在 |
| 13 | testRedPartnerDamage | 红将输出 | ≥25% |
| 14 | testTypeDistribution | 兵种分布 | 步/骑/弓/策 |
| 15-16 | testGrowth* | 成长值 | 1-10范围/红>橙 |
| 17-18 | testUnique* | 唯一性 | ID/名称唯一 |
| 19 | testCombatPowerByQuality | 战力对比 | 红>橙>紫>蓝 |

### 4.3 PartnerTest (武将系统) - 12个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-2 | testPartnerCreation/Attributes | 创建和属性 |
| 3-6 | testPhysicalDamage_* | 物理伤害 |
| 7-9 | testMagicDamage_* | 法术伤害 |
| 10-12 | testGrowth/LevelUp/Zhanli | 成长/升级/战力 |

### 4.4 BattleTest (战斗系统) - 13个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-3 | testPhysicalAttack_* | 物理攻击 |
| 4-5 | testMagicAttack_* | 法术攻击 |
| 6-12 | testBattleScenario | 战斗场景 |

### 4.5 GachaServiceTest (抽卡系统) - 6个

| 序号 | 测试方法 | 测试内容 |
|------|----------|----------|
| 1-6 | testSingleDraw/Guarantee/TenDraw | 抽卡/保底 |

### 4.6 ApiIntegrationTest (接口测试) - 14个

| 序号 | 测试方法 | 接口 |
|------|----------|------|
| 1-14 | test*/API | 认证/玩家/抽卡/战斗等 |

---

## 五、运行测试命令

```bash
# 运行所有测试
mvn test

# 运行技能测试
mvn test -Dtest=SkillEffectTest

# 运行平衡性测试
mvn test -Dtest=PartnerBalanceTest

# 运行接口测试
mvn test -Dtest=ApiIntegrationTest
```

---

## 六、测试状态

| 测试类 | 方法数 | 状态 |
|--------|--------|------|
| SkillEffectTest | 23 | ✅ 已编写 |
| PartnerBalanceTest | 19 | ✅ 已编写 |
| PartnerTest | 12 | ✅ 已编写 |
| BattleTest | 13 | ✅ 已编写 |
| GachaServiceTest | 6 | ✅ 已编写 |
| ApiIntegrationTest | 14 | ✅ 已编写 |
| JwtUtilTest | 3 | ✅ 已编写 |

**总计**: 90个测试用例

---

## 七、新增文件

1. `src/main/java/com/game/config/SkillEffect.java` - 技能效果类
2. `src/test/java/com/game/SkillEffectTest.java` - 技能系统测试
3. 更新 `PartnerBalanceTest.java` - 增加技能测试

---

*报告更新: 2026-03-15 18:45*
