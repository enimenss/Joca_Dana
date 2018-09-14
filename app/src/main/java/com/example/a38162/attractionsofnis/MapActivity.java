package com.example.a38162.attractionsofnis;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private LocationManager locationManager;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    List<Place> landmarks;
    List<Place> museums;
    List<Place> restaurants;
    ArrayList<LatLng> listPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        landmarks = new ArrayList<>();
        museums = new ArrayList<>();
        restaurants = new ArrayList<>();
        listPoints = new ArrayList<>();
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase firebaseDatabase = null;
        DatabaseReference databaseReference;
        databaseReference = firebaseDatabase.getInstance().getReference("places");
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        //checking the network provider is enabled
       /* if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Geocoder  geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String string = addressList.get(0).getLocality() + ", ";
                        string += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(string));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {           }

                @Override
                public void onProviderEnabled(String s) {               }

                @Override
                public void onProviderDisabled(String s) {                }
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Geocoder  geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String string = addressList.get(0).getLocality() + ", ";
                        string += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(string));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {                }

                @Override
                public void onProviderEnabled(String s) {                }

                @Override
                public void onProviderDisabled(String s) {                }
            });
        }*/

       Button button_addPlace = (Button) findViewById(R.id.button_addPlace);
       button_addPlace.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MapActivity.this, AddPlaceActivity.class);
               startActivity(intent);
           }
       });

        Button button_search = (Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et_search = (EditText) findViewById(R.id.editText_search);
                String location = et_search.getText().toString();
                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();

                if(! location.equals("")) {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (int i=0; i < addressList.size();i++) {
                        Address myAddress = addressList.get(i);
                        LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                        String string = myAddress.getLocality();
                        markerOptions.position(latLng);
                        mMap.addMarker(markerOptions.title(string));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
            }
        });

        Button button_landmarks = (Button) findViewById(R.id.button_landmarks);
        button_landmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<landmarks.size();i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(Double.parseDouble(landmarks.get(i).getLatitude()),Double.parseDouble(landmarks.get(i).getLongitude()));
                    mMap.addMarker(new MarkerOptions().position(latLng).title(landmarks.get(i).getName()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });

        Button button_restaurants = (Button) findViewById(R.id.button_clubs);
        button_restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<restaurants.size();i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(Double.parseDouble(restaurants.get(i).getLatitude()),Double.parseDouble(restaurants.get(i).getLongitude()));
                    markerOptions.position(latLng);
                    mMap.addMarker(markerOptions.title(restaurants.get(i).getName()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });

        Button button_museums = (Button) findViewById(R.id.button_museums);
        button_museums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<museums.size();i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(Double.parseDouble(museums.get(i).getLatitude()),Double.parseDouble(museums.get(i).getLongitude()));
                    markerOptions.position(latLng);
                    mMap.addMarker(markerOptions.title(museums.get(i).getName()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            restaurants.clear();
            landmarks.clear();
            museums.clear();
            if(dataSnapshot.exists()) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    if(place.category.equals("landmark")) {
                        landmarks.add(place);
                    } else if(place.category.matches("museum")) {
                        museums.add(place);
                    } else if(place.category.matches("restaurant")) {
                        restaurants.add(place);
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        }
        else {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(listPoints.size() == 2) {
                    listPoints.clear();
                    mMap.clear();
                }

                listPoints.add(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if(listPoints.size() == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }
                mMap.addMarker(markerOptions);

                if(listPoints.size() == 2) {
                    String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);
                }
            }
        });
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude +","+origin.longitude;
        String str_dest ="destination="+dest.longitude+","+dest.longitude;
        String sensor = "sensor=false";
        String mode ="mode=driving";
        String param = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output +"?" + param;

        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location"+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyAWRScdD3IjDs8lXT-nYuZ0zWKc80oKlSk");

        return googlePlaceUrl.toString();
    }




    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString ="";

            try {
                responseString = requestDirection(strings[0]);
            }catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }



    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject=null;
            List<List<HashMap<String,String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionParser directionParser = new DirectionParser();
                routes = directionParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for(List<HashMap<String,String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for(HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if(polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            }
            else {
                Toast.makeText(getApplicationContext(), "Direction not fount", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
