package torumpca.pl.gut.mt.controller;

import org.springframework.web.bind.annotation.*;
import torumpca.pl.gut.mt.model.Shipwreck;

import java.util.List;

/**
 * Created by Tomasz Rumpca on 2016-08-07.
 */
@RestController
@RequestMapping("api/v1/")
public class ShipwreckController {

    @RequestMapping(value = "shipwrecks", method = RequestMethod.GET)
    public List<Shipwreck> list(){
        return ShipwreckStub.list();
    }

    @RequestMapping(value = "shipwrecks", method = RequestMethod.POST)
    public Shipwreck create(@RequestBody Shipwreck shipwreck){
        return ShipwreckStub.create(shipwreck);
    }

    @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.GET)
    public Shipwreck get(@PathVariable Long id){
        return ShipwreckStub.get(id);
    }

    @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.PUT)
    public Shipwreck put(@PathVariable Long id, @RequestBody Shipwreck shipwreck){
        return ShipwreckStub.update(id, shipwreck);
    }

    @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.DELETE)
    public Shipwreck delete(@PathVariable Long id){
        return ShipwreckStub.delete(id);
    }

}
