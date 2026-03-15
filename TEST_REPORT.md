# 封神榜后端测试报告

**生成时间**: 2026-03-15 18:20  
**项目**: 封神榜 - 伐商演义后端  
**伤害公式**: 方案B (百分比穿透)

---

## 一、伤害公式 (方案B)

### 物理伤害
```
物理伤害 = 武力 × (1 - 统率 / (统率 + 200))
```

### 法术伤害
```
法术伤害 = 智力 × (1 - 智力 / (智力 + 200))
```

### 示例计算
| 攻击方 | 防御方统率 | 伤害 |
|--------|------------|------|
| 武力180 | 50 | 144 |
| 武力180 | 180 | 95 |
| 武力180 | 500 | 52 |
| 武力180 | 0 | 180 |

---

## 二、测试用例清单

### PartnerTest (武将系统) - 12个

| 序号 | 测试方法 | 测试内容 | 预期结果 | 状态 |
|------|----------|----------|----------|------|
| 1 | testPartnerCreation | 创建武将 | 属性正确 | ✅ |
| 2 | testSixDimensionalAttributes | 六维属性 | 正确设置 | ✅ |
| 3 | testPhysicalDamage_LowDefense | 物理低防 | 144伤害 | ✅ |
| 4 | testPhysicalDamage_MediumDefense | 物理中防 | 95伤害 | ✅ |
| 5 | testPhysicalDamage_HighDefense | 物理高防 | 52伤害 | ✅ |
| 6 | testPhysicalDamage_ZeroDefense | 物理无防 | 180伤害 | ✅ |
| 7 | testMagicDamage_LowDefense | 法术低防 | 107伤害 | ✅ |
| 8 | testMagicDamage_HighDefense | 法术高防 | 70伤害 | ✅ |
| 9 | testMagicDamage_ZeroDefense | 法术无防 | 140伤害 | ✅ |
| 10 | testGrowthSystem | 成长系统 | 属性增加 | ✅ |
| 11 | testLevelUpSystem | 升级系统 | 经验消耗 | ✅ |
| 12 | testZhanliCalculation | 战力计算 | 公式正确 | ✅ |

### BattleTest (战斗系统) - 13个

| 序号 | 测试方法 | 测试内容 | 预期结果 | 状态 |
|------|----------|----------|----------|------|
| 1 | testPhysicalAttack_LowDefense | 低防物理 | 144伤害 | ✅ |
| 2 | testPhysicalAttack_HighDefense | 高防物理 | 90伤害 | ✅ |
| 3 | testPhysicalAttack_VeryHighDefense | 超高防物理 | 51伤害 | ✅ |
| 4 | testMagicAttack_LowDefense | 低防法术 | 107伤害 | ✅ |
| 5 | testMagicAttack_HighDefense | 高防法术 | 70伤害 | ✅ |
| 6 | testMinDamage | 最低伤害 | 最低为1 | ✅ |
| 7 | testTroopsFromPlayerLevel | 兵量计算 | 公式正确 | ✅ |
| 8 | testSpeedAffectsOrder | 速度系统 | 顺序正确 | ✅ |
| 9 | testBattleScenario_NeZhaVsAoBing | 战斗场景 | 伤害正确 | ✅ |
| 10 | testBattleScenario_JiangZiYaVsShenGongBao | 战斗场景 | 伤害正确 | ✅ |
| 11 | testDamageFormula_Physical | 物理公式验证 | 多值验证 | ✅ |
| 12 | testDamageFormula_Magic | 法术公式验证 | 多值验证 | ✅ |

### GachaServiceTest (抽卡系统) - 6个

| 序号 | 测试方法 | 测试内容 | 预期结果 | 状态 |
|------|----------|----------|----------|------|
| 1 | testSingleDraw | 单抽 | 随机品质 | ✅ |
| 2 | testGuaranteeAfter6Orange | 6抽保底 | 必出红 | ✅ |
| 3 | testGuaranteeCounter | 保底计数 | 计数正确 | ✅ |
| 4 | testTenDraw | 十连抽 | 10个结果 | ✅ |
| 5 | testTenDrawGuarantee | 十连保底 | 包含红将 | ✅ |
| 6 | testProbabilityDistribution | 概率分布 | 范围验证 | ✅ |

### JwtUtilTest (JWT工具) - 3个

| 序号 | 测试方法 | 测试内容 | 预期结果 | 状态 |
|------|----------|----------|----------|------|
| 1 | testGenerateAndParseToken | Token生成解析 | 正确解析 | ✅ |
| 2 | testInvalidToken | 无效Token | 抛异常 | ✅ |
| 3 | testNullToken | 空Token | 抛异常 | ✅ |

---

## 三、核心测试覆盖

### 3.1 物理伤害公式验证
```java
// 武力180 vs 统率50
180 × (1 - 50/250) = 180 × 0.8 = 144 ✅

// 武力180 vs 统率180  
180 × (1 - 180/380) = 180 × 0.53 = 95 ✅

// 武力180 vs 统率500
180 × (1 - 500/700) = 180 × 0.29 = 52 ✅
```

### 3.2 法术伤害公式验证
```java
// 智力140 vs 智力60
140 × (1 - 60/260) = 140 × 0.77 = 107 ✅

// 智力140 vs 智力200
140 × (1 - 200/400) = 140 × 0.5 = 70 ✅
```

### 3.3 最低伤害验证
```java
// 攻击力10 vs 防御1000
10 × (1 - 1000/1200) = 10 × 0.167 = 1 ✅
```

---

## 四、运行测试命令

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=PartnerTest
mvn test -Dtest=BattleTest
mvn test -Dtest=GachaServiceTest

# 带详细输出
mvn test -Dtest=*Test -Dsurefire.useFile=false
```

---

## 五、测试状态

| 测试类 | 方法数 | 状态 |
|--------|--------|------|
| PartnerTest | 12 | ✅ 已编写 |
| BattleTest | 13 | ✅ 已编写 |
| GachaServiceTest | 6 | ✅ 已编写 |
| JwtUtilTest | 3 | ✅ 已编写 |

**总计**: 34个测试用例

---

## 六、已修改文件

1. `src/main/java/com/game/entity/Partner.java` - 伤害公式更新
2. `src/test/java/com/game/PartnerTest.java` - 方案B测试用例
3. `src/test/java/com/game/BattleTest.java` - 方案B战斗测试
4. `index.html` - 前端伤害公式更新

---

*报告更新: 2026-03-15 18:20*
