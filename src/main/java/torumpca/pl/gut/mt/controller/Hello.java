package torumpca.pl.gut.mt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @RequestMapping(method = RequestMethod.GET, path = "hello")
    public String sayHello(){
        return "{ \"a\" : \"Hello\" }";
    }
}
