package com.showerly.api.model.presentable;

import lombok.Data;

import java.util.List;

@Data
public class PresentableShower {
    private float totalPriceToday;
    private List<PresentableMonthlyModel> monthlyModels;
}
