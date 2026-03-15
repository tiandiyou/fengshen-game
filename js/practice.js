// 修炼系统模块

function renderPractice() {
    var list = document.getElementById("practice-list");
    if (!list || state.partners.length === 0) {
        if(list) list.innerHTML = "<div style='text-align:center;padding:20px;color:#888'>暂无武将</div>";
        return;
    }
    
    list.innerHTML = state.partners.map(function(p, i) {
        var phtml = '<div class="card" style="flex-direction:column;align-items:stretch;margin-bottom:8px">';
        phtml += '<div style="display:flex;align-items:center;gap:8px;margin-bottom:8px"><div class="partner-avatar">' + p.icon + '</div><div><div style="font-weight:bold">' + p.name + '</div><div style="font-size:8px;color:#888">Lv.' + (p.level||1) + '</div></div></div>';
        phtml += '<div style="display:flex;flex-wrap:wrap;gap:4px">';
        
        PRACTICE_DATA.forEach(function(pm) {
            var val = (p.practice && p.practice[pm.attr]) || 0;
            var totalAttr = (p[pm.attr]||0) + val;
            phtml += '<div style="display:flex;align-items:center;gap:4px;background:rgba(255,255,255,0.1);padding:4px 8px;border-radius:4px;font-size:9px">';
            phtml += '<span>' + pm.icon + '</span><span>' + totalAttr + '</span>';
            phtml += '<button class="btn btn-small" style="padding:2px 6px;font-size:8px" onclick="practiceAttr(' + i + ',\'' + pm.attr + '\')">+' + pm.bonus + ' 💎' + pm.cost + '</button>';
            phtml += '</div>';
        });
        
        phtml += '</div></div>';
        return phtml;
    }).join("");
}

function practiceAttr(idx, attr) {
    var p = state.partners[idx];
    var pm = PRACTICE_DATA.find(function(x){return x.attr===attr});
    if (!p || !pm) return;
    if (state.lingqi < pm.cost) { showToast("灵气不足"); return; }
    
    state.lingqi -= pm.cost;
    if (!p.practice) p.practice = {};
    p.practice[attr] = (p.practice[attr]||0) + pm.bonus;
    
    save(); renderPractice(); renderAll();
    showToast(p.name + " " + pm.name + " +" + pm.bonus);
}
