package com.kristaappel.truckstops.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kristaappel.truckstops.R;
import com.kristaappel.truckstops.objects.NetworkMonitor;
import com.kristaappel.truckstops.objects.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.radius;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends android.app.Fragment {

    private String radiusToSearch = "100";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton searchButton = (ImageButton)getActivity().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForTruckStops();
            }
        });
    }


    private void searchForTruckStops(){
        final ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);

        // Check for internet connection:
        if (!NetworkMonitor.deviceIsConnected(getActivity())){
            Toast.makeText(getActivity(), "No connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar:
        if (progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }


        final String url = "http://webapp.transflodev.com/svc1.transflomobile.com/api/v3/stations/" + radiusToSearch;
////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SearchFragment:127", "response: " + response);
                //makeJobList(response); //TODO: make truck stop list, display truck stops on map
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("SearchFragment:133", "That didn't work!!!!!!!");
                Log.i("SearchFragment:134", error.toString());
                error.printStackTrace();
                // Hide progress bar:
                if (progressBar != null){
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if (error.networkResponse.statusCode == 404){
                    Toast.makeText(getActivity(), "No data available.", Toast.LENGTH_SHORT).show();
            //        ArrayList<Job> emptyJobs = new ArrayList<>(); //TODO: remove this line and do something else??
            //        showJobs(getActivity(),emptyJobs); //TODO: remove this line and do something else??
                    // Hide progress bar:
                    if (progressBar != null){
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String authString = "amNhdGFsYW5AdHJhbnNmbG8uY29tOnJMVGR6WmdVTVBYbytNaUp6RlIxTStjNmI1VUI4MnFYcEVKQzlhVnFWOEF5bUhaQzdIcjVZc3lUMitPTS9paU8=";
                String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
             //   params.put("Authentication", authString);
                params.put("Authentication", "Basic " + authString);
                //params.put("lat", "28.2621200");
               // params.put("lng", "-82.3777330");
               // params.put("radius", "100");
                return params;
            }

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("lat", "28.2621200");
//                 params.put("lng", "-82.3777330");
//                 params.put("radius", "100");
//                return params;
//            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
///////////////////////////
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
