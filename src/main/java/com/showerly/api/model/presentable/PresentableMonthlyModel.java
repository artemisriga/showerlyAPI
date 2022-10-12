package com.showerly.api.model.presentable;

import lombok.Data;

import java.util.List;

@Data
public class PresentableMonthlyModel {
    private int month;
    private float pricePerMonth;
    private List<PresentableDailyModel> dailyModels;
}
