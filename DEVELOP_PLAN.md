# 封神榜 - 伐商演义 开发计划

**项目**: 封神榜 - 伐商演义 (H5/SLG游戏)  
**版本**: v3.0  
**更新**: 2026-03-15  
**状态**: 规划中

---

## 一、环境搭建

### 1.1 开发环境

| 环境 | 要求 | 说明 |
|------|------|------|
| JDK | 17+ | Java运行环境 |
| Maven | 3.8+ | 项目构建 |
| MySQL | 8.0+ | 数据库 |
| Node.js | 18+ | 前端构建(可选) |
| IDE | IntelliJ IDEA | 推荐 |

### 1.2 启动命令

```bash
# 1. 导入数据库
mysql -u root -p < sql/init.sql

# 2. 修改配置
vim src/main/resources/application.yml

# 3. 启动后端
mvn spring-boot:run

# 4. 部署前端
# 将index.html部署到nginx或直接访问
```

### 1.3 配置文件

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fengshen
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

---

## 二、开发阶段

### 阶段一: 核心数据 (第1-2天)

#### 目标
完成武将、法术、装备基础数据结构

#### 任务

| # | 任务 | 用例 | 测试步骤 |
|---|------|------|----------|
| 1.1 | Partner实体完善 | 创建武将 | 验证属性计算 |
| 1.2 | Skill实体完善 | 创建法术 | 验证法术效果 |
| 1.3 | Equipment实体 | 创建装备 | 验证装备属性 |
| 1.4 | Mount实体 | 创建坐骑 | 验证坐骑属性 |

#### 用例示例

```java
// 用例1.1: 创建武将
@Test
void testCreatePartner() {
    Partner p = new Partner();
    p.setName("哪吒");
    p.setQuality("red");
    p.setAtk(160);
    // 验证属性
    assertEquals(160, p.getAtk());
}
```

---

### 阶段二: 战斗系统 (第3-4天)

#### 目标
实现伤害公式和战斗逻辑

#### 任务

| # | 任务 | 用例 | 测试步骤 |
|---|------|------|----------|
| 2.1 | 物理伤害公式 | 物理攻击计算 | 验证各种防御值 |
| 2.2 | 法术伤害公式 | 法术攻击计算 | 验证各种智力值 |
| 2.3 | 暴击伤害 | 暴击触发 | 验证暴击伤害 |
| 2.4 | 战斗流程 | 回合战斗 | 验证胜负判定 |

#### 用例示例

```java
// 用例2.1: 物理伤害计算
@Test
void testPhysicalDamage() {
    // 武力180, 统率50
    // 伤害 = 180 × (1 - 50/250) = 144
    int damage = calcPhysicalDamage(180, 50);
    assertEquals(144, damage);
}

// 用例2.2: 最低伤害保障
@Test
void testMinDamage() {
    // 防御极高时伤害为1
    int damage = calcPhysicalDamage(100, 1000);
    assertEquals(1, damage);
}
```

---

### 阶段三: 抽卡系统 (第5-6天)

#### 目标
实现三种卡包和保底机制

#### 任务

| # | 任务 | 用例 | 测试步骤 |
|---|------|------|----------|
| 3.1 | 大卡包抽取 | 抽卡概率 | 验证红1.5%橙8.5% |
| 3.2 | 小卡包抽取 | 抽卡概率 | 验证橙5%紫35% |
| 3.3 | 紫将卡包 | 抽卡概率 | 验证紫20% |
| 3.4 | 20抽保底 | 保底触发 | 验证20抽必橙 |
| 3.5 | 6橙保底 | 保底触发 | 验证6橙必红 |

#### 用例示例

```java
// 用例3.1: 大卡包概率
@Test
void testBigGachaRate() {
    // 10000次抽卡, 验证红将概率1.5%±0.3%
    int redCount = 0;
    for (int i = 0; i < 10000; i++) {
        if (drawBigGacha() == RED) redCount++;
    }
    assertTrue(redCount > 120 && redCount < 180);
}

// 用例3.4: 20抽保底
@Test
void testPity20() {
    // 连续19抽无橙,第20抽必橙
    List<Partner> cards = draw19WithoutOrange();
    Partner card20 = draw20th();
    assertEquals("orange", card20.getQuality());
}
```

---

### 阶段四: 修仙系统 (第7-8天)

#### 目标
实现修仙和修炼功能

#### 任务

| # | 任务 | 用例 | 测试步骤 |
|---|------|------|----------|
| 4.1 | 修仙等级 | 升级消耗 | 验证结晶消耗 |
| 4.2 | 体修加成 | 属性计算 | 验证武力+50 |
| 4.3 | 法修加成 | 法术伤害 | 验证伤害+25% |
| 4.4 | 结晶加速 | 加速计算 | 验证100结晶/天 |

#### 用例示例

```java
// 用例4.1: 修仙升级
@Test
void testCultivationUpgrade() {
    Cultivation c = new Cultivation();
    c.setLevel(1);
    c.addExp(100); // 升级到2级
    assertEquals(2, c.getLevel());
}

// 用例4.2: 体修属性
@Test
void testBodyCultivation() {
    // 50级体修: 武力+50
    assertEquals(50, getBodyCultivationBonus(50));
}
```

---

### 阶段五: 装备坐骑 (第9-10天)

#### 目标
实现装备合成和坐骑获取

#### 任务

| # | 任务 | 用例 | 测试步骤 |
|---|------|------|----------|
| 5.1 | 材料合成 | 4材料=1装备 | 验证成功率 |
| 5.2 | 装备属性 | 属性叠加 | 验证最终属性 |
| 5.3 | 遗迹攻打 | 坐骑掉落 | 验证概率 |
| 5.4 | 坐骑穿戴 | 属性计算 | 验证加成 |

#### 用例示例

```java
// 用例5.1: 装备合成
@Test
void testEquipmentCraft() {
    // 4个玄铁=1蓝色装备
    Equipment e = craft(4, "iron_ingot");
    assertEquals("blue", e.getQuality());
}

// 用例5.3: 遗迹掉落
@Test
void testRuinsDrop() {
    // 仙府红坐骑10%
    int redCount = 0;
    for (int i = 0; i < 1000; i++) {
        if (attackRuins("xianfu") == RED_MOUNT) redCount++;
    }
    assertTrue(redCount > 50 && redCount < 150);
}
```

---

### 阶段六: 地图与城池 (第11-13天)

#### 目标
实现大地图和城池系统

#### 任务

| # | 任务 | 用例 | 测试步骤 |
|---|------|------|----------|
| 6.1 | 行军遇怪 | 30%触发 | 验证触发概率 |
| 6.2 | 自动挂机 | 收益计算 | 验证材料获取 |
| 6.3 | 城池建筑 | 升级消耗 | 验证产出 |
| 6.4 | 城池科技 | 加成计算 | 验证增产 |
| 6.5 | 迁城CD | 7天冷却 | 验证CD |
| 6.6 | 士气系统 | 距离计算 | 验证伤害加成 |

#### 用例示例

```java
// 用例6.1: 行军遇怪
@Test
void testEncounterRate() {
    // 30%遇怪概率
    int encounterCount = 0;
    for (int i = 0; i < 1000; i++) {
        if (randomEncounter()) encounterCount++;
    }
    assertTrue(encounterCount > 250 && encounterCount < 350);
}

// 用例6.6: 士气计算
@Test
void testMorale() {
    // 距离10: 100 - 10×5 = 50%
    assertEquals(50, calcMorale(10));
    // 最低50%
    assertEquals(50, calcMorale(20));
}
```

---

### 阶段七: 联盟与社交 (第14-16天)

#### 目标
实现联盟和好友系统

#### 任务

| # | 任务 | 用例 | 测试步骤 |
|---|------|------|----------|
| 7.1 | 创建联盟 | 条件验证 | 10级+500元宝 |
| 7.2 | 联盟科技 | 加成计算 | 验证全员加成 |
| 7.3 | 城池占领 | 联盟战争 | 验证占领 |
| 7.4 | 战队系统 | 组队战斗 | 验证3-5人 |
| 7.5 | 好友系统 | 友好度 | 验证互赠体力 |

---

### 阶段八: 前端整合 (第17-20天)

#### 目标
前端与后端API对接

#### 任务

| # | 任务 | 说明 |
|---|------|------|
| 8.1 | API对接 | 抽卡/战斗/地图 |
| 8.2 | 界面优化 | UI/UX |
| 8.3 | 性能优化 | 加载速度 |

---

## 三、测试用例汇总

### 测试分类

| 分类 | 数量 | 覆盖 |
|------|------|------|
| 单元测试 | 100+ | 核心算法 |
| 集成测试 | 50+ | API接口 |
| 端到端 | 20+ | 完整流程 |

### 核心测试用例

```java
// 1. 伤害公式测试 (10用例)
testPhysicalDamage_基础();
testPhysicalDamage_高防御();
testPhysicalDamage_最低伤害();
testMagicDamage_基础();
testMagicDamage_高智力();
testCritDamage_暴击();
testCritDamage_未暴击();
testDamage_兵力不足();

// 2. 抽卡系统测试 (15用例)
testBigGacha_概率();
testSmallGacha_概率();
testPurpleGacha_概率();
testPity20_触发();
testPity6Orange_触发();
testGachaWithGeneral_加成();

// 3. 修仙系统测试 (10用例)
testCultivation_升级();
testCultivation_加速();
testBodyCultivation_属性();
testMagicCultivation_伤害();

// 4. 装备系统测试 (10用例)
testCraft_成功率();
testEquipment_属性();
testMount_特技();
testMount_穿戴();

// 5. 地图系统测试 (10用例)
testEncounter_触发();
testMorale_计算();
testCity_建筑();
testCity_科技();
testRelocate_CD();

// 6. 联盟系统测试 (10用例)
testAlliance_创建();
testAlliance_科技();
testAlliance_城池();
testTeam_组队();
```

---

## 四、开发规范

### 4.1 代码规范

```java
// 命名规范
类名: PartnerService, GachaController
方法名: calculateDamage(), drawGacha()
变量名: partnerId, quality

// 日志规范
log.info("抽卡: playerId={}, result={}", playerId, result);
log.error("战斗异常: {}", e.getMessage());
```

### 4.2 Git规范

```bash
# 提交规范
git commit -m "[模块] 功能描述"

# 示例
git commit -m "[战斗] 物理伤害公式实现"
git commit -m "[抽卡] 20抽保底机制"
```

---

## 五、部署步骤

### 5.1 后端部署

```bash
# 1. 构建
mvn clean package -DskipTests

# 2. 运行
java -jar target/fengshen-game.jar

# 3. Docker (可选)
docker build -t fengshen-game .
docker run -p 8080:8080 fengshen-game
```

### 5.2 前端部署

```bash
# 1. 打包
# 直接使用index.html

# 2. Nginx配置
server {
    listen 80;
    root /var/www/html;
    index index.html;
}
```

---

## 六、进度跟踪

### 开发时间线

| 阶段 | 天数 | 累计 |
|------|------|------|
| 阶段一 | 2天 | 2天 |
| 阶段二 | 2天 | 4天 |
| 阶段三 | 2天 | 6天 |
| 阶段四 | 2天 | 8天 |
| 阶段五 | 2天 | 10天 |
| 阶段六 | 3天 | 13天 |
| 阶段七 | 3天 | 16天 |
| 阶段八 | 4天 | 20天 |

**预计总工期: 20天**

---

## 七、风险与应对

| 风险 | 影响 | 应对 |
|------|------|------|
| 数据库压力 | 性能 | 读写分离 |
| 并发抽卡 | 数据一致性 | 分布式锁 |
| 地图性能 | 加载慢 | 分区域加载 |
| 内存泄漏 | 长时间运行 | 定期重启 |

---

*文档版本: v1.0*  
*最后更新: 2026-03-15*
