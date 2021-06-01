package br.com.dsclient.model.dto;

import br.com.dsclient.model.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ClientDTO {

    private String name;
    private String cpf;
    private Double income;
    private Instant birthDate;
    private Integer children;

    public ClientDTO(Client clientFound) {
        this.name = clientFound.getName();
        this.cpf = clientFound.getCpf();
        this.birthDate = clientFound.getBirthDate();
        this.income = clientFound.getIncome();
        this.children = clientFound.getChildren();
    }
}
