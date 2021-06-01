package br.com.dsclient.service;

import br.com.dsclient.model.Client;
import br.com.dsclient.model.dto.ClientDTO;
import br.com.dsclient.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Page<ClientDTO> findAllPage(PageRequest pageRequest) {
        Page<Client> categories = clientRepository.findAll(pageRequest);
        return categories.map(ClientDTO::new);
    }

    public ClientDTO findByIdClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        return new ClientDTO(client);
    }

    public ClientDTO insertClient(Client client) {
        client = clientRepository.save(client);
        return new ClientDTO(client);
    }

    public ClientDTO updateClient(ClientDTO clientInput, Long id) {
        Client clientFound = clientRepository.getOne(id);
        clientFound.setName(clientInput.getName());
        clientFound.setCpf(clientInput.getCpf());
        clientFound.setBirthDate(clientInput.getBirthDate());
        clientFound.setIncome(clientInput.getIncome());
        clientFound.setChildren(clientInput.getChildren());
        return new ClientDTO(clientFound);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

}
