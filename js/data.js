// 游戏静态数据
var GAME_DATA = {
    partners: [
        {id:1,name:"哪吒",icon:"🔥",quality:"red",type:"骑",hp:1200,atk:180,def:80,speed:95,politics:30,military:95,lead:90,charm:70,intelligence:85,skills:["火尖枪"]},
        {id:2,name:"杨戬",icon:"🐕",quality:"red",type:"步",hp:1100,atk:160,def:90,speed:90,politics:40,military:90,lead:85,charm:60,intelligence:80,skills:["八九玄功"]},
        {id:3,name:"雷震子",icon:"⚡",quality:"purple",type:"弓",hp:1000,atk:150,def:70,speed:100,politics:20,military:80,lead:70,charm:50,intelligence:75,skills:["风雷棍"]},
        {id:4,name:"姜子牙",icon:"📜",quality:"orange",type:"弓",hp:900,atk:140,def:60,speed:70,politics:98,military:60,lead:95,charm:90,intelligence:95,skills:["打神鞭"]},
        {id:5,name:"金吒",icon:"🗡️",quality:"purple",type:"步",hp:950,atk:145,def:85,speed:85,politics:35,military:75,lead:65,charm:55,intelligence:70,skills:["遁龙桩"]},
        {id:6,name:"木吒",icon:"🎋",quality:"purple",type:"步",hp:930,atk:140,def:80,speed:83,politics:30,military:72,lead:63,charm:52,intelligence:68,skills:["混天绫"]}
    ],
    chapters: [
        {id:1,title:"陈塘关",enemies:[{name:"巡海夜叉",hp:300,atk:30,icon:"👹"},{name:"敖丙",hp:800,atk:80,icon:"🐉"}],reward:{lingqi:100,gold:50}},
        {id:2,title:"渭水河",enemies:[{name:"渔夫",hp:400,atk:40,icon:"🎣"},{name:"樵夫",hp:450,atk:45,icon:"🪓"}],reward:{lingqi:150,gold:80}},
        {id:3,title:"西岐",enemies:[{name:"纣使",hp:500,atk:50,icon:"💂"},{name:"官兵",hp:600,atk:60,icon:"🛡️"}],reward:{lingqi:200,gold:100}},
        {id:4,title:"佳梦关",enemies:[{name:"魔礼青",hp:800,atk:90,icon:"🌪️"},{name:"魔礼红",hp:850,atk:95,icon:"🌊"}],reward:{lingqi:300,gold:150}},
        {id:5,title:"青龙关",enemies:[{name:"郑伦",hp:900,atk:100,icon:"🤺"},{name:"陈奇",hp:950,atk:105,icon:"⚔️"}],reward:{lingqi:350,gold:180}},
        {id:6,title:"汜水关",enemies:[{name:"余化",hp:1000,atk:120,icon:"🔪"},{name:"余元",hp:1200,atk:130,icon:"👹"}],reward:{lingqi:400,gold:200}}
    ]
};

var PRACTICE_DATA = [
    {attr:"atk",name:"武力",icon:"⚔",cost:30,bonus:10},
    {attr:"def",name:"防御",icon:"🛡",cost:30,bonus:8},
    {attr:"hp",name:"生命",icon:"♥",cost:20,bonus:50},
    {attr:"speed",name:"速度",icon:"⚡",cost:25,bonus:3},
    {attr:"intelligence",name:"智力",icon:"🧠",cost:25,bonus:5}
];

var BUILD_DATA = [
    {id:1,name:"民居",base:10},
    {id:2,name:"灵田",base:10},
    {id:3,name:"粮仓",base:10},
    {id:4,name:"林场",base:10}
];
