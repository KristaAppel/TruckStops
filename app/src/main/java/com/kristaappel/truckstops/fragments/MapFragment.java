package com.kristaappel.truckstops.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kristaappel.truckstops.R;
import com.kristaappel.truckstops.objects.LocationHelper;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback,
        LocationListener{

    private GoogleMap googleMap;
    public static int locationRequestCode = 0x01001;
    private boolean mRequestingUpdates = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        getMapAsync(this);

        final LocationListener locationListener = this;

        // Set up click listener for location zoom button:
        ImageButton locationButton = (ImageButton) getActivity().findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomToLocation();
            }
        });

        // Set up listener for Tracking Mode switch:
        final Switch trackingModeSwitch = (Switch) getActivity().findViewById(R.id.trackingModeSwitch);
        trackingModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trackingModeSwitch.isChecked()){
                    LocationHelper.startLocationUpdates(getActivity(), locationListener);
                    mRequestingUpdates = true;
                }else{
                    LocationHelper.stopLocationUpdates(getActivity(), locationListener);
                    mRequestingUpdates = false;
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
         //   throw new RuntimeException(context.toString()
         //           + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestingUpdates) {
            LocationHelper.stopLocationUpdates(getActivity(), this);
            mRequestingUpdates = false;
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        zoomToLocation();
    }

    @SuppressWarnings({"MissingPermission"}) // Permissions is being checked in locationPermissionCheck()
    private void zoomToLocation() {
        if (googleMap != null) {
            if (locationPermissionCheck()){
                LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
                Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (currentLocation == null){
                    Log.i("TAG", "No last known location.  Starting location updates.");
                    LocationHelper.startLocationUpdates(getActivity(), this);
                    mRequestingUpdates = true;
                }else{
                    Log.i("TAG", "Using last known location.");
                    LatLng myPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    //Zoom to current location & display custom map marker:
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_truck);
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(myPosition).icon(bitmapDescriptor).title("Your Location")); //TODO: custom marker
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 12));
                }
            }
        }
    }

    public boolean locationPermissionCheck(){
        // Check for permission to access location:
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted.  Enable location:
            if (!mRequestingUpdates){
                LocationHelper.startLocationUpdates(getActivity(), this);
                mRequestingUpdates = true;
            }
            //googleMap.setMyLocationEnabled(true);
            return true;
        } else {
            // Request permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, locationRequestCode);
            return false;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        zoomToLocation();
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
