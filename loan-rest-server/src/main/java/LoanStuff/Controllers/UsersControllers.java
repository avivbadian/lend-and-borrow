package LoanStuff.Controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
public class UsersControllers {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/users/login/{username}/{password}")
    public Boolean greeting(@PathVariable String username, @PathVariable String password) {
        return  true;
    }
}