package com.showerly.api.service;

import com.showerly.api.model.DataModel;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;

@Service
public class PriceCalculatorService {

    private static float GASPRICE = 0;
    private static int DAYUPDATED = -1;

    public float calculatePrice(DataModel dataModel) {
//        float cents = Math.round(dataModel.getAmountOfWater() * GASPRICE);
//        return (cents / 100);
        float amountOfWater = dataModel.getAmountOfWater();
        float avgtemp = dataModel.getAvgTemperature() - 10;
        float WaterPrice = 0.00089f;
        //Beginning calculations
        float Qenergy = (float) (amountOfWater * 4186 * avgtemp);
//        float CombustionHeatOfGas = (float) 35.17;
//        float GasVolumeInLiters = Qenergy / CombustionHeatOfGas;
//        float GasVolumeInCubicMeters = GasVolumeInLiters / 1000;
        float gas = Qenergy/263775;
        float Price = ((float) (GASPRICE * 0.75 * gas) + (WaterPrice * amountOfWater))/10;
        float finalPriceToReturn = Math.round(Price);
        return finalPriceToReturn /100;

    }

    public static void updatePrice() {
        // TODO move to application.properties
        String timezone = "Europe/Amsterdam";
        int day = Instant.now().atZone(ZoneId.of(timezone)).getDayOfMonth();
        if (DAYUPDATED == day) {
            return;
        }

        DAYUPDATED = day;

        JSONObject result;
        try {
            // https://www.theice.com/products/27996665/Dutch-TTF-Gas-Futures/data?marketId=5439161
            URL url = new URL("https://www.theice.com/marketdata/DelayedMarkets.shtml?getIntradayChartDataAsJson=&marketId=5439161");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            int expectedStatus = 200;
            if (status != expectedStatus) {
                System.out.println("Expected status code " + expectedStatus + " but got " + status);
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            result = new JSONObject(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        GASPRICE = result.getFloat("settlementPrice");
        System.out.println("Price updated to: €" + (GASPRICE / 100));
    }
}
