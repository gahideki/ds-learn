package com.devsuperior.bds02.controller;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.exception.EntityNotFoundException;
import com.devsuperior.bds02.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public Page<EventDTO> findAll(Pageable pageable) {
        return eventService.findAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        try {
            return ResponseEntity.ok(eventService.updateEvent(eventDTO, id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO eventDTO) {
        EventDTO event = eventService.insert(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

}
