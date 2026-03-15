# 封神榜后端测试报告

**生成时间**: 2026-03-15 18:30  
**项目**: 封神榜 - 伐商演义后端  
**伤害公式**: 方案B (百分比穿透)

---

## 一、武将数据统计

| 品质 | 数量 | 显示颜色 | 实际品质 |
|------|------|----------|----------|
| 红色(SSR) | 10 | 橙色 | 红色(隐藏) |
| 橙色(SR) | 30 | 橙色 | 橙色 |
| 紫色(R) | 30 | 紫色 | 紫色 |
| 蓝色(N) | 30 | 蓝色 | 蓝色 |
| **总计** | **100** | | |

---

## 二、伤害公式 (方案B)

```
物理伤害 = 武力 × (1 - 统率 / (统率 + 200))
法术伤害 = 智力 × (1 - 智力 / (智力 + 200))
```

---

## 三、测试用例清单 (共54个)

### 3.1 PartnerBalanceTest (武将平衡性测试) - 14个

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testPartnerCount | 武将总数 | 100个 |
| 2 | testRedPartnerCount | 红将数量 | 10个 |
| 3 | testOrangePartnerCount | 橙将数量 | 30个 |
| 4 | testPurplePartnerCount | 紫将数量 | 30个 |
| 5 | testBluePartnerCount | 蓝将数量 | 30个 |
| 6 | testRedPartnerAttributes | 红将属性 | 武力/智力/统率≥150 |
| 7 | testOrangePartnerAttributes | 橙将属性 | 30-170范围 |
| 8 | testPurplePartnerAttributes | 紫将属性 | 10-100范围 |
| 9 | testBluePartnerAttributes | 蓝将属性 | 5-40范围 |
| 10 | testRedPartnerDamage | 红将输出 | ≥25%攻击力 |
| 11 | testTypeDistribution | 兵种分布 | 步/骑/弓/策 |
| 12 | testGrowthValues | 成长值 | 1-10范围 |
| 13 | testUniqueIds | ID唯一性 | 无重复 |
| 14 | testUniqueNames | 名称唯一性 | 无重复 |

### 3.2 PartnerTest (武将系统) - 12个

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testPartnerCreation | 创建武将 | 属性正确 |
| 2 | testSixDimensionalAttributes | 六维属性 | 正确设置 |
| 3-6 | testPhysicalDamage_* | 物理伤害 | 公式验证 |
| 7-9 | testMagicDamage_* | 法术伤害 | 公式验证 |
| 10 | testGrowthSystem | 成长系统 | 属性增加 |
| 11 | testLevelUpSystem | 升级系统 | 经验消耗 |
| 12 | testZhanliCalculation | 战力计算 | 公式正确 |

### 3.3 BattleTest (战斗系统) - 13个

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1-3 | testPhysicalAttack_* | 物理攻击 | 伤害计算 |
| 4-5 | testMagicAttack_* | 法术攻击 | 伤害计算 |
| 6 | testMinDamage | 最低伤害 | ≥1 |
| 7 | testTroopsFromPlayerLevel | 兵量计算 | 公式正确 |
| 8 | testSpeedAffectsOrder | 速度系统 | 顺序正确 |
| 9-10 | testBattleScenario | 战斗场景 | 多场景验证 |
| 11-12 | testDamageFormula_* | 公式验证 | 多值验证 |

### 3.4 GachaServiceTest (抽卡系统) - 6个

| 序号 | 测试方法 | 测试内容 | 预期结果 |
|------|----------|----------|----------|
| 1 | testSingleDraw | 单抽 | 随机品质 |
| 2 | testGuaranteeAfter6Orange | 6抽保底 | 必出红 |
| 3 | testGuaranteeCounter | 保底计数 | 计数正确 |
| 4 | testTenDraw | 十连抽 | 10个结果 |
| 5 | testTenDrawGuarantee | 十连保底 | 包含红将 |
| 6 | testProbabilityDistribution | 概率分布 | 范围验证 |

### 3.5 ApiIntegrationTest (接口测试) - 14个

| 序号 | 测试方法 | 接口 | 预期结果 |
|------|----------|------|----------|
| 1 | testRegister | POST /api/auth/register | 200 OK |
| 2 | testLogin | POST /api/auth/login | 200 OK |
| 3 | testGetPlayer | GET /api/player/{id} | 200 OK |
| 4 | testGetPlayerNotFound | GET /api/player/99999 | 404 |
| 5 | testGachaDraw | POST /api/gacha/draw | 200 OK |
| 6 | testGachaDrawInsufficientLingqi | POST /api/gacha/draw | 灵气不足 |
| 7 | testBattleStart | POST /api/battle/start | 200 OK |
| 8 | testGetQuests | GET /api/quest/list | 200 OK |
| 9 | testGetSigninStatus | GET /api/signin/status | 200 OK |
| 10 | testDoSignin | POST /api/signin/do | 200 OK |
| 11 | testGetRank | GET /api/rank/list | 200 OK |
| 12 | testGetSkills | GET /api/skill/list | 200 OK |
| 13 | testGetPartners | GET /api/partner/list | 200 OK |
| 14 | testGetShopItems | GET /api/shop/list | 200 OK |

---

## 四、属性平衡性验证

### 4.1 各品质武力平均值
```
红将(SSR): ~145 武力
橙将(SR):  ~95  武力
紫将(R):   ~55  武力
蓝将(N):   ~18  武力
```

### 4.2 伤害输出比例
- 红将 vs 橙将: +50%+
- 橙将 vs 紫将: +70%+
- 紫将 vs 蓝将: +200%+

---

## 五、运行测试命令

```bash
# 运行所有测试
mvn test

# 运行平衡性测试
mvn test -Dtest=PartnerBalanceTest

# 运行接口测试
mvn test -Dtest=ApiIntegrationTest

# 生成测试报告
mvn test -Dtest=*Test -Dsurefire.useFile=false
```

---

## 六、测试状态

| 测试类 | 方法数 | 状态 |
|--------|--------|------|
| PartnerBalanceTest | 14 | ✅ 已编写 |
| PartnerTest | 12 | ✅ 已编写 |
| BattleTest | 13 | ✅ 已编写 |
| GachaServiceTest | 6 | ✅ 已编写 |
| ApiIntegrationTest | 14 | ✅ 已编写 |
| JwtUtilTest | 3 | ✅ 已编写 |

**总计**: 62个测试用例

---

## 七、新增文件

1. `src/main/java/com/game/config/PartnerData.java` - 100+武将数据
2. `src/test/java/com/game/PartnerBalanceTest.java` - 武将平衡性测试
3. `src/test/java/com/game/ApiIntegrationTest.java` - 接口自动化测试
4. `TEST_REPORT.md` - 测试报告

---

*报告更新: 2026-03-15 18:30*
