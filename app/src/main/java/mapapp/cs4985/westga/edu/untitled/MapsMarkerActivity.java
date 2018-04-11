package mapapp.cs4985.westga.edu.untitled;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends ListActivity
        implements OnMapReadyCallback {
    double lat = 33.575;
    double lon = -85.098;
    int REQUEST_PLACE_PICKER = 1;
    AdapterView.AdapterContextMenuInfo menuinfo = null;
    Handler handler;
    ThreadFetcher fetcher;
    //TextView textview;
    ListView listview;
    List<Entry> listForView;
    EntryAdapter adapter;
    EditText input;
    final int TIMEOUT = 240;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        //$setContentView(R.layout.activity_maps);
        setContentView(R.layout.list_search);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        //$SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //$        .findFragmentById(R.id.map);
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


        //textview = (TextView) findViewById(R.id.textview);
        listview = (ListView) findViewById(android.R.id.list);
        input = (EditText) findViewById(R.id.editText);
        ImageView img = (ImageView) findViewById(R.id.button);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inputText = input.getText().toString();
                System.out.println(inputText);
                String searchURL = getUrl(lat,lon,inputText);
                fetcher = new ThreadFetcher(searchURL);
                fetcher.start();
                listview.setAdapter(null);
                //textview.setText("retrieving");
                handler = new Handler();
                handler.post(checkFetcher);
            }
        });
        registerForContextMenu(listview);


        //$mapFragment.getMapAsync(this);
        /**int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e){
            System.out.println("something went wrong");
        }**/

    }

    Runnable checkFetcher = new Runnable() {
        int count = 0;

        public void run() {
            if (fetcher.isFinished()) {
                if (fetcher.successful()) {
                    //textview.setText("");
                    JSONParser parser = new JSONParser(fetcher.getResult());
                    System.out.println(fetcher.getResult());
                    List<Entry> listy = parser.forecastEntryList();
                    displayEntries(listy);
                    listForView = listy;
                } else {
                    //textview.setText(("failed"));
                    System.out.println("fail");
                    listview.setAdapter(null);
                    listForView = null;
                }

            } else {
                System.out.println("fetching");
                count++;
                if (count < TIMEOUT) {
                    handler.postDelayed(checkFetcher, 1000);
                } else {
                    //textview.setText("No Network connection");
                    listview.setAdapter(null);
                }
            }
        }
    };

    private void permissionRequester(String resource) {

        int result = ContextCompat.checkSelfPermission(this, resource);
        if (result == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{resource}, 0);
        }

    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        //LatLng sydney = new LatLng(-33.852, 151.211);
        //LatLng current = new LatLng(lat,lon);
        //googleMap.addMarker(new MarkerOptions().position(current).title("Marker at current location"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }
/**
    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }
**//**
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            //mViewName.setText(name);
            //mViewAddress.setText(address);
            //mViewAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
     }**/
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Item Operations");

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.layout.list_menu,menu);
        menuinfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Options");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try{
            menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Long ids = adapter.getItemId(info.position);//what item was selected is ListView
            int i = ids.intValue();

            switch (item.getItemId()) {

                case R.id.delete_item:

                    listForView.remove(i);
                    displayEntries(listForView);
                    return true;


            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return super.onContextItemSelected(item);
    }
}