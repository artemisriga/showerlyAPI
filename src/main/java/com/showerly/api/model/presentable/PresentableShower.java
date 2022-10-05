package com.showerly.api.model.presentable;

import lombok.Data;

import java.util.List;

@Data
public class PresentableShower {
    private double totalPriceToday;
    private List<PresentableMonthlyModel> monthlyModels;
}
