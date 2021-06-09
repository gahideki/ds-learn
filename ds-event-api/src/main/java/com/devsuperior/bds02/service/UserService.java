package com.devsuperior.bds02.service;

import com.devsuperior.bds02.dto.UserDTO;
import com.devsuperior.bds02.dto.UserInsertDTO;
import com.devsuperior.bds02.dto.UserUpdateDTO;
import com.devsuperior.bds02.entities.Role;
import com.devsuperior.bds02.entities.User;
import com.devsuperior.bds02.repository.RoleRepository;
import com.devsuperior.bds02.repository.UserRepository;
import com.devsuperior.bds02.service.exception.DatabaseException;
import com.devsuperior.bds02.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(u -> new UserDTO(u));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO save(UserInsertDTO userInsertDTO) {
        User user = new User();
        copyDtoToEntity(userInsertDTO, user);

        String password = passwordEncoder.encode(userInsertDTO.getPassword());
        user.setPassword(password);

        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        try {
            User user = userRepository.getOne(id);
            copyDtoToEntity(userUpdateDTO, user);
            return new UserDTO(user);
        } catch (EntityNotFoundException ex) {
            throw new ResourceNotFoundException(String.format("Id %d not found", id));
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException(String.format("Id %d not found", id));
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User user) {
        user.setEmail(userDTO.getEmail());

        user.getRoles().clear();
        userDTO.getRoles().forEach(r -> {
            Role role = roleRepository.getOne(r.getId());
            user.getRoles().add(role);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(ObjectUtils.isEmpty(user)) {
            logger.error("User not found {}", username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("User {} found", username);
        return user;
    }

}
