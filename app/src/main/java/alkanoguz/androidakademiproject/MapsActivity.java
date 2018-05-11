package alkanoguz.androidakademiproject;

import android.Manifest;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    String address = "";
    double lat;
    double lng;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
            mMap.clear();
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null){
                double latitude = lastLocation.getLatitude();
                double longitude = lastLocation.getLongitude();
                LatLng lastuserLocation = new LatLng(latitude,longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastuserLocation, 15));
            }


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, locationListener);
                mMap.clear();
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng lastuserLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastuserLocation, 15));
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        HashMap hashMap = new HashMap();
        mMap.clear();
        address = "";
        try {

            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressList != null && addressList.size() > 0) {
                if (addressList.get(0).getAddressLine(0) != null) {
                    address += addressList.get(0).getAddressLine(0);
                }
                if (addressList.get(0).getAddressLine(1) != null) {
                    address += " " + addressList.get(0).getAddressLine(1);
                }
                if (addressList.get(0).getAddressLine(2) != null) {
                    address += " " + addressList.get(0).getAddressLine(2);
                }
                if (addressList.get(0).getAddressLine(3) != null) {
                    address += " " + addressList.get(0).getAddressLine(3);
                }


                hashMap.put("Addressss", address);
                System.out.println(hashMap);
            } else {
                address = "New Place";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        Toast.makeText(getApplicationContext(), "Konum oluşturuldu.", Toast.LENGTH_LONG).show();
        lat = latLng.latitude;
        lng = latLng.longitude;

        System.out.println(latLng.latitude);
        System.out.println(latLng.longitude);
        System.out.println(address);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Konum Onayı");

        // Setting Dialog Message
        alertDialog.setMessage("Konumu doğru seçtiniz mi?");

        // Setting Icon to Dialog


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                pDialog = new ProgressDialog(MapsActivity.this);
                pDialog.setMessage("Yükleniyor...");
                pDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MapsActivity.this,CamTestActivity.class);
                        intent.putExtra("adres", address);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        hidePDialog();
                    }
                },2000);

            }





        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,	int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
