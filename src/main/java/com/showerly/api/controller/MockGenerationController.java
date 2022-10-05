package com.showerly.api.controller;

import com.showerly.api.model.DataModel;
import com.showerly.api.model.Shower;
import com.showerly.api.repository.DataModelRepository;
import com.showerly.api.repository.ShowerRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

@Api(tags = "Generate mock")
@RestController
@RequestMapping("/MOCK")
public class MockGenerationController {
    @Autowired
    private DataModelRepository dataModelRepository;
    @Autowired
    private ShowerRepository showerRepository;

    @PostMapping
    public ResponseEntity createMock() {
        Shower shower1 = new Shower();
        shower1.setId("1");
        shower1.setToken("t1");
        shower1.setDataModels(new HashSet<>());
        this.showerRepository.save(shower1);
        Shower shower2 = new Shower();
        shower2.setId("2");
        shower2.setToken("t2");
        shower2.setDataModels(new HashSet<>());
        this.showerRepository.save(shower2);

        this.generateDataModel(shower1, 35, 10, Instant.now().minus(4, ChronoUnit.MINUTES));
        this.generateDataModel(shower1, 37, 13, Instant.now().minus(3, ChronoUnit.MINUTES));
        this.generateDataModel(shower1, 38, 20, Instant.now().minus(2, ChronoUnit.MINUTES));
        this.generateDataModel(shower1, 39, 5, Instant.now().minus(1, ChronoUnit.MINUTES));

        this.generateDataModel(shower1, 39, 25, Instant.now().minus(3, ChronoUnit.MINUTES).minus(1, ChronoUnit.DAYS));
        this.generateDataModel(shower1, 35, 1, Instant.now().minus(2, ChronoUnit.MINUTES).minus(1, ChronoUnit.DAYS));
        this.generateDataModel(shower1, 35, 5, Instant.now().minus(1, ChronoUnit.MINUTES).minus(1, ChronoUnit.DAYS));

        this.generateDataModel(shower1, 34, 25, Instant.now().minus(3, ChronoUnit.MINUTES).minus(60, ChronoUnit.DAYS));
        this.generateDataModel(shower1, 36, 20, Instant.now().minus(2, ChronoUnit.MINUTES).minus(60, ChronoUnit.DAYS));
        this.generateDataModel(shower1, 38, 5, Instant.now().minus(1, ChronoUnit.MINUTES).minus(60, ChronoUnit.DAYS));

        this.generateDataModel(shower2, 34, 13, Instant.now().minus(54, ChronoUnit.MINUTES));
        this.generateDataModel(shower2, 35, 15, Instant.now().minus(53, ChronoUnit.MINUTES));
        this.generateDataModel(shower2, 35, 32, Instant.now().minus(52, ChronoUnit.MINUTES));
        this.generateDataModel(shower2, 31, 10, Instant.now().minus(51, ChronoUnit.MINUTES));

        return ResponseEntity.noContent().build();
    }

    private void generateDataModel(Shower shower, int avgTemperature, int amountOfWater, Instant time) {
        DataModel dataModel = new DataModel();
        dataModel.setTime(time);
        dataModel.setAmountOfWater(amountOfWater);
        dataModel.setAvgTemperature(avgTemperature);
        this.dataModelRepository.save(dataModel);
        shower.getDataModels().add(dataModel);
        this.showerRepository.save(shower);
    }
}
