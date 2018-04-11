package mapapp.cs4985.westga.edu.untitled;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {
    JSONObject forecast;
    JSONObject nearbyThings;

    public JSONParser(String jsonfile) {
        try {
            nearbyThings = new JSONObject(jsonfile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Entry> forecastEntryList() {
        JSONArray properties = null;
        ArrayList<Entry> list = new ArrayList<Entry>();
        try {
            properties = this.nearbyThings.getJSONArray("results");
            System.out.println(properties.toString());
        } catch (JSONException e) {
            return list;
        }

        JSONArray periods = null;

            //periods = properties.getJSONObject(0);

        //catch (JSONException e) {
           // return list;
        //}

        for (int i = 0; i < properties.length(); i++) {
            try {
                String name = properties.getJSONObject(i).getString("name");
                JSONObject geo = properties.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");

                double lat = geo.getDouble("lat");
                double lon = geo.getDouble("lng");
                System.out.println(name + " " + lat + " " + lon);
                /**String shortForecast = periods.getJSONObject(i).getString("shortForecast");
                String detailedForecst = periods.getJSONObject(i).getString("detailedForecast");
                String temperature = periods.getJSONObject(i).getString("temperature");
                String image = periods.getJSONObject(i).getString("icon");**/
                Entry theEntry = new Entry(name, lat, lon);
                //String forecast = name + "\n\t\t" + temperature + "\t\t" + shortForecast + "\t\t" + image;
                list.add(theEntry);
            } catch (JSONException e) {
                return list;
            }
        }
        return list;
    }
}

