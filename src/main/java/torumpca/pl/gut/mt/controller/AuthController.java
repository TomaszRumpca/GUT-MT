package torumpca.pl.gut.mt.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "*",
             maxAge = 3600,
             allowedHeaders = {"x-auth-token", "x-requested-with", "x-xsrf-token",
                     "Authentication`"})
public class AuthController {

    @RequestMapping(value = "user")
    public Principal getUser(Principal user) {
        return user;
    }

    //    @RequestMapping("/token")
    //    public Map<String,String> token(HttpSession session) {
    //        return Collections.singletonMap("token", session.getId());
    //    }
}
