package dmu.dasom.miniproject.controller;

import dmu.dasom.miniproject.dto.UserDto;
import dmu.dasom.miniproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDto registerUser(UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PostMapping("/login")
    public boolean login(UserDto userDto) {
        return userService.login(userDto);
    }

}
