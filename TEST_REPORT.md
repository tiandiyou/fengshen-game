# 封神榜后端测试报告

**生成时间**: 2026-03-15  
**项目**: 封神榜 - 伐商演义后端  
**状态**: 代码已完成，待运行环境测试

---

## 一、测试用例清单

### 1.1 PartnerTest (武将系统测试)

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testPartnerCreation | 创建武将对象 | 属性正确设置 |
| 2 | testSixDimensionalAttributes | 六维属性设置 | 武力/智力/统率/速度/政令/军事/魅力正确 |
| 3 | testPhysicalDamageCalculation | 物理伤害计算 | 伤害=武力-统率 |
| 4 | testMagicDamageCalculation | 法术伤害计算 | 伤害=智力-智力 |
| 5 | testGrowthSystem | 成长系统 | 等级提升属性增加 |
| 6 | testLevelUpSystem | 升级系统 | 经验足够可升级 |
| 7 | testZhanliCalculation | 战力计算 | 战力公式正确 |
| 8 | testStarSystem | 星级系统 | 0-5星 |
| 9 | testHiddenRedPartner | 隐藏红将 | 红将属性较高 |

### 1.2 BattleTest (战斗系统测试)

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testPhysicalAttack | 物理攻击 | 武力-统率=伤害 |
| 2 | testMagicAttack | 法术攻击 | 智力-智力=伤害 |
| 3 | testMinDamage | 最低伤害 | 最低为1 |
| 4 | testTroopsFromPlayerLevel | 兵量计算 | 10000+等级*1000 |
| 5 | testSpeedAffectsOrder | 速度系统 | 速度影响顺序 |
| 6 | testTypeCounter | 兵种克制 | 步>骑>弓>步 |
| 7 | testBattleScenario | 战斗场景模拟 | 伤害计算正确 |

### 1.3 GachaServiceTest (抽卡系统测试)

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testSingleDraw | 单抽 | 随机品质 |
| 2 | testGuaranteeAfter6Orange | 6抽保底 | 第6抽必出红 |
| 3 | testGuaranteeCounter | 保底计数器 | 计数正确 |
| 4 | testTenDraw | 十连抽 | 10个结果 |
| 5 | testTenDrawGuarantee | 十连抽保底 | 包含红将 |
| 6 | testProbabilityDistribution | 概率分布 | 概率在范围内 |

### 1.4 JwtUtilTest (JWT工具测试)

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testGenerateAndParseToken | 生成解析Token | 正确解析 |
| 2 | testInvalidToken | 无效Token | 抛出异常 |
| 3 | testNullToken | 空Token | 抛出异常 |

---

## 二、核心测试覆盖

### 2.1 伤害公式测试
```java
// 物理伤害 = 武力 - 统率
int damage = attacker.getCurrentAtk() - defender.getCurrentLead();
assertEquals(100, damage);

// 法术伤害 = 智力 - 法术防御(智力)
int magicDamage = attacker.getCurrentInt() - defender.getCurrentInt();
assertEquals(80, magicDamage);
```

### 2.2 兵量系统测试
```java
// 兵量由玩家等级决定
int troops = 10000 + playerLevel * 1000;
assertEquals(15000, troops); // 5级=15000
```

### 2.3 保底机制测试
```java
// 6抽必出红将
if (gachaCount >= 6) {
    assertEquals("red", result.getQuality());
    assertTrue(result.isGuarantee());
}
```

---

## 三、运行测试命令

```bash
# 在有Java环境的机器上运行
cd /root/.openclaw/workspace/fengshen-game
mvn test

# 运行特定测试类
mvn test -Dtest=PartnerTest
mvn test -Dtest=BattleTest
mvn test -Dtest=GachaServiceTest

# 生成测试报告
mvn test -Dtest=*Test -Dsurefire.useFile=false
```

---

## 四、测试状态

| 测试类 | 方法数 | 状态 |
|--------|--------|------|
| PartnerTest | 9 | ✅ 已编写 |
| BattleTest | 7 | ✅ 已编写 |
| GachaServiceTest | 6 | ✅ 已编写 |
| JwtUtilTest | 3 | ✅ 已编写 |

**总计**: 25个测试用例

---

## 五、待完善

- [ ] 在Java环境中运行测试
- [ ] 添加更多集成测试
- [ ] 添加API端到端测试

---

*报告生成时间: 2026-03-15 18:10*
