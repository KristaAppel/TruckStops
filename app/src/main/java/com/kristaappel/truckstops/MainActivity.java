package com.kristaappel.truckstops;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kristaappel.truckstops.fragments.MapFragment;
import com.kristaappel.truckstops.fragments.SearchFragment;

import static android.os.Build.VERSION_CODES.M;
import static com.kristaappel.truckstops.fragments.MapFragment.locationRequestCode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create and display a SearchFragment:
        SearchFragment searchFrag = new SearchFragment();
        getFragmentManager().beginTransaction().replace(R.id.searchFrame, searchFrag).commit();

        MapFragment mapFrag = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.mapFrame, mapFrag).commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MapFragment.locationRequestCode){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission granted.
                MapFragment mapFrag = new MapFragment();
                getFragmentManager().beginTransaction().replace(R.id.mapFrame, mapFrag).commit();
            }else{
                if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}
