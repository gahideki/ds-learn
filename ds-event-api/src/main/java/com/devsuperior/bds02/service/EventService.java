package com.devsuperior.bds02.service;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.exception.EntityNotFoundException;
import com.devsuperior.bds02.repository.CityRepository;
import com.devsuperior.bds02.repository.EventRepository;
import com.devsuperior.bds02.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CityRepository cityRepository;

    public void deleteEvent(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format("Id %d not found", id));
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Integrity violation");
        }
    }

    public EventDTO updateEvent(EventDTO eventDTO, Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("City not found"));
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setUrl(eventDTO.getUrl());

        City city = cityRepository.findById(eventDTO.getCityId()).orElseThrow(() -> new EntityNotFoundException("City not found"));
        event.setCity(city);
        return new EventDTO(event);
    }

    public Page<EventDTO> findAll(Pageable pageable) {
        Page<Event> pageEvents = eventRepository.findAll(pageable);
        return pageEvents.map(EventDTO::new);
    }

    public EventDTO insert(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setUrl(eventDTO.getUrl());

        City city = cityRepository.findById(eventDTO.getCityId()).orElseThrow(() -> new ResourceNotFoundException("City not found"));
        event.setCity(city);

        event = eventRepository.save(event);
        return new EventDTO(event);
    }

}
