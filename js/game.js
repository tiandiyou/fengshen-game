// 封神榜 - 游戏入口

// 游戏状态
var state = {
    name: "",
    lingqi: 200,
    gold: 100,
    food: 100,
    wood: 100,
    partners: [],
    selectedPartners: [],
    buildings: [],
    battleCount: 0,
    chapterId: 0
};

// 战力计算
function calcZhanli() {
    return state.partners.reduce(function(s, p) {
        var hp = (p.hp||0) + (p.practice&&p.practice.hp||0);
        var atk = (p.atk||0) + (p.practice&&p.practice.atk||0);
        var def = (p.def||0) + (p.practice&&p.practice.def||0);
        return s + hp + atk*2 + def;
    }, 0);
}

// 数据持久化
function save() {
    localStorage.setItem("fengshen_save", JSON.stringify(state));
}

function load() {
    var s = localStorage.getItem("fengshen_save");
    if (s) {
        state = JSON.parse(s);
        return true;
    }
    return false;
}

// 创建角色
function createCharacter() {
    var name = document.getElementById("player-name").value.trim() || "阐教弟子";
    state.name = name;
    state.partners = [{
        name: "哪吒", icon: "🔥", quality: "red", type: "骑",
        hp: 1200, atk: 180, def: 80, speed: 95,
        politics: 30, military: 95, lead: 90, charm: 70, intelligence: 85,
        skills: ["火尖枪"], level: 1, star: 0, practice: {}
    }];
    state.selectedPartners = [0];
    state.buildings = BUILD_DATA.map(function(b){return{id:b.id,level:1}});
    save();
    showScreen("main-screen");
    renderAll();
    renderPartners();
    showToast("欢迎 " + name + "！获得哪吒！");
}

// 初始化
function init() {
    if (load()) {
        // 确保buildings存在
        if (!state.buildings || state.buildings.length === 0) {
            state.buildings = BUILD_DATA.map(function(b){return{id:b.id,level:1}});
        }
        showScreen("main-screen");
        renderAll();
    }
}

// 启动
init();
