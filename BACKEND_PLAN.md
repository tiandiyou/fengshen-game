# 封神榜后端开发计划

## 待实现功能清单

### 阶段1: 玩家等级系统
- [ ] Player表增加level字段
- [ ] Player表增加exp字段
- [ ] 等级计算API
- [ ] 兵量计算API: troops = 10000 + level * 1000

### 阶段2: 战法系统
- [ ] Skill实体类
- [ ] SkillRepository
- [ ] SkillController (CRUD)
- [ ] 战法数据初始化

### 阶段3: 5+1抽卡系统
- [ ] GachaRecord实体 (抽卡记录)
- [ ] 保底计数器
- [ ] 6抽保底逻辑
- [ ] 随机武将逻辑

### 阶段4: 战斗系统重构
- [ ] 物理伤害 = 武力 - 统率
- [ ] 法术伤害 = 智力 - 法术防御
- [ ] 兵量计算

---

## 测试用例计划

### 单元测试
- [ ] JwtUtil测试
- [ ] PlayerService测试
- [ ] GachaService保底测试

### 集成测试
- [ ] 注册/登录API测试
- [ ] 抽卡API测试
- [ ] 战斗API测试

---

*创建时间: 2026-03-15*
