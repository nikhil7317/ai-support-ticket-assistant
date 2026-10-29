package com.example.aitickets.ticket;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="tickets")
public class Ticket {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=120) private String title;
  @Column(nullable=false,length=4000) private String description;
  @Column(nullable=false,length=80) private String category;
  @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private TicketStatus status=TicketStatus.OPEN;
  @Column(nullable=false) private LocalDateTime createdAt;
  @PrePersist void onCreate(){ if(createdAt==null) createdAt=LocalDateTime.now(); if(status==null) status=TicketStatus.OPEN; }
  public Ticket() {}
  public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;}
  public String getDescription(){return description;} public void setDescription(String v){description=v;}
  public String getCategory(){return category;} public void setCategory(String v){category=v;}
  public TicketStatus getStatus(){return status;} public void setStatus(TicketStatus v){status=v;}
  public LocalDateTime getCreatedAt(){return createdAt;}
}
