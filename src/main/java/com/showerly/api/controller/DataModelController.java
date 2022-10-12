package com.showerly.api.controller;

import com.showerly.api.controller.viewmodel.DataModelViewModel;
import com.showerly.api.model.DataModel;
import com.showerly.api.model.Shower;
import com.showerly.api.repository.DataModelRepository;
import com.showerly.api.repository.ShowerRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@Api(tags = "2")
@RestController
@RequestMapping("/datamodel")
public class DataModelController {
    @Autowired
    private ShowerRepository showerRepository;
    @Autowired
    private DataModelRepository dataModelRepository;

    @PostMapping("/{showerId}")
    public ResponseEntity sendDataModel(@PathVariable String showerId, @RequestParam String token, @RequestBody DataModelViewModel dataModelViewModel) {
        Optional<Shower> optionalShower = this.showerRepository.findById(showerId);
        if (optionalShower.isEmpty()) {
            return ResponseEntity.status(400).body("Shower with id " + showerId + " does not exist");
        }

        Shower shower = optionalShower.get();
        if (!shower.getToken().equals(token)) {
            // TODO change this to generic error so that it is unknown whether token or id is incorrect
            return ResponseEntity.status(400).body("Token incorrect");
        }

        DataModel dataModel = new DataModel();
        dataModel.setTime(Instant.now());
        dataModel.setAvgTemperature(dataModelViewModel.getAvgTemperature());
        dataModel.setAmountOfWater(dataModelViewModel.getAmountOfWater());
        this.dataModelRepository.save(dataModel);

        shower.getDataModels().add(dataModel);
        this.showerRepository.save(shower);

        return ResponseEntity.noContent().build();
    }
}
