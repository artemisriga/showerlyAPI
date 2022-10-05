package com.showerly.api.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
public class DataModel {
    @Id
    @GeneratedValue
    private long id;
    private Instant time;
    private int amountOfWater;
    private int avgTemperature;
}
