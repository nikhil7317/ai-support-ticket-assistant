const BASE=import.meta.env.VITE_API_BASE_URL||"http://localhost:8080";
async function req(path,options={}) {
 const res=await fetch(BASE+path,{...options,headers:{"Content-Type":"application/json",...(options.headers||{})}});
 if(!res.ok){let msg=`Request failed (${res.status})`;try{const b=await res.json();msg=b.error||b.message||msg;}catch{}throw new Error(msg);}
 if(res.status===204)return null; return res.json();
}
export const getTickets=()=>req("/api/tickets");
export const createTicket=t=>req("/api/tickets",{method:"POST",body:JSON.stringify(t)});
export const updateTicket=(id,t)=>req(`/api/tickets/${id}`,{method:"PUT",body:JSON.stringify(t)});
export const deleteTicket=id=>req(`/api/tickets/${id}`,{method:"DELETE"});
export const analyzeTicket=description=>req("/api/ai/analyze",{method:"POST",body:JSON.stringify({description})});
export const askAi=question=>req("/api/ai/chat",{method:"POST",body:JSON.stringify({question})});
