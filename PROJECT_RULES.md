# 封神榜项目开发 Rules

## 📋 项目概述

**项目名称**: 封神榜-伐商演义
**技术栈**: Spring Boot + JPA + H5前端 + MySQL
**部署地址**: 1.14.139.77

---

## 🔧 开发规范

### 1. 代码管理
- 使用Git进行版本控制
- 定期提交，避免长时间不提交
- 提交信息要清晰描述改动内容

### 2. 部署流程
- 部署前检查服务器资源（内存、CPU）
- 前端代码提交前做语法检查
- 部署后验证功能正常

### 3. 问题排查
- Java进程被Kill → 检查内存是否不足
- 前端JS错误 → 检查括号、引号是否匹配
- API调用失败 → 检查参数是否正确

---

## 🤖 AI技能使用规范

### 已安装Skills

| 技能 | 用途 | 配置要求 |
|------|------|----------|
| weather | 免费天气查询 | ✅ 无需配置 |
| tavily-search | AI搜索 | ❌ 需要API Key |
| summarize | 内容总结 | ❌ 需要GEMINI_API_KEY |
| agent-browser | 浏览器自动化 | ❌ 需要Chrome |
| web-pilot | 网页搜索 | ⚠️ DuckDuckGo连不上 |
| capability-evolver | 能力进化 | ✅ 已配置 |
| self-evolving-skill | 自我学习 | ⚠️ 需core模块 |
| deep-research-pro | 深度研究 | ✅ 已安装 |
| self-improving-agent | 自我改进 | ✅ 已安装 |
| frontend | 前端开发 | ✅ 已安装 |
| superpowers | TDD开发 | ✅ 已安装 |

### 常用工具

| 工具 | 用途 |
|------|------|
| web_fetch | 抓取网页内容 |
| curl | 命令行获取网页 |
| Playwright | 前端自动化测试 |

---

## 🧪 测试规范

### API测试
- 使用Playwright进行自动化测试
- 覆盖核心业务场景
- 每次代码变更后运行测试

### 前端测试
- 首页加载测试
- 登录/注册功能测试
- JavaScript错误检测
- 响应式设计测试

---

## 📈 优化记录

### 已完成优化
1. ✅ HikariCP连接池配置
2. ✅ JPA批量操作优化
3. ✅ API请求缓存模块
4. ✅ 性能监控模块
5. ✅ Playwright测试框架
6. ✅ AI测试引擎

### 待优化
- [ ] 前端代码分割
- [ ] 图片懒加载
- [ ] MySQL数据库迁移
- [ ] Redis缓存集成

---

## 📝 常见问题

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 进程被Kill | 内存不足 | 检查OOM，增加内存 |
| JS报错 missing ) | 语法错误 | 检查括号/引号 |
| 抽卡API 500 | 参数错误 | 需传playerId参数 |

---

## 🎯 今日工作 (2026-03-17)

1. ✅ 自我学习总结
2. ✅ Skills安装 (capability-evolver, self-evolving-skill等)
3. ✅ Tavily API配置完成
4. ✅ AI行业洞察（阿里、英伟达GTC）
5. ✅ API测试框架创建
6. ✅ 前端Playwright测试
7. ✅ 项目优化（HikariCP、缓存、监控）
8. ✅ 安装frontend、superpowers技能

*最后更新: 2026-03-17 15:00*
