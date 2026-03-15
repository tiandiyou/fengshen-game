// 内政系统
var BUILD_DATA=[
{id:1,name:"民居",level:1,maxLevel:10,base:10},
{id:2,name:"灵田",level:1,maxLevel:10,base:10},
{id:3,name:"粮仓",level:1,maxLevel:10,base:10},
{id:4,name:"林场",level:1,maxLevel:10,base:10}
];

function renderBuildings(){
    var list=document.getElementById("build-list");
    if(!list)return;
    var gold=0,lingqi=0,food=0,wood=0;
    BUILD_DATA.forEach(function(b){
        var my=state.buildings.find(function(x){return x.id===b.id})||{level:1};
        var out=Math.floor(b.base*my.level);
        if(b.id===1)gold=out;
        if(b.id===2)lingqi=out;
        if(b.id===3)food=out;
        if(b.id===4)wood=out;
    });
    list.innerHTML=BUILD_DATA.map(function(b){
        var my=state.buildings.find(function(x){return x.id===b.id})||{level:1};
        var cost=Math.floor(b.base*Math.pow(1.5,my.level));
        return '<div class="card"><div><div style="font-weight:bold">'+b.name+' Lv.'+my.level+'</div><div style="font-size:9px;color:#888">产出 '+Math.floor(b.base*my.level)+'</div></div><button class="btn btn-small" onclick="upgradeBuild('+b.id+')">'+cost+'💰</button></div>';
    }).join('')+'<div style="margin-top:10px;padding:10px;background:rgba(0,0,0,0.3);border-radius:6px"><div style="font-size:10px">💰'+gold+' 💎'+lingqi+' 🌾'+food+' 🪵'+wood+' /小时</div></div>';
}

function upgradeBuild(id){
    var b=BUILD_DATA.find(function(x){return x.id===id});
    var my=state.buildings.find(function(x){return x.id===id});
    if(!my){my={id:id,level:1};state.buildings.push(my);}
    var cost=Math.floor(b.base*Math.pow(1.5,my.level));
    if(state.player.gold<cost){showToast('金币不足');return;}
    state.player.gold-=cost;
    my.level++;
    save();renderBuildings();renderAll();showToast(b.name+' -> Lv.'+my.level);
}

if(!state.buildings||state.buildings.length===0){
    state.buildings=BUILD_DATA.map(function(b){return{id:b.id,level:1}});
}
