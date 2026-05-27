// Simple client-side task controller implementing graph/toposort and mergesort
class Task {
  constructor(id,name,priority,deadline){this.id=id;this.name=name;this.priority=priority;this.deadline=deadline;this.done=false}
}

const Controller = (()=>{
  const tasks = new Map(); // id -> Task
  const adj = new Map(); // id -> set of dependents
  const indeg = new Map();

  function addTask(id,name,pr,dl){
    if(tasks.has(id)) return `Task ${id} already exists.`;
    const t=new Task(id,name,pr,dl);
    tasks.set(id,t); if(!adj.has(id)) adj.set(id,new Set());
    if(!indeg.has(id)) indeg.set(id,0);
    return `Added ${id}: ${name}`;
  }

  function addDependency(from,to){
    if(!tasks.has(from) || !tasks.has(to)) return 'Invalid ids';
    adj.get(from).add(to);
    indeg.set(to,(indeg.get(to)||0)+1);
    return `Dependency ${from} -> ${to}`;
  }

  function markDone(id){
    const t = tasks.get(id);
    if(!t) return 'Not found';
    if(t.done) return `Task ${id} already done`;
    t.done = true;
    // reduce indegree of dependents
    const set = adj.get(id) || new Set();
    for(const d of set) indeg.set(d, Math.max(0,(indeg.get(d)||1)-1));
    return `Marked ${id} done`;
  }

  function getUnlocked(){
    const res=[];
    for(const [id,t] of tasks){ if(t.done) continue; if((indeg.get(id)||0)===0) res.push(t); }
    return res;
  }

  function allDone(){
    if(tasks.size===0) return false;
    for(const t of tasks.values()) if(!t.done) return false;
    return true;
  }

  // topological sort (Kahn) ignoring done nodes
  function topoSort(){
    const inDegCopy = new Map(indeg);
    const q=[];
    for(const [id,t] of tasks) if(!t.done && (inDegCopy.get(id)||0)===0) q.push(id);
    const out=[];
    while(q.length){ const id=q.shift(); out.push(tasks.get(id)); for(const nb of (adj.get(id)||[])){ inDegCopy.set(nb,(inDegCopy.get(nb)||0)-1); if(inDegCopy.get(nb)===0 && !tasks.get(nb).done) q.push(nb); }}
    return out;
  }

  // stable merge sort by deadline
  function mergeSort(arr){ if(arr.length<=1) return arr; const mid=Math.floor(arr.length/2); const L=mergeSort(arr.slice(0,mid)); const R=mergeSort(arr.slice(mid)); const res=[]; let i=0,j=0; while(i<L.length && j<R.length){ if(L[i].deadline<=R[j].deadline) res.push(L[i++]); else res.push(R[j++]); } while(i<L.length) res.push(L[i++]); while(j<R.length) res.push(R[j++]); return res; }

  function getMaster(){ const topo = topoSort(); return mergeSort(topo); }

  function showAll(){ let s=''; for(const [id,t] of tasks) s+=`${id}: ${t.name} Pr:${t.priority} DL:${t.deadline} Done:${t.done}\n`; return s; }

  function reset(){ tasks.clear(); adj.clear(); indeg.clear(); }

  function toJSON(){ const tasksArr=[]; for(const [id,t] of tasks) tasksArr.push({id:t.id,name:t.name,priority:t.priority,deadline:t.deadline,done:t.done}); const edges=[]; for(const [k,set] of adj) for(const v of set) edges.push([k,v]); return JSON.stringify({tasks:tasksArr,edges},null,2); }
  function loadFrom(obj){ reset(); if(obj.tasks) for(const t of obj.tasks) { addTask(t.id,t.name,t.priority,t.deadline); if(t.done) tasks.get(t.id).done=true; } if(obj.edges) for(const e of obj.edges) addDependency(e[0],e[1]); }

  return {addTask,addDependency,markDone,getUnlocked, getMaster, showAll, reset, toJSON, loadFrom, allDone};
})();

// UI wiring
const $ = id => document.getElementById(id);
const consoleOut = txt=>{ const el=$('console'); el.textContent += txt+"\n"; el.scrollTop = el.scrollHeight; };

let selectedUnlockedId = null;

function renderStatus(){ const status=$('status-text'); if(Controller.allDone()){ status.textContent = 'Project completed — all tasks are done.'; $('btn-mark').disabled = true; return; }
  const unlocked = Controller.getUnlocked();
  if(unlocked.length === 0){ status.textContent = 'No unlocked tasks yet. Complete prerequisites or add a task.'; $('btn-mark').disabled = true; }
  else { status.textContent = `Unlocked tasks: ${unlocked.length}. Select one then click Mark Selected Done.`; $('btn-mark').disabled = selectedUnlockedId === null; }
}

function isUnlocked(t){ return !t.done && (Controller.getUnlocked().find(u=>u.id===t.id) != null); }

function renderMaster(){ const tbody=$('master-table').querySelector('tbody'); tbody.innerHTML=''; const arr = Controller.getMaster(); for(const t of arr){ const tr=document.createElement('tr'); if(t.done) tr.classList.add('done');
    const unlocked = Controller.getUnlocked().some(u => u.id===t.id);
    const statusLabel = t.done ? 'Done' : unlocked ? 'Ready' : 'Locked';
    const actionTd = document.createElement('td');
    const btn = document.createElement('button');
    if(t.done){ btn.textContent='Done'; btn.disabled=true; }
    else if(unlocked){ btn.textContent='Mark Done'; btn.addEventListener('click',()=>{ consoleOut(Controller.markDone(t.id)); selectedUnlockedId = null; renderMaster(); renderUnlocked(); renderStatus(); }); }
    else { btn.textContent='Locked'; btn.disabled=true; }
    actionTd.appendChild(btn);
    tr.innerHTML = `<td>${t.id}</td><td>${t.name}</td><td>${t.priority}</td><td>${t.deadline}</td><td>${statusLabel}</td>`;
    tr.appendChild(actionTd);
    tbody.appendChild(tr);
  } renderStatus(); }
function renderUnlocked(){ const ul=$('unlocked-list'); ul.innerHTML=''; selectedUnlockedId = null; const list=Controller.getUnlocked(); for(const t of list){ const li=document.createElement('li'); li.dataset.id = t.id; li.textContent = `${t.id}: ${t.name} (Pr:${t.priority} DL:${t.deadline})`; li.addEventListener('click',()=>{ selectedUnlockedId = t.id; Array.from(ul.children).forEach(node=>node.classList.toggle('selected', node === li)); renderStatus(); }); li.addEventListener('dblclick',()=>{ Controller.markDone(t.id); consoleOut(`Marked ${t.id} done`); selectedUnlockedId = null; renderMaster(); renderUnlocked(); renderStatus(); }); ul.appendChild(li); } renderStatus(); }

// buttons
$('btn-add').addEventListener('click',()=>{
  const id=Number($('inp-id').value), name=$('inp-name').value||"(no name)", pr=Number($('inp-pr').value)||0, dl=Number($('inp-dl').value)||0;
  consoleOut(Controller.addTask(id,name,pr,dl)); renderMaster(); renderUnlocked();
});
$('btn-dep').addEventListener('click',()=>{ const f=Number($('inp-from').value), t=Number($('inp-to').value); consoleOut(Controller.addDependency(f,t)); renderMaster(); renderUnlocked(); });
$('btn-unlocked').addEventListener('click',()=>{ renderUnlocked(); consoleOut('Unlocked tasks shown'); });
$('btn-master').addEventListener('click',()=>{ renderMaster(); consoleOut('Master schedule shown'); });
$('btn-mark').addEventListener('click',()=>{ if(selectedUnlockedId===null){ consoleOut('Select an unlocked task first'); return; } consoleOut(Controller.markDone(selectedUnlockedId)); selectedUnlockedId = null; renderMaster(); renderUnlocked(); renderStatus(); });
$('btn-sample').addEventListener('click',()=>{ Controller.reset(); Controller.addTask(1,'Do Math Homework',1,2); Controller.addTask(2,'Read Chapter 4',3,5); Controller.addTask(3,'Write Essay Draft',2,7); Controller.addTask(4,'Submit Final Essay',1,10); Controller.addDependency(3,4); Controller.addDependency(2,3); consoleOut('Sample populated'); renderMaster(); renderUnlocked(); });
$('btn-save').addEventListener('click',()=>{ localStorage.setItem('sstm', Controller.toJSON()); consoleOut('Saved to localStorage'); });
$('btn-load').addEventListener('click',()=>{ const raw=localStorage.getItem('sstm'); if(!raw) return consoleOut('No saved data'); try{ const obj=JSON.parse(raw); Controller.loadFrom(obj); consoleOut('Loaded'); renderMaster(); renderUnlocked(); }catch(e){ consoleOut('Load failed'); } });

// initial
consoleOut('Web scheduler ready');
renderMaster(); renderUnlocked();
