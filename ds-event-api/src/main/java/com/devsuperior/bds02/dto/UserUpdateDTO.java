package com.devsuperior.bds02.dto;

import com.devsuperior.bds02.service.validation.UserUpdateValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UserUpdateValid
public class UserUpdateDTO extends UserDTO {

}
