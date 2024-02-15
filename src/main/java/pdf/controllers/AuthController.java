package pdf.controllers;

import pdf.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pdf.models.UserModel;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public UserModel save(@RequestBody UserModel user) {
        return userService.createUser(user);
    }

}
