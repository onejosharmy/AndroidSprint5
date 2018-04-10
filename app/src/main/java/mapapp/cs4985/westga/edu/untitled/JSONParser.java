package mapapp.cs4985.westga.edu.untitled;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {
    JSONObject forecast;

    public JSONParser(String jsonfile) {
        try {
            forecast = new JSONObject(jsonfile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Entry> forecastEntryList() {
        JSONObject properties = null;
        ArrayList<Entry> list = new ArrayList<Entry>();
        try {
            properties = this.forecast.getJSONObject("properties");
        } catch (JSONException e) {
            return list;
        }

        JSONArray periods = null;
        try {
            periods = properties.getJSONArray("periods");
        } catch (JSONException e) {
            return list;
        }

        for (int i = 0; i < periods.length(); i++) {
            try {
                String name = periods.getJSONObject(i).getString("name");
                String shortForecast = periods.getJSONObject(i).getString("shortForecast");
                String detailedForecst = periods.getJSONObject(i).getString("detailedForecast");
                String temperature = periods.getJSONObject(i).getString("temperature");
                String image = periods.getJSONObject(i).getString("icon");
                Entry theEntry = new Entry(name, shortForecast, detailedForecst, temperature, image);
                String forecast = name + "\n\t\t" + temperature + "\t\t" + shortForecast + "\t\t" + image;
                list.add(theEntry);
            } catch (JSONException e) {
                return list;
            }
        }
        return list;
    }
}

