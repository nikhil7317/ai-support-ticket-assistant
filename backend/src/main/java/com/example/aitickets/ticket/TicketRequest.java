package com.example.aitickets.ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record TicketRequest(@NotBlank @Size(max=120) String title,
 @NotBlank @Size(max=4000) String description, @NotBlank @Size(max=80) String category,
 TicketStatus status) {}
