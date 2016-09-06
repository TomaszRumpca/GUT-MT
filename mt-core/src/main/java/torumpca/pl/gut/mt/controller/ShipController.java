package torumpca.pl.gut.mt.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import torumpca.pl.gut.mt.dsm.model.ShipMetaData;
import torumpca.pl.gut.mt.repository.ShipMetaDataRepository;

import java.util.List;

/**
 * Created by Tomasz Rumpca on 2016-08-13.
 */
@RestController
@RequestMapping("api/")
public class ShipController {

    @Autowired
    private ShipMetaDataRepository shipMetaDataRepository;

    @RequestMapping(value = "ships", method = RequestMethod.GET)
    public List<ShipMetaData> list() {
        return shipMetaDataRepository.findAll();
    }

    @RequestMapping(value = "ships/{id}", method = RequestMethod.GET)
    public ShipMetaData get(@PathVariable Long id) {
        return shipMetaDataRepository.findOne(id);
    }

    @RequestMapping(value = "ships", method = RequestMethod.POST)
    public ShipMetaData create(@RequestBody ShipMetaData shipMetaData) {
        return shipMetaDataRepository.saveAndFlush(shipMetaData);
    }


    @RequestMapping(value = "ships/{id}", method = RequestMethod.PUT)
    public ShipMetaData put(@PathVariable Long id, @RequestBody ShipMetaData shipMetaData) {
        ShipMetaData existingMetaData = shipMetaDataRepository.findOne(id);
        BeanUtils.copyProperties(shipMetaData, existingMetaData);
        return shipMetaDataRepository.saveAndFlush(existingMetaData);
    }

    @RequestMapping(value = "ships/{id}", method = RequestMethod.DELETE)
    public ShipMetaData delete(@PathVariable Long id) {
        ShipMetaData shipMetaData = shipMetaDataRepository.findOne(id);
        shipMetaDataRepository.delete(id);
        return shipMetaData;
    }

}
