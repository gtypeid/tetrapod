package com.kosta.apiserver.spring.ctl.async;

import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import com.kosta.common.spring.data.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/user")
@Profile("async")
public class UserController {

    private final RestApiConnector restApiConnector;

    @Autowired
    public UserController(RestApiConnector restApiConnector){
        this.restApiConnector = restApiConnector;
    }


    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.POST)
                        .url("http://localhost:8093/user/create")
                        .body(user).build()
                , User.class);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long id){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.GET)
                        .url("http://localhost:8093/user/" + id).build()
                , User.class);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long id){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.PUT)
                        .url("http://localhost:8093/user/" + id).build()
                , User.class);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("userId") Long id){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.DELETE)
                        .url("http://localhost:8093/user/" + id).build()
                , Boolean.class);
    }

}
