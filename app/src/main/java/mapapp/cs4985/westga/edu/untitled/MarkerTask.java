package mapapp.cs4985.westga.edu.untitled;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MarkerTask extends AsyncTask<Void, Void, String> {
    private static final String SERVICE_URL = "AIzaSyAur5sNgpt2bxCixYQZc0a62YwNkgnvx6Y";
    private AsyncResponse delegate = null;
    protected GoogleMap map;
    double lat = 33.575;
    double lon = -85.098;
    private List<Entry> listy;


    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(Void... args) {
        System.out.println("doing");
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(getUrl());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            System.out.print("JSON parsing error");
            //throw new IOException("Error connecting to service", e); //uncaught
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return json.toString();
    }

    public List<Entry> onPostExecute() {
        return listy;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String json) {

        JSONParser parser = new JSONParser(json);
        listy = parser.forecastEntryList();
        System.out.println(listy.size() + "---------");
        delegate.processFinish(json);
    }

    private String getUrl() {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + lat + "," + lon);
        googlePlacesUrl.append("&radius=" + 5000);
        googlePlacesUrl.append("&keyword=" + "food");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAur5sNgpt2bxCixYQZc0a62YwNkgnvx6Y");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }
}
