// 战斗系统模块

function renderChapters() {
    var list = document.getElementById("chapter-list");
    list.innerHTML = GAME_DATA.chapters.map(function(c) {
        var passed = state.chapterId >= c.id;
        return '<div class="card"><div><div style="font-weight:bold">' + c.title + '</div><div style="font-size:9px;color:#888">第' + c.id + '章</div></div><button class="btn btn-small" onclick="startBattle(' + c.id + ')">' + (passed?"扫荡":"战斗") + '</button></div>';
    }).join("");
}

function startBattle(id) {
    if (state.selectedPartners.length === 0) { showModal("请先选择武将"); return; }
    var ch = GAME_DATA.chapters.find(function(c){return c.id===id});
    
    var playerPower = state.selectedPartners.reduce(function(s, i) {
        var p = state.partners[i];
        return s + (p.atk||0) + (p.practice&&p.practice.atk||0);
    }, 0);
    
    var enemyPower = ch.enemies.reduce(function(s, e){return s + e.atk}, 0);
    
    if (playerPower >= enemyPower * 0.5) {
        state.lingqi += ch.reward.lingqi;
        state.gold += ch.reward.gold;
        if (id > state.chapterId) state.chapterId = id;
        save(); renderAll();
        showModal("🎉 战斗胜利！\n\n获得 " + ch.reward.lingqi + "灵气 " + ch.reward.gold + "金币", "战斗结果");
    } else {
        showModal("💀 战斗力不足，建议升级武将", "战斗结果");
    }
}
