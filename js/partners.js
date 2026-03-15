// 武将系统模块

function renderPartners() {
    var list = document.getElementById("partners-list");
    if (state.partners.length === 0) {
        list.innerHTML = '<div style="grid-column:span2;text-align:center;padding:20px;color:#888">暂无武将<br><button class="btn" style="margin-top:10px" onclick="gacha()">抽卡</button></div>';
        return;
    }
    
    list.innerHTML = state.partners.map(function(p, i) {
        var sel = state.selectedPartners.indexOf(i) >= 0 ? "selected" : "";
        var pdata = GAME_DATA.partners.find(function(x){return x.name===p.name}) || {};
        var cost = Math.floor(50 * Math.pow(1.5, p.level || 1));
        
        var attrs = '<div class="attr-bar" style="margin-top:4px"><span class="attr-item">⚔' + (p.atk||0) + '</span><span class="attr-item">🧠' + (pdata.intelligence||70) + '</span><span class="attr-item">⚡' + (p.speed||0) + '</span></div>';
        attrs += '<div class="attr-bar"><span class="attr-item" style="background:rgba(255,200,0,0.2)">政' + (pdata.politics||30) + '</span><span class="attr-item" style="background:rgba(255,100,0,0.2)">军' + (pdata.military||60) + '</span><span class="attr-item" style="background:rgba(100,100,255,0.2)">统' + (pdata.lead||60) + '</span></div>';
        
        var skillHtml = p.skills ? '<div style="font-size:8px;margin-top:4px">' + p.skills.map(function(s){return'<span class="skill-tag">'+s+'</span>'}).join("") + '</div>' : '';
        
        return '<div class="partner-card ' + sel + '" onclick="togglePartner(' + i + ')">' +
            '<div class="partner-header"><div class="partner-avatar">' + p.icon + '</div>' +
            '<div><div style="font-weight:bold;font-size:11px">' + p.name + '</div>' +
            '<div style="font-size:8px;color:#888">' + (pdata.type||"步") + ' | Lv.' + (p.level||1) + ' | ⭐' + (p.star||0) + '</div></div></div>' +
            attrs + skillHtml +
            '<div style="margin-top:4px"><button class="btn btn-small" onclick="event.stopPropagation();upgradePartner('+i+')">↑' + cost + '</button></div></div>';
    }).join("") + '<div style="grid-column:span2;text-align:center;padding:10px"><button class="btn" onclick="gacha()">抽卡 (100灵)</button></div>';
}

function togglePartner(i) {
    var idx = state.selectedPartners.indexOf(i);
    if (idx >= 0) state.selectedPartners.splice(idx, 1);
    else if (state.selectedPartners.length < 3) state.selectedPartners.push(i);
    else { showToast("最多上阵3个"); return; }
    save(); renderPartners();
}

function upgradePartner(idx) {
    var p = state.partners[idx];
    var cost = Math.floor(50 * Math.pow(1.5, p.level || 1));
    if (state.lingqi < cost) { showToast("灵气不足"); return; }
    state.lingqi -= cost;
    p.level = (p.level||0) + 1;
    p.hp = Math.floor(p.hp * 1.1);
    p.atk = Math.floor(p.atk * 1.1);
    save(); renderAll(); renderPartners();
    showToast(p.name + " 升级到 Lv." + p.level);
}

function gacha() {
    if (state.lingqi < 100) { showModal("灵气不足，需要100"); return; }
    state.lingqi -= 100;
    var r = Math.random(), q;
    if (r < 0.03) q = "red";
    else if (r < 0.15) q = "orange";
    else if (r < 0.40) q = "purple";
    else q = "blue";
    
    var cand = GAME_DATA.partners.filter(function(p){
        if (q === "red") return p.quality === "red";
        if (q === "orange") return p.quality === "orange" || p.quality === "red";
        return true;
    });
    var p = cand[Math.floor(Math.random() * cand.length)];
    
    var np = {
        name: p.name, icon: p.icon, quality: p.quality, type: p.type,
        hp: p.hp, atk: p.atk, def: p.def, speed: p.speed,
        politics: p.politics, military: p.military, lead: p.lead, charm: p.charm, intelligence: p.intelligence,
        skills: [p.skills[0]], level: 1, star: 0, practice: {}
    };
    state.partners.push(np);
    if (state.selectedPartners.length < 3) state.selectedPartners.push(state.partners.length - 1);
    
    save(); renderAll(); renderPartners();
    var colors = {red:"#e74c3c",orange:"#e67e22",purple:"#9b59b6",blue:"#3498db"};
    showModal('<div style="font-size:30px">' + p.icon + '</div><div style="color:#ffd700;font-weight:bold">' + p.name + '</div><div style="color:' + colors[q] + '">' + q.toUpperCase() + '</div>', '抽卡结果');
}
