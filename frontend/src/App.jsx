import {useEffect,useState} from "react";
import {getTickets,createTicket,updateTicket,deleteTicket,analyzeTicket,askAi} from "./api.js";
const blank={title:"",description:"",category:"Technical",status:"OPEN"};
export default function App(){
 const [tickets,setTickets]=useState([]),[form,setForm]=useState(blank),[error,setError]=useState("");
 const [busy,setBusy]=useState(false),[ai,setAi]=useState(""),[question,setQuestion]=useState(""),[answer,setAnswer]=useState(""),[chatBusy,setChatBusy]=useState(false);
 async function load(){try{setTickets(await getTickets());setError("");}catch(e){setError(e.message);}}
 useEffect(()=>{load();},[]);
 async function submit(e){e.preventDefault();try{await createTicket(form);setForm(blank);await load();}catch(e){setError(e.message);}}
 async function analyze(text){setBusy(true);setAi("");try{setAi((await analyzeTicket(text)).answer);}catch(e){setError(e.message);}finally{setBusy(false);}}
 async function status(t){const s=t.status==="OPEN"?"IN_PROGRESS":t.status==="IN_PROGRESS"?"RESOLVED":"OPEN";try{await updateTicket(t.id,{...t,status:s});await load();}catch(e){setError(e.message);}}
 async function remove(id){if(!confirm("Delete this ticket?"))return;try{await deleteTicket(id);await load();}catch(e){setError(e.message);}}
 async function chat(e){e.preventDefault();setChatBusy(true);setAnswer("");try{setAnswer((await askAi(question)).answer);}catch(e){setError(e.message);}finally{setChatBusy(false);}}
 const stat=s=>tickets.filter(t=>t.status===s).length;
 return <main className="wrap">
  <header><div><p className="kicker">FULL STACK · GENERATIVE AI</p><h1>AI Support Desk</h1><p className="sub">Manage IT tickets and get AI-powered troubleshooting suggestions.</p></div><button className="secondary" onClick={load}>Refresh</button></header>
  {error&&<div className="error">{error}</div>}
  <section className="stats"><Stat name="Total tickets" value={tickets.length}/><Stat name="Open" value={stat("OPEN")}/><Stat name="In progress" value={stat("IN_PROGRESS")}/><Stat name="Resolved" value={stat("RESOLVED")}/></section>
  <div className="columns">
   <section className="panel"><p className="kicker">TICKET MANAGEMENT</p><h2>Create ticket</h2>
    <form onSubmit={submit} className="form">
     <label>Title<input required maxLength="120" value={form.title} onChange={e=>setForm({...form,title:e.target.value})} placeholder="API returns HTTP 500"/></label>
     <label>Description<textarea required maxLength="4000" rows="5" value={form.description} onChange={e=>setForm({...form,description:e.target.value})} placeholder="Describe the issue and error message..."/></label>
     <label>Category<select value={form.category} onChange={e=>setForm({...form,category:e.target.value})}>{["Technical","Database","Network","Access","Other"].map(x=><option key={x}>{x}</option>)}</select></label>
     <button>Create ticket</button>
    </form>
    <div className="rule"/>
    <p className="kicker">AI ASSISTANT</p><h2>Analyze description</h2><p className="muted">Ask AI to summarize the current form description and suggest next steps.</p>
    <button className="secondary" disabled={busy||!form.description.trim()} onClick={()=>analyze(form.description)}>{busy?"Analyzing...":"Analyze with AI"}</button>
    {ai&&<Ai text={ai}/>}
   </section>
   <section className="panel"><div className="heading"><div><p className="kicker">YOUR WORKSPACE</p><h2>Support tickets</h2></div><span className="pill">{tickets.length} total</span></div>
    {!tickets.length?<p className="muted">No tickets yet. Create one to get started.</p>:<div className="ticket-list">{tickets.map(t=><article className="ticket" key={t.id}>
     <div className="heading"><span className="muted">Ticket #{t.id} · {t.category}</span><span className={"status "+t.status.toLowerCase()}>{t.status.replace("_"," ")}</span></div>
     <h3>{t.title}</h3><p className="description">{t.description}</p><p className="muted">{t.createdAt?new Date(t.createdAt).toLocaleString():""}</p>
     <div className="actions"><button className="small" disabled={busy} onClick={()=>analyze(t.description)}>✦ Analyze with AI</button><button className="small secondary" onClick={()=>status(t)}>Change status</button><button className="small delete" onClick={()=>remove(t.id)}>Delete</button></div>
    </article>)}</div>}
   </section>
  </div>
  <section className="panel chat"><p className="kicker">TECHNICAL QUESTION</p><h2>AI troubleshooting chat</h2><form onSubmit={chat} className="chat-form"><textarea required maxLength="4000" rows="3" value={question} onChange={e=>setQuestion(e.target.value)} placeholder="e.g. How can I investigate a slow Spring Boot API?"/><button disabled={chatBusy}>{chatBusy?"Thinking...":"Ask AI"}</button></form>{answer&&<Ai text={answer}/>}</section>
  <footer>Learning project · Verify AI suggestions before using them.</footer>
 </main>;
}
function Stat({name,value}){return <div className="stat"><span>{name}</span><strong>{value}</strong></div>}
function Ai({text}){return <div className="ai"><b>✦ AI RESPONSE</b><pre>{text}</pre></div>}
