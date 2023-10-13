package br.com.devmaicon.todolist.user;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {

        try {
            var user = this.userRepository.findByUsername(userModel.getUsername());

            var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

            userModel.setPassword(passwordHash);

            if (user != null) {
               throw new Exception("User already exists");
            }

            var userCreated = this.userRepository.save(userModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

        } catch (Exception e) {
            var errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }
}
