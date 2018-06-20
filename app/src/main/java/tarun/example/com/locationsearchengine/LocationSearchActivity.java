package tarun.example.com.locationsearchengine;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This activity displays a view with a world map and a search input field for the user to search for any location.
 */
public class LocationSearchActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = LocationSearchActivity.class.getSimpleName();
    private static final float LOCATION_ZOOM_LEVEL = 10f;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        setupMapFragment();
        setupPlaceAutoCompleteFragment();
    }

    /**
     * This method sets up the Map Fragment which will display the world map.
     */
    private void setupMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // Set a callback which will be triggered when the GoogleMap instance is ready to be used.
        mapFragment.getMapAsync(this);
    }

    /**
     * This method sets up the PlaceAutoCompleteFragment which displays the auto complete search bar
     * at the top of the screen for user to search for locations.
     */
    private void setupPlaceAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Removes the existing marker from the map.
                mMap.clear();
                // Add a marker along with title as info for the new location selected by the user on searching.
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                // Animates the movement of the camera from the current position to the position of
                // the selected location and set the zoom level to 10 by default.
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), LOCATION_ZOOM_LEVEL));
            }

            @Override
            public void onError(Status status) {
                // Something went wrong. Display an error message to the user.
                Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(LocationSearchActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set the map instance.
        mMap = googleMap;
    }
}
