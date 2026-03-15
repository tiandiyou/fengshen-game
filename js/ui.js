// UI渲染模块

function showScreen(id) {
    document.querySelectorAll(".screen").forEach(function(x){x.classList.remove("active")});
    document.getElementById(id).classList.add("active");
}

function showTab(tab) {
    document.querySelectorAll(".nav-item").forEach(function(x){x.classList.remove("active")});
    event.target.closest(".nav-item").classList.add("active");
    document.querySelectorAll(".tab-content").forEach(function(x){x.classList.remove("active")});
    document.getElementById("tab-"+tab).classList.add("active");
    
    if(tab==="partners")renderPartners();
    else if(tab==="battle")renderChapters();
    else if(tab==="practice")renderPractice();
    else if(tab==="build")renderBuildings();
}

function showToast(msg) {
    var t = document.createElement("div");
    t.className = "toast";
    t.textContent = msg;
    document.body.appendChild(t);
    setTimeout(function(){t.remove()},2000);
}

function showModal(txt, tit) {
    document.getElementById("modal-title").textContent = tit || "提示";
    document.getElementById("modal-text").textContent = txt;
    document.getElementById("modal").classList.add("active");
}

function closeModal() {
    document.getElementById("modal").classList.remove("active");
}

function renderAll() {
    document.getElementById("lingqi").textContent = state.lingqi;
    document.getElementById("gold").textContent = state.gold;
    document.getElementById("food").textContent = state.food;
    document.getElementById("wood").textContent = state.wood;
    document.getElementById("zhanli").textContent = calcZhanli();
}
