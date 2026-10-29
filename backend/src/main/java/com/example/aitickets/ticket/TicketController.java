package com.example.aitickets.ticket;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/tickets")
public class TicketController {
  private final TicketService service;
  public TicketController(TicketService service){this.service=service;}
  @GetMapping public List<Ticket> all(){return service.all();}
  @GetMapping("/{id}") public Ticket one(@PathVariable Long id){return service.one(id);}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Ticket create(@Valid @RequestBody TicketRequest r){return service.create(r);}
  @PutMapping("/{id}") public Ticket update(@PathVariable Long id,@Valid @RequestBody TicketRequest r){return service.update(id,r);}
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
