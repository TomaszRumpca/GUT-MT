package torumpca.pl.gut.mt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import torumpca.pl.gut.mt.repository.Seaport;
import torumpca.pl.gut.mt.repository.SeaportRepository;

import java.util.List;

@RestController
public class SeaportsController {

    private final SeaportRepository seaportRepository;

    @Autowired
    public SeaportsController(SeaportRepository seaportRepository) {
        this.seaportRepository = seaportRepository;
    }

    @RequestMapping(value = "seaport", method = RequestMethod.GET)
    public List<Seaport> getAllSeaports() {
        return seaportRepository.findAll();
    }

}
