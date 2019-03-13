package torumpca.pl.gut.mt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.web.bind.annotation.*;
import torumpca.pl.gut.mt.repository.User;
import torumpca.pl.gut.mt.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("users/")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") long id) {
        return userRepository.findById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public User deleteUser(@PathVariable("id") long id) {
        return userRepository.deleteById(id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public User updateUser(@RequestBody User user) {
//        return userRepository.update(user);
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    public boolean authenticate(@RequestBody UserCredentials credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());
        return user.getPassword().equals(credentials.getPassword());
    }
}
