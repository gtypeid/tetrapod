package com.kosta.apiserver.spring.ctl.sync;

import com.kosta.common.spring.data.dto.UserDTO;
import com.kosta.common.core.module.chain.ServerChainService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/user")
    @Profile("sync")
    public class SyncUserController {

        @Autowired
        ServerChainService serverChainService;

        @PostMapping("/create")
        public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user, HttpServletRequest request){
            if ( serverChainService.isChain() ){
                return serverChainService.gate(request, user);
            }
            return ResponseEntity.ok( user );
        }

        @GetMapping("/{userId}")
        public ResponseEntity<UserDTO> getUser(@PathVariable("userId") long userId, HttpServletRequest request){
            if ( serverChainService.isChain() ){
                return serverChainService.gate(request, userId);
            }
            return ResponseEntity.notFound().build();
        }

        @PutMapping("/{userId}")
        public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") long userId,
                                                  @RequestBody UserDTO user,
                                                  HttpServletRequest request ){
            return ResponseEntity.notFound().build();
        }

        @DeleteMapping("/{userId}")
        public ResponseEntity<Boolean> deleteUser(@PathVariable("userId") long userId){
            return ResponseEntity.notFound().build();
        }
    }
