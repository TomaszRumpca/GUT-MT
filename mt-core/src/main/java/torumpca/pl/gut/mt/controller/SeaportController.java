package torumpca.pl.gut.mt.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import torumpca.pl.gut.mt.dsm.model.Seaport;
import torumpca.pl.gut.mt.repository.SeaportRepository;

import java.util.List;

/**
 * Created by Tomasz Rumpca on 2016-08-13.
 */
@RestController
@RequestMapping("api/")
public class SeaportController {
    
    @Autowired
    private SeaportRepository seaportRepository;
    
    @RequestMapping(value = "seaports", method = RequestMethod.GET)
    public List<Seaport> list() {
        return seaportRepository.findAll();
    }
    
    @RequestMapping(value = "seaports/{id}", method = RequestMethod.GET)
    public Seaport get(@PathVariable Long id) {
        return seaportRepository.findOne(id);
    }
    
    @RequestMapping(value = "seaports", method = RequestMethod.POST)
    public Seaport create(@RequestBody Seaport seaport) {
        return seaportRepository.saveAndFlush(seaport);
    }
    
    
    @RequestMapping(value = "seaports/{id}", method = RequestMethod.PUT)
    public Seaport put(@PathVariable Long id, @RequestBody Seaport seaport) {
        Seaport existingSeaport = seaportRepository.findOne(id);
        BeanUtils.copyProperties(seaport, existingSeaport);
        return seaportRepository.saveAndFlush(existingSeaport);
    }
    
    @RequestMapping(value = "seaports/{id}", method = RequestMethod.DELETE)
    public Seaport delete(@PathVariable Long id) {
        Seaport seaport = seaportRepository.findOne(id);
        seaportRepository.delete(id);
        return seaport;
    }
    
}
