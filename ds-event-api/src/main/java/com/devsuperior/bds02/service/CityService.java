package com.devsuperior.bds02.service;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.exception.EntityNotFoundException;
import com.devsuperior.bds02.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public void deleteCity(Long id) {
        try {
            cityRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format("Id %d not found", id));
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Integrity violation");
        }
    }

    public List<CityDTO> findAll() {
        List<City> cities = cityRepository.findAll(Sort.by("name"));
        return cities.stream().map(CityDTO::new).collect(Collectors.toList());
    }

    public CityDTO insert(CityDTO cityDTO) {
        City city = new City();
        city.setName(cityDTO.getName());
        city = cityRepository.save(city);
        return new CityDTO(city);
    }

}
