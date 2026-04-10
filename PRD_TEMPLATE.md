# 封神榜游戏功能开发 PRD 模板

```json
{
  "projectName": "fengshen-game",
  "branchName": "feature/",
  "userStories": [
    {
      "id": "US-001",
      "title": "功能名称",
      "description": "功能详细描述",
      "acceptanceCriteria": [
        "验收标准1",
        "验收标准2",
        "验收标准3"
      ],
      "passes": false,
      "technicalNotes": "技术实现要点"
    }
  ]
}
```

## 使用方法

### 1. 创建功能PRD

编辑 `prd.json`，添加用户故事：

```bash
# 示例：添加新功能
cat > prd.json << 'EOF'
{
  "projectName": "fengshen-game",
  "branchName": "feature/new-feature",
  "userStories": [
    {
      "id": "US-001",
      "title": "添加用户登录功能",
      "description": "实现基于JWT的用户认证系统",
      "acceptanceCriteria": [
        "用户可以使用用户名密码登录",
        "登录成功后返回JWT token",
        "token有效期为24小时",
        "密码错误返回明确提示"
      ],
      "passes": false,
      "technicalNotes": "使用Spring Security + JWT"
    }
  ]
}
EOF
```

### 2. 运行Ralph

```bash
./scripts/ralph/ralph.sh 10 claude
```

### 3. 查看进度

```bash
# 查看完成的故事
cat prd.json | jq '.userStories[] | {id, title, passes}'

# 查看学习记录
cat progress.txt
```

## 质量检查

Ralph会自动运行以下检查：
- `mvn compile` - 编译检查
- `mvn test` - 单元测试

如果检查失败，故事不会标记为完成，会进行重试。