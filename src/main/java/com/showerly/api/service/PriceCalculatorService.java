package com.showerly.api.service;

import com.showerly.api.model.DataModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceCalculatorService {

    // TODO get this from external source
    private static double PRICE = 0.84;

    public double calculatePrice(DataModel dataModel) {
        // TODO include temperature
//        System.out.println(round(dataModel.getAmountOfWater() * PRICE, 2));
        double val = round(dataModel.getAmountOfWater() * PRICE, 2);
        return val;
    }

    public static double round(double value, int places) {
        value = value * Math.pow(10, places);
        value = Math.round(value);
        return value / Math.pow(10, places);
    }
}
