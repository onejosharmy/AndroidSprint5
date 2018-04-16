package mapapp.cs4985.westga.edu.untitled;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

public class MapsMarkerActivity extends ListActivity
        implements OnMapReadyCallback, AsyncResponse {
    double lat = 33.575;
    double lon = -85.098;
    //int REQUEST_PLACE_PICKER = 1;
    MarkerTask markerTask = new MarkerTask();
    AdapterView.AdapterContextMenuInfo menuinfo = null;
    //Handler handler;
    //ThreadFetcher fetcher;
    ListView listview;
    List<Entry> listForView;
    EntryAdapter adapter;
    EditText input;
    Button find;
    //final int TIMEOUT = 240;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.list_search);

        permissionRequester(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionRequester(Manifest.permission.INTERNET);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequester(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionRequester(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        listview = (ListView) findViewById(android.R.id.list);
        input = (EditText) findViewById(R.id.editText);
        ImageView img = (ImageView) findViewById(R.id.button);
       // img.setOnClickListener((OnClickListener)this);

        //this.markerTask.delegate = this;

/**
        public void onClick (View view){
            // String inputText = input.getText().toString();
            // System.out.println(inputText);
            //String searchURL = getUrl(lat, lon, inputText);
            //fetcher = new ThreadFetcher(searchURL);
            //fetcher.start();
            listview.setAdapter(null);
            // handler = new Handler();
            // handler.post(checkFetcher);
        }**/



    }

    public void onClick(View view){
        String inputText = input.getText().toString();
        this.markerTask.execute(getUrl(lat, lon, inputText));
        listview.setAdapter(null);
        displayEntries(this.markerTask.getListy());
        listForView = this.markerTask.getListy();

        registerForContextMenu(listview);
    }

    @Override
    public String processFinish(String output) {
        return output;
    }

    /**
     * Runnable checkFetcher = new Runnable() {
     * int count = 0;
     * <p>
     * public void run() {
     * if (fetcher.isFinished()) {
     * if (fetcher.successful()) {
     * JSONParser parser = new JSONParser(fetcher.getResult());
     * System.out.println(fetcher.getResult());
     * List<Entry> listy = parser.forecastEntryList();
     * displayEntries(listy);
     * listForView = listy;
     * } else {
     * System.out.println("fail");
     * listview.setAdapter(null);
     * listForView = null;
     * }
     * <p>
     * } else {
     * System.out.println("fetching");
     * count++;
     * if (count < TIMEOUT) {
     * handler.postDelayed(checkFetcher, 1000);
     * } else {
     * listview.setAdapter(null);
     * }
     * }
     * }
     * };
     **/
    private void permissionRequester(String resource) {

        int result = ContextCompat.checkSelfPermission(this, resource);
        if (result == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{resource}, 0);
        }

    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 5000);
        googlePlacesUrl.append("&keyword=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "YOUR_KEY_HERE");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }
    //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.575,-85.098&radius=5000&keyword=food&sensor=true&key=YOUR_KEY_HERE"

    private void displayEntries(List<Entry> forecasts) {
        adapter = new EntryAdapter(MapsMarkerActivity.this, R.layout.list_search, forecasts);
        setListAdapter(adapter);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Item Operations");

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.layout.list_menu, menu);
        menuinfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Options");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Long ids = adapter.getItemId(info.position);//what item was selected is ListView
            int i = ids.intValue();

            switch (item.getItemId()) {
                case R.id.delete_item:
                    listForView.remove(i);
                    displayEntries(listForView);
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onContextItemSelected(item);
    }
}