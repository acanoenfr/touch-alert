package fr.acanoen.touchalert;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Dash extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    //composants de l'interface
    private ImageButton event, danger, solde, medical, cata, more;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        // le bouton event
        event = findViewById(R.id.event);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // traitement, envoie l'evenement et la geolocalisation
                sendGeo();
            }
        });

        //le bouton danger
        danger = findViewById(R.id.danger);
        danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //localisation
                sendGeo();
            }
        });

        //le bouton solde
        solde = findViewById(R.id.solde);
        solde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // localisation methode
                sendGeo();
            }
        });

        //le bouton medical
        medical = findViewById(R.id.medical);
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //localisation methode
                sendGeo();
            }
        });

        //le bouton cata
        cata = findViewById(R.id.cata);
        cata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //localisation methode
                sendGeo();
            }
        });

        //le bouton more
        more = findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGeo();
            }
        });
    }

    // envoie la localisation
    private void sendGeo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // TODO
        } else {
        // Show rationale and request permission.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

            }
        }
    }
}
