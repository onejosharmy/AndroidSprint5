package mapapp.cs4985.westga.edu.untitled;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MarkerTask extends AsyncTask<Void, Void, String> {
    private static final String SERVICE_URL = "OUR_JSON_API";
    protected GoogleMap map;

    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(Void... args) {

        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            //throw new IOException("Error connecting to service", e); //uncaught
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return json.toString();
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String json) {

        try {
            // De-serialize the JSON string into an array of city objects
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                LatLng latLng = new LatLng(jsonObj.getJSONArray("latlng").getDouble(0),
                        jsonObj.getJSONArray("latlng").getDouble(1));

                //move CameraPosition on first result
                if (i == 0) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(13).build();

                    map.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }

                // Create a marker for each city in the JSON data.
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(jsonObj.getString("name"))
                        .snippet(Integer.toString(jsonObj.getInt("population")))
                        .position(latLng));
            }
        } catch (JSONException e) {
        }
    }
}
