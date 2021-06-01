package br.com.dsclient.controller;

import br.com.dsclient.model.Client;
import br.com.dsclient.model.dto.ClientDTO;
import br.com.dsclient.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public Page<ClientDTO> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
                                @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return clientService.findAllPage(pageRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        try {
            ClientDTO clientDTO = clientService.findByIdClient(id);
            return ResponseEntity.ok(clientDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ClientDTO insert(@RequestBody Client client) {
        return clientService.insertClient(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@RequestBody ClientDTO clientInput, @PathVariable Long id) {
        try {
            ClientDTO clientDTO = clientService.updateClient(clientInput, id);
            return ResponseEntity.ok(clientDTO);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
