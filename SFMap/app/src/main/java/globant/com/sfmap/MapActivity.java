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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Collections;


public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean showingCrimePin = false;
    Marker currentCrimeMarker;
    Toolbar mToolbar;

    public static final String MAP_FLAG = "showingCrimePin";
    public static final String CRIME_LAT = "lat";
    public static final String CRIME_LON = "lon";
    public static final String CRIME_TITLE = "title";
    public static final String CRIME_SNIPPET = "snippet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

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
        IconGenerator factory = new IconGenerator(this);
        ArrayList<District> districts = ReportsRetriever.getInstance().getDistricts();
        Collections.sort(districts, new District.ReportComparator());
        for (int i = 0; i < districts.size(); i++) {
            District district = districts.get(i);
            factory.setColor(Color.parseColor(District.colors.get(i)));
            addIcon(factory, district.mName + "\n" + district.reports, new LatLng(district.lat, district.lon));
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
        updateTitle();
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

    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mMap.addMarker(markerOptions);
    }

    private void updateTitle() {
        mToolbar.setTitle(getString(R.string.reports, ReportsRetriever.getInstance().getTotalReportLoaded()));
    }

}
