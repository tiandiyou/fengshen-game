#!/bin/bash
# Ralph Loop Script for fengshen-game
# 自动运行AI编码循环直到所有PRD项目完成

set -e

# 配置
MAX_ITERATIONS=${1:-10}
AI_TOOL=${2:-claude}  # claude, anthropic, ollama
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
RALPH_DIR="$(dirname "$0")"

cd "$PROJECT_DIR"

echo "========================================"
echo "⚔️  fengshen-game Ralph Loop"
echo "========================================"
echo "项目: $PROJECT_DIR"
echo "最大迭代: $MAX_ITERATIONS"
echo "AI工具: $AI_TOOL"
echo ""

# 检查PRD文件
if [ ! -f "prd.json" ]; then
    echo "❌ 未找到 prd.json，请先创建功能规格文档"
    exit 1
fi

# 检查分支名称
BRANCH_NAME=$(jq -r '.branchName // "feature/ralph"' prd.json)
USER_STORIES=$(jq -r '.userStories[] | select(.passes == false)' prd.json)

if [ -z "$USER_STORIES" ]; then
    echo "✅ 所有用户故事已完成！"
    exit 0
fi

# 创建特性分支
git checkout -b "$BRANCH_NAME" 2>/dev/null || git checkout "$BRANCH_NAME"
echo "📂 当前分支: $BRANCH_NAME"

# 初始化进度文件
echo "# Ralph Progress - $(date)" >> progress.txt
echo "" >> progress.txt

# 主循环
ITERATION=1
while [ $ITERATION -le $MAX_ITERATIONS ]; do
    echo ""
    echo "========================================"
    echo "🔄 迭代 $ITERATION/$MAX_ITERATIONS"
    echo "========================================"
    
    # 获取最高优先级未完成的故事
    CURRENT_STORY=$(jq -r '.userStories[] | select(.passes == false) | .id' prd.json | head -1)
    STORY_TITLE=$(jq -r ".userStories[] | select(.id == \"$CURRENT_STORY\") | .title" prd.json)
    
    if [ "$CURRENT_STORY" = "null" ] || [ -z "$CURRENT_STORY" ]; then
        echo "✅ 所有用户故事已完成！"
        echo "<promise>COMPLETE</promise>" >> progress.txt
        break
    fi
    
    echo "📋 当前任务: [$CURRENT_STORY] $STORY_TITLE"
    
    # 执行AI编码
    case $AI_TOOL in
        claude)
            # 使用Claude Code
            claude --dangerously-skip-permissions << 'EOF'
You are working on fengshen-game project.

Current task from prd.json:
$STORY_TITLE

Steps:
1. Read the current prd.json to understand requirements
2. Implement the story
3. Run quality checks (compile, test)
4. If checks pass, commit with descriptive message
5. Update prd.json to mark this story as passes: true
6. Append learnings to progress.txt

Quality checks:
- mvn compile
- mvn test

Remember:
- Each commit should be small and focused
- Update AGENTS.md with any discoveries
- Mark story complete in prd.json only after tests pass
EOF
            ;;
        anthropic)
            # 使用 Anthropic API
            echo "TODO: 实现 Anthropic API 调用"
            ;;
        *)
            echo "❌ 未知的AI工具: $AI_TOOL"
            exit 1
    esac
    
    # 检查是否完成
    PASSES_COUNT=$(jq '[.userStories[] | select(.passes == true)] | length' prd.json)
    TOTAL_COUNT=$(jq '.userStories | length' prd.json)
    
    echo "📊 进度: $PASSES_COUNT/$TOTAL_COUNT 完成"
    
    # 记录学习
    echo "[迭代 $ITERATION] $(date): 完成 $CURRENT_STORY" >> progress.txt
    
    ITERATION=$((ITERATION + 1))
done

if [ $ITERATION -gt $MAX_ITERATIONS ]; then
    echo ""
    echo "⚠️  达到最大迭代次数 $MAX_ITERATIONS"
    echo "请检查 progress.txt 和 prd.json 了解进度"
fi

echo ""
echo "========================================"
echo "🏁 Ralph Loop 完成"
echo "========================================"
echo "查看进度: cat progress.txt"
echo "查看任务: cat prd.json | jq '.userStories'"