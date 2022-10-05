package com.showerly.api.service;

import com.showerly.api.model.DataModel;
import com.showerly.api.model.presentable.PresentableDailyModel;
import com.showerly.api.model.presentable.PresentableMonthlyModel;
import com.showerly.api.model.presentable.PresentableShower;
import com.showerly.api.model.Shower;
import com.showerly.api.repository.ShowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PresentableService {
    private static String TIMEZONE = "Europe/Amsterdam";

    @Autowired
    private ShowerRepository showerRepository;

    @Autowired
    private PriceCalculatorService priceCalculatorService;

    public PresentableShower getPresentableShower(String showerId) {
        Shower shower = this.showerRepository.getById(showerId);

        PresentableShower presentableShower = new PresentableShower();
        presentableShower.setMonthlyModels(generateMonthlyModels(shower));

        for (PresentableDailyModel presentableDailyModel : presentableShower.getMonthlyModels().get(0).getDailyModels()) {
            if (presentableDailyModel.getDay() == this.getDay()) {
                presentableShower.setTotalPriceToday(presentableDailyModel.getPrice());
                break;
            }
        }

        return presentableShower;
    }

    private List<PresentableMonthlyModel> generateMonthlyModels(Shower shower) {
        // Create the monthly models of last year
        List<PresentableMonthlyModel> monthlyModels = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            PresentableMonthlyModel presentableMonthlyModel = new PresentableMonthlyModel();
            presentableMonthlyModel.setMonth(modulo12(this.getMonth() - i));
            presentableMonthlyModel.setDailyModels(new ArrayList<>());
            monthlyModels.add(presentableMonthlyModel);
        }

        // Put daily dataModels into monthly models as daily models
        dailyModelLoop:
        for (DataModel dataModel : shower.getDataModels()) {
            if (!dataModel.getTime().isAfter(Instant.now().minus(365, ChronoUnit.DAYS))) {
                continue;
            }

            for (PresentableMonthlyModel presentableMonthlyModel : monthlyModels) {
                if (presentableMonthlyModel.getMonth() == getMonth(dataModel.getTime())) {

                    double priceOfDataModel = this.priceCalculatorService.calculatePrice(dataModel);

                    for (PresentableDailyModel presentableDailyModel : presentableMonthlyModel.getDailyModels()) {
                        if (presentableDailyModel.getDay() == this.getDay(dataModel.getTime())) {
                            presentableDailyModel.setPrice(presentableDailyModel.getPrice() + priceOfDataModel);
                            continue dailyModelLoop;
                        }
                    }

                    PresentableDailyModel presentableDailyModel = new PresentableDailyModel();
                    presentableDailyModel.setPrice(priceOfDataModel);
                    presentableDailyModel.setDay(this.getDay(dataModel.getTime()));
                    presentableMonthlyModel.getDailyModels().add(presentableDailyModel);

                    continue dailyModelLoop;
                }
            }
        }

        // Calculate monthly price
        for (PresentableMonthlyModel presentableMonthlyModel : monthlyModels) {
            for (PresentableDailyModel presentableDailyModel : presentableMonthlyModel.getDailyModels()) {
                presentableMonthlyModel.setPricePerMonth(presentableMonthlyModel.getPricePerMonth() + presentableDailyModel.getPrice());
            }
        }

        return monthlyModels;
    }

    private int getMonth() {
        return this.getMonth(Instant.now());
    }

    private int getMonth(Instant time) {
        return time.atZone(ZoneId.of(TIMEZONE)).getMonth().getValue();
    }

    private int getDay() {
        return this.getDay(Instant.now());
    }

    private int getDay(Instant time) {
        return time.atZone(ZoneId.of(TIMEZONE)).getDayOfMonth();
    }

    private int modulo12(int i) {
        while (i <= 0) {
            i = i + 12;
        }
        while (i > 12) {
            i = i - 12;
        }
        return i;
    }
}
