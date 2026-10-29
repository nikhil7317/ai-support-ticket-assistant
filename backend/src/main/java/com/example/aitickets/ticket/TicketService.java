package com.example.aitickets.ticket;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class TicketService {
  private final TicketRepository repo;
  public TicketService(TicketRepository repo){this.repo=repo;}
  public List<Ticket> all(){return repo.findAll();}
  public Ticket one(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Ticket not found"));}
  public Ticket create(TicketRequest r){Ticket t=new Ticket(); t.setTitle(r.title().trim()); t.setDescription(r.description().trim()); t.setCategory(r.category().trim()); t.setStatus(r.status()==null?TicketStatus.OPEN:r.status()); return repo.save(t);}
  public Ticket update(Long id,TicketRequest r){Ticket t=one(id); t.setTitle(r.title().trim());t.setDescription(r.description().trim());t.setCategory(r.category().trim());if(r.status()!=null)t.setStatus(r.status());return repo.save(t);}
  public void delete(Long id){repo.delete(one(id));}
}
