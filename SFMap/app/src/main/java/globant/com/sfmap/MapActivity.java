package globant.com.sfmap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;


public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean showingCrimePin = false;
    Marker currentCrimeMarker;

    public static final String MAP_FLAG = "showingCrimePin";
    public static final String CRIME_LAT = "lat";
    public static final String CRIME_LON = "lon";
    public static final String CRIME_TITLE = "title";
    public static final String CRIME_SNIPPET = "snippet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        showingCrimePin = getIntent().getBooleanExtra(MAP_FLAG, false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sf = new LatLng(37.773972, -122.431297);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sf, 12));

        mMap.clear();

        ArrayList<District> districts = ReportsRetriever.getInstance().getDistricts();
        Collections.sort(districts, new District.ReportComparator());
        for (int i = 0; i < districts.size(); i++) {
            District district = districts.get(i);
            BitmapDescriptor descriptor = getMarkerIconColorFromStringColor(District.colors.get(i));
            mMap.addMarker(new MarkerOptions()
                            .title(district.mName)
                            .position(new LatLng(district.lat, district.lon))
                            .snippet(getString(R.string.reports, district.reports))
                            .icon(descriptor)
            );
        }

        if (showingCrimePin) {
            try {
                double lat = Double.parseDouble(getIntent().getStringExtra(CRIME_LAT));
                double lon = Double.parseDouble(getIntent().getStringExtra(CRIME_LON));
                currentCrimeMarker = mMap.addMarker(new MarkerOptions()
                                .title(getIntent().getStringExtra(CRIME_TITLE))
                                .position(new LatLng(lat, lon))
                                .snippet(getIntent().getStringExtra(CRIME_SNIPPET))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_crime))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    // method definition
    public BitmapDescriptor getMarkerIconColorFromStringColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

}
