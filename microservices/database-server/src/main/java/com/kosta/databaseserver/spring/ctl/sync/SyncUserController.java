package com.kosta.databaseserver.spring.ctl.sync;

import com.kosta.common.spring.data.dto.UserDTO;
import com.kosta.databaseserver.spring.repo.abs.sync.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Profile("sync")
public class SyncUserController {

    private final UserRepository userRepository;

    @Autowired
    public SyncUserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user){

        return ResponseEntity.ok(
                userRepository
                        .create(user.castInboundEntity())
                        .castOutboundDTO(0) );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") long userId){
        return ResponseEntity.ok(
                userRepository
                        .read(userId)
                        .castOutboundDTO(1) );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") long userId,
                                           @RequestBody UserDTO user){
        return ResponseEntity.ok(
                userRepository
                        .update(userId, user.castInboundEntity())
                        .castOutboundDTO(0) );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("userId") long userId){
        return ResponseEntity.ok( userRepository.delete(userId) );
    }
}
