// 六维属性+缘分+技能系统
(function() {
    console.log("加载六维属性系统...");
    
    // 添加六维属性到武将数据 (智力替换防御)
    var partners = [
        {id:1,politics:30,military:95,lead:90,charm:70,intelligence:85},
        {id:2,politics:40,military:90,lead:85,charm:60,intelligence:80},
        {id:3,politics:20,military:80,lead:70,charm:50,intelligence:75},
        {id:4,politics:98,military:60,lead:95,charm:90,intelligence:95},
        {id:5,politics:35,military:75,lead:65,charm:55,intelligence:70},
        {id:6,politics:30,military:72,lead:63,charm:52,intelligence:68},
        {id:7,politics:75,military:65,lead:80,charm:70,intelligence:65},
        {id:8,politics:40,military:85,lead:90,charm:95,intelligence:60},
        {id:9,politics:60,military:30,lead:40,charm:99,intelligence:90},
        {id:10,politics:50,military:78,lead:75,charm:45,intelligence:85},
        {id:11,politics:55,military:82,lead:78,charm:50,intelligence:80},
        {id:12,politics:65,military:88,lead:92,charm:60,intelligence:75}
    ];
    
    partners.forEach(function(p) {
        var gp = GAME_DATA.partners.find(function(x) { return x.id === p.id; });
        if (gp) {
            gp.politics = p.politics;
            gp.military = p.military;
            gp.lead = p.lead;
            gp.charm = p.charm;
            gp.intelligence = p.intelligence;
        }
    });
    
    // 缘分数据
    var YUANFEN_DATA = [
        {name: "阐教弟子", partners: [2,5,6], bonus: 0.15},
        {name: "截教弟子", partners: [10,11], bonus: 0.15},
        {name: "封神大业", partners: [1,2,4], bonus: 0.20},
        {name: "托塔天王", partners: [7,5,6], bonus: 0.10}
    ];
    
    // 技能数据
    var SKILL_DATA = {
        1: {name: "火尖枪", desc: "攻击+20%", max: 3},
        2: {name: "哮天犬", desc: "追击+15%", max: 3},
        3: {name: "风雷棍", desc: "速度+10", max: 3},
        4: {name: "打神鞭", desc: "对BOSS伤害+20%", max: 3},
        5: {name: "遁龙桩", desc: "控制+10%", max: 3},
        6: {name: "混天绫", desc: "闪避+10%", max: 3},
        7: {name: "玲珑宝塔", desc: "援护+15%", max: 3},
        8: {name: "天子剑", desc: "全体攻击+15%", max: 3},
        9: {name: "魅惑", desc: "敌人攻-10%", max: 3},
        10: {name: "开天珠", desc: "群体伤害+15%", max: 3},
        11: {name: "定海珠", desc: "攻击+15%", max: 3},
        12: {name: "雌雄双鞭", desc: "双重打击+15%", max: 3}
    };
    
    // 可兑换技能
    var EXCHANGE_SKILLS = [
        {id: 101, name: "兵种相克", desc: "克制伤害+10%", needPartners: [1,2], cost: 200},
        {id: 102, name: "鼓舞", desc: "全队攻击+10%", needPartners: [2,4], cost: 300},
        {id: 103, name: "护盾", desc: "全队防御+10%", needPartners: [4,7], cost: 300},
        {id: 104, name: "追击", desc: "残血伤害+20%", needPartners: [1,3], cost: 250}
    ];
    
    // 获取激活缘分
    function getActiveYuanfen() {
        if (!state || !state.partners) return [];
        var pids = state.partners.map(function(p) { return p.id; });
        return YUANFEN_DATA.filter(function(y) {
            return y.partners.every(function(pid) { return pids.includes(pid); });
        });
    }
    
    // 保存原始函数引用
    var _renderPartners = renderPartners;
    var _togglePartner = togglePartner;
    
    // 初始化武将技能
    function initPartnerSkills() {
        if (!state || !state.partners) return;
        state.partners.forEach(function(p) {
            if (!p.skills || p.skills.length === 0) {
                var sk = SKILL_DATA[p.id];
                if (sk) {
                    p.skills = [{id: p.id, name: sk.name, level: 1}];
                }
            }
            if (p.duplicates === undefined) p.duplicates = 0;
        });
    }
    
    // 获取可解锁技能数
    function getMaxSkills(level) {
        if (level >= 10) return 3;
        if (level >= 5) return 2;
        return 1;
    }
    
    // 覆盖renderPartners
    renderPartners = function() {
        var list = document.getElementById("partners-list");
        
        // 如果没有伙伴或列表不存在，调用原始函数
        if (!list || !state || !state.partners || state.partners.length === 0) {
            if (_renderPartners) {
                _renderPartners();
            } else {
                // 手动渲染空状态
                if (list) {
                    list.innerHTML = '<div style="grid-column:span2;text-align:center;padding:40px;color:#888">暂无武将<br><button class="btn" style="margin-top:10px" onclick="doGacha()">抽卡 (100灵气)</button></div>';
                }
            }
            return;
        }
        
        initPartnerSkills();
        var yf = getActiveYuanfen();
        
        list.innerHTML = state.partners.map(function(p, i) {
            var sel = state.selected.indexOf(i) >= 0 ? "selected" : "";
            var pdata = GAME_DATA.partners.find(function(x) { return p.id === x.id; });
            var yfNames = yf.filter(function(y) { return y.partners.includes(p.id); }).map(function(y) { return y.name; }).join(", ");
            
            var maxSkills = getMaxSkills(p.level);
            var skillHtml = "";
            if (p.skills && p.skills.length > 0) {
                skillHtml = p.skills.map(function(sk) {
                    return '<span class="skill-tag">' + sk.name + (sk.level > 1 ? " Lv" + sk.level : "") + '</span>';
                }).join("");
            }
            
            // 达到等级可以学习新技能
            if (p.skills && p.skills.length < maxSkills) {
                var needLevel = p.level >= 10 ? 10 : (p.level >= 5 ? 5 : 1);
                if (p.level >= needLevel) {
                    skillHtml += '<button class="btn btn-small btn-purple" style="margin:2px;padding:2px 4px;font-size:8px" onclick="event.stopPropagation();learnSkill(' + i + ')">+学技能</button>';
                }
            }
            
            var html = '<div class="partner-card ' + sel + '" onclick="togglePartner(' + i + ')">';
            html += '<div class="partner-header"><div class="partner-avatar">' + p.icon + '</div>';
            html += '<div><div style="font-weight:bold;font-size:11px">' + p.name + '</div>';
            html += '<div style="font-size:8px;color:#888">' + (pdata ? pdata.type : "步") + ' | Lv.' + p.level + ' | ⭐' + p.star + '</div></div></div>';
            html += '<div class="attr-bar"><span class="attr-item">⚔' + p.atk + '</span><span class="attr-item">🧠' + (pdata ? pdata.intelligence : 70) + '</span><span class="attr-item">⚡' + p.speed + '</span></div>';
            html += '<div class="attr-bar" style="margin-top:2px">';
            html += '<span class="attr-item" style="background:rgba(255,200,0,0.2)">政' + (pdata ? pdata.politics : 30) + '</span>';
            html += '<span class="attr-item" style="background:rgba(255,100,0,0.2)">军' + (pdata ? pdata.military : 60) + '</span>';
            html += '<span class="attr-item" style="background:rgba(100,100,255,0.2)">统' + (pdata ? pdata.lead : 60) + '</span>';
            html += '<span class="attr-item" style="background:rgba(255,100,200,0.2)">魅' + (pdata ? pdata.charm : 40) + '</span>';
            html += '</div>';
            html += '<div style="margin-top:4px;font-size:9px">' + skillHtml + '</div>';
            if (yfNames) html += '<div style="font-size:8px;color:#ffd700;margin-top:4px">' + yfNames + '</div>';
            html += '</div>';
            return html;
        }).join('') + '<div style="grid-column:span2;text-align:center;padding:10px"><button class="btn" onclick="doGacha()">抽卡 (100灵气)</button></div>';
        
        // 缘分激活提示
        if (yf.length > 0) {
            var yfDiv = document.createElement("div");
            yfDiv.style.cssText = "grid-column:span2;margin-top:10px;padding:8px;background:rgba(255,215,0,0.1);border:1px solid #ffd700;border-radius:6px;text-align:center";
            yfDiv.innerHTML = '<div style="color:#ffd700;font-size:10px;font-weight:bold">缘分激活</div>' + yf.map(function(y) { return '<div style="font-size:9px">' + y.name + ': +' + (y.bonus * 100) + '%</div>'; }).join('');
            list.appendChild(yfDiv);
        }
        
        // 可兑换技能
        var exchangeHtml = "";
        EXCHANGE_SKILLS.forEach(function(es) {
            var pids = state.partners.map(function(p) { return p.id; });
            var hasAll = es.needPartners.every(function(pid) { return pids.includes(pid); });
            if (hasAll) {
                exchangeHtml += '<div class="card" style="margin-top:6px"><div><div style="font-weight:bold;font-size:10px">' + es.name + '</div><div style="font-size:8px;color:#888">' + es.desc + '</div></div><button class="btn btn-small btn-green" onclick="event.stopPropagation();exchangeSkill(' + es.id + ')">' + es.cost + '💎</button></div>';
            }
        });
        if (exchangeHtml) {
            var exDiv = document.createElement("div");
            exDiv.style.cssText = "grid-column:span2;margin-top:10px;padding:8px;background:rgba(0,255,0,0.1);border-radius:6px";
            exDiv.innerHTML = '<div style="color:#0f0;font-size:10px;font-weight:bold;margin-bottom:4px">可兑换组合技能</div>' + exchangeHtml;
            list.appendChild(exDiv);
        }
    };
    
    // 学习技能
    window.learnSkill = function(idx) {
        if (!state || !state.partners) return;
        var p = state.partners[idx];
        if (!p) return;
        
        var maxSkills = getMaxSkills(p.level);
        if (p.skills && p.skills.length >= maxSkills) {
            if (typeof showToast === 'function') showToast("等级不足");
            return;
        }
        
        var sk = SKILL_DATA[p.id];
        if (!sk) return;
        
        var newSk = {id: p.id + 100, name: sk.name + "·强", level: 1};
        if (!p.skills) p.skills = [];
        p.skills.push(newSk);
        
        save();
        renderPartners();
        if (typeof showToast === 'function') showToast("学会: " + newSk.name);
    };
    
    // 兑换技能
    window.exchangeSkill = function(skillId) {
        var es = EXCHANGE_SKILLS.find(function(x) { return x.id === skillId; });
        if (!es) return;
        
        if (!state || !state.player || state.player.lingqi < es.cost) {
            if (typeof showToast === 'function') showToast("灵气不足");
            return;
        }
        
        state.player.lingqi -= es.cost;
        if (!state.learnedSkills) state.learnedSkills = [];
        state.learnedSkills.push({id: es.id, name: es.name});
        
        save();
        renderAll();
        renderPartners();
        if (typeof showToast === 'function') showToast("学会: " + es.name);
    };
    
    // 修改战力计算
    var _calcZhanli = calcZhanli;
    calcZhanli = function() {
        if (!state || !state.partners || state.partners.length === 0) {
            return _calcZhanli ? _calcZhanli() : 0;
        }
        
        var yf = getActiveYuanfen();
        var bonus = 1 + yf.reduce(function(s, y) { return s + y.bonus; }, 0);
        
        var skillBonus = 0;
        state.partners.forEach(function(p) {
            if (p.skills) {
                p.skills.forEach(function(sk) {
                    skillBonus += (sk.level - 1) * 0.1;
                });
            }
        });
        
        return state.partners.reduce(function(s, p) {
            return s + Math.floor((p.hp + p.atk * 2 + p.def) * bonus * (1 + skillBonus * 0.1));
        }, 0);
    };
    
    // 修改选择限制为3
    togglePartner = function(i) {
        if (!state || !state.selected) {
            if (_togglePartner) _togglePartner(i);
            return;
        }
        
        var idx = state.selected.indexOf(i);
        if (idx >= 0) {
            state.selected.splice(idx, 1);
        } else if (state.selected.length >= 3) {
            if (typeof showToast === 'function') showToast("最多上阵3个");
            return;
        } else {
            state.selected.push(i);
        }
        save();
        renderPartners();
    };
    
    console.log("六维属性系统加载完成");
})();
