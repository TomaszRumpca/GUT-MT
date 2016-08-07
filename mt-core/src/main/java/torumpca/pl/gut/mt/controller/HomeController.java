package torumpca.pl.gut.mt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tomasz Rumpca on 2016-08-07.
 */
@RestController
public class HomeController {

    @RequestMapping("/")
    public String home(){
        return "home page";

    }
}
