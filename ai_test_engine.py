"""
AI驱动的API场景识别与增量测试引擎
- 自动解析Controller生成API清单
- 智能识别依赖关系
- 增量场景检测
- 自动生成测试链路
"""

import os
import re
import json
from pathlib import Path
from typing import Dict, List, Set, Tuple
from dataclasses import dataclass, field
from collections import defaultdict
import hashlib

@dataclass
class APIDef:
    """API定义"""
    controller: str
    endpoint: str
    method: str
    path: str
    requires_auth: bool = True
    params: List[str] = field(default_factory=list)
    return_type: str = ""
    description: str = ""

class APIAnalyzer:
    """API分析器 - 从Controller自动提取"""
    
    def __init__(self, project_path: str):
        self.project_path = Path(project_path)
        self.controllers_path = self.project_path / "src/main/java/com/game/controller"
        
    def scan_all_apis(self) -> Dict[str, APIDef]:
        """扫描所有Controller生成API清单"""
        apis = {}
        
        if not self.controllers_path.exists():
            print(f"❌ Controller目录不存在: {self.controllers_path}")
            return apis
        
        for java_file in self.controllers_path.glob("*Controller.java"):
            controller_name = java_file.stem.replace("Controller", "")
            
            content = java_file.read_text(encoding='utf-8')
            
            # 提取所有Mapping
            for match in re.finditer(
                r'@(Get|Post|Put|Delete)Mapping\(?(?:value\s*=\s*)?["\']([^"\']*)["\']',
                content
            ):
                method = match.group(1)
                path = match.group(2)
                
                api_key = f"{controller_name.lower()}_{path.replace('/', '_')}"
                
                # 判断是否需要认证
                requires_auth = not any([
                    "register" in path.lower(),
                    "login" in path.lower(),
                ])
                
                apis[api_key] = APIDef(
                    controller=controller_name,
                    endpoint=path,
                    method=method,
                    path=f"/{controller_name.lower()}{path}",
                    requires_auth=requires_auth
                )
        
        return apis
    
    def build_dependency_graph(self, apis: Dict[str, APIDef]) -> Dict[str, Set[str]]:
        """构建依赖图"""
        dependencies = defaultdict(set)
        
        # 基于路径关键字推断依赖
        dependency_rules = {
            "create": ["login", "register"],
            "draw": ["create", "heroes"],
            "battle": ["create", "draw"],
            "join": ["create"],
            "buy": ["list"],
            "claim": ["list"],
            "upgrade": ["list"],
            "produce": ["init"],
            "collect": ["init"],
            "move": ["zones", "init"],
            "practice": ["choose", "info"],
            "result": ["start"],
        }
        
        for api_key, api in apis.items():
            for keyword, deps in dependency_rules.items():
                if keyword in api.endpoint.lower():
                    for dep in deps:
                        for other_key, other_api in apis.items():
                            if dep in other_api.endpoint.lower():
                                dependencies[api_key].add(other_key)
        
        return dict(dependencies)


class IncrementalTester:
    """增量测试引擎"""
    
    def __init__(self, project_path: str):
        self.project_path = project_path
        self.api_file = f"{project_path}/.api_hash.json"
        self.previous_apis = self._load_previous()
        
    def _load_previous(self) -> Dict:
        """加载之前的API快照"""
        if os.path.exists(self.api_file):
            with open(self.api_file, 'r') as f:
                return json.load(f)
        return {}
    
    def _save_current(self, apis: Dict):
        """保存当前API快照"""
        with open(self.api_file, 'w') as f:
            json.dump(apis, f, indent=2)
    
    def compute_hash(self, api_data: Dict) -> str:
        """计算API数据的Hash"""
        content = json.dumps(api_data, sort_keys=True)
        return hashlib.md5(content.encode()).hexdigest()
    
    def detect_changes(self, current_apis: Dict) -> Dict:
        """检测增量变化"""
        previous = self.previous_apis.get("apis", {})
        
        added = set(current_apis.keys()) - set(previous.keys())
        removed = set(previous.keys()) - set(current_apis.keys())
        modified = set()
        
        # 检查修改
        for api_key in set(current_apis.keys()) & set(previous.keys()):
            old_hash = previous[api_key].get("hash", "")
            new_hash = self.compute_hash(current_apis[api_key])
            if old_hash != new_hash:
                modified.add(api_key)
        
        return {
            "added": list(added),
            "removed": list(removed),
            "modified": list(modified),
            "total": len(current_apis)
        }
    
    def generate_impact_chain(self, changed_apis: List[str], all_apis: Dict, dependencies: Dict) -> List[str]:
        """生成影响链 - 包含依赖的API"""
        impact_set = set(changed_apis)
        queue = list(changed_apis)
        
        while queue:
            current = queue.pop(0)
            
            # 找出依赖当前API的其他API
            for api_key, deps in dependencies.items():
                if current in deps:
                    if api_key not in impact_set:
                        impact_set.add(api_key)
                        queue.append(api_key)
            
            # 找出当前API依赖的
            if current in dependencies:
                for dep in dependencies[current]:
                    if dep in all_apis and dep not in impact_set:
                        impact_set.add(dep)
                        queue.append(dep)
        
        return list(impact_set)


class AISceneGenerator:
    """AI场景生成器 - 基于业务逻辑自动生成测试场景"""
    
    # 业务场景定义
    BUSINESS_SCENES = {
        "新手流程": {
            "apis": ["auth_register", "auth_login", "player_create", "resource_init"],
            "priority": 1
        },
        "核心玩法_抽卡": {
            "apis": ["gacha_heroes", "gacha_draw", "gacha_records"],
            "priority": 2
        },
        "核心玩法_战斗": {
            "apis": ["battle_start", "battle_result"],
            "priority": 2
        },
        "资源循环": {
            "apis": ["resource_produce", "resource_my", "city_collect"],
            "priority": 3
        },
        "社交_联盟": {
            "apis": ["alliance_create", "alliance_join", "alliance_list", "alliance_my"],
            "priority": 4
        },
        "日常任务": {
            "apis": ["quest_list", "quest_claim", "achievement_list", "signin_status", "signin_do"],
            "priority": 3
        },
        "成长系统": {
            "apis": ["growth_info", "growth_level_up", "growth_star_up"],
            "priority": 3
        },
        "PVP_排行榜": {
            "apis": ["rank_list", "season_current", "season_ranking"],
            "priority": 5
        },
        "商店消费": {
            "apis": ["shop_list", "shop_buy"],
            "priority": 4
        },
        "探索_地图": {
            "apis": ["map_zones", "map_move", "map_init"],
            "priority": 3
        },
    }
    
    @classmethod
    def generate_test_order(cls, api_list: List[str]) -> List[str]:
        """根据业务优先级排序"""
        # 按场景分组
        scene_apis = defaultdict(list)
        
        for api in api_list:
            api_lower = api.lower()
            for scene_name, scene_info in cls.BUSINESS_SCENES.items():
                if any(kw in api_lower for kw in scene_info["apis"]):
                    scene_apis[scene_name].append(api)
                    break
        
        # 按优先级排序
        ordered = []
        for scene_name in sorted(scene_apis.keys(), 
                key=lambda x: cls.BUSINESS_SCENES[x]["priority"]):
            ordered.extend(scene_apis[scene_name])
        
        # 添加未分类的
        classified = set(ordered)
        for api in api_list:
            if api not in classified:
                ordered.append(api)
        
        return ordered
    
    @classmethod
    def generate_scenario_from_description(cls, description: str, all_apis: Dict) -> List[str]:
        """根据描述生成场景"""
        description = description.lower()
        matched_apis = []
        
        keywords = {
            "抽卡": ["gacha", "draw", "heroes"],
            "战斗": ["battle", "fight", "war"],
            "资源": ["resource", "gold", "produce"],
            "联盟": ["alliance", "guild"],
            "商店": ["shop", "buy"],
            "任务": ["quest", "achievement"],
            "签到": ["signin"],
            "成长": ["growth", "level", "star"],
            "排名": ["rank"],
        }
        
        for api_key, api in all_apis.items():
            api_lower = api_key.lower()
            for category, kws in keywords.items():
                if any(kw in api_lower for kw in kws):
                    if category in description:
                        matched_apis.append(api_key)
                        break
        
        return cls.generate_test_order(matched_apis)


def main():
    """主函数 - 演示"""
    project_path = "/root/.openclaw/workspace/fengshen-game"
    
    print("=" * 60)
    print("🤖 AI API 测试引擎 - 封神榜")
    print("=" * 60)
    
    # 1. 分析API
    analyzer = APIAnalyzer(project_path)
    apis = analyzer.scan_all_apis()
    print(f"\n📊 扫描到 {len(apis)} 个API端点")
    
    # 2. 构建依赖图
    deps = analyzer.build_dependency_graph(apis)
    print(f"🔗 构建了 {len(deps)} 个依赖关系")
    
    # 3. 增量检测
    tester = IncrementalTester(project_path)
    changes = tester.detect_changes(apis)
    
    print(f"\n📈 变更检测:")
    print(f"  ➕ 新增: {len(changes['added'])}")
    print(f"  ➖ 删除: {len(changes['removed'])}")
    print(f"  🔄 修改: {len(changes['modified'])}")
    
    # 保存当前状态
    api_data = {k: {"hash": tester.compute_hash(v.__dict__)} for k, v in apis.items()}
    tester._save_current({"apis": api_data})
    
    # 4. 生成测试场景
    print("\n🎮 业务场景测试链:")
    for scene, info in AISceneGenerator.BUSINESS_SCENES.items():
        apis_in_scene = [a for a in info["apis"] if a in apis]
        if apis_in_scene:
            print(f"  • {scene}: {len(apis_in_scene)} APIs")
    
    # 5. 根据描述生成场景
    description = "我想测试抽卡和战斗"
    scenario = AISceneGenerator.generate_scenario_from_description(description, apis)
    print(f"\n💡 场景: {description}")
    print(f"  生成的测试链: {' -> '.join(scenario[:5])}")
    
    return apis, deps


if __name__ == "__main__":
    main()
