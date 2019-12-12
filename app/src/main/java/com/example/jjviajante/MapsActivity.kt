package com.example.jjviajante

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.jjviajante.model.User

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap;
    private  var locationGPS: Location? = null
    private  var locationNetwork: Location? = null
    private lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private lateinit var user: User;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.setMyLocationEnabled(true);
        mMap.uiSettings.setZoomControlsEnabled(true);

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;

        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if(hasGPS){
            if(hasGPS){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0f,object: LocationListener{
                    override fun onLocationChanged(location: Location?) {
                        if(location != null){
                            locationGPS = location
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }

                })

                val localGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localGPSLocation != null)
                    locationGPS = localGPSLocation
            }


            if(locationGPS != null ) {

                var circle = CircleOptions()


                val self = LatLng(locationGPS!!.latitude, locationGPS!!.longitude)
                // adicionando um circulo em até 3km
                circle.center(self)
                    .radius(3000.0)
                    .strokeWidth(1F)
                    .fillColor(Color.argb(108, 0, 100, 255))
                mMap.addCircle(circle);

                // Adiciona meu marcador
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(self, 13f))
                mMap.addMarker(
                    MarkerOptions()
                        .position(self)
                        .title("Olá " + this.user.nome)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                ).showInfoWindow()


                val academia1 = LatLng(locationGPS!!.latitude -0.02, locationGPS!!.longitude +0.02)
                mMap.addMarker(MarkerOptions().position(academia1).title("Academia 1"))

                val academia2 = LatLng(locationGPS!!.latitude + 0.006, locationGPS!!.longitude +0.01)
                mMap.addMarker(MarkerOptions().position(academia2).title("Academia 2"))

                val academia3 = LatLng(locationGPS!!.latitude + 0.01, locationGPS!!.longitude)
                mMap.addMarker(MarkerOptions().position(academia3).title("Academia 3"))

                val academia4 = LatLng(locationGPS!!.latitude + -0.01, locationGPS!!.longitude)
                mMap.addMarker(MarkerOptions().position(academia4).title("Academia 4"))
            }

        }else{
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }



    }



    override fun onResume() {
        super.onResume()

        this.user = intent.getSerializableExtra("user") as User

        var actionBar = supportActionBar;
        actionBar!!.title = this.user.nome;
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_manu, menu);
        return true
    }

    // Menu da ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == R.id.settings){
            var intent = Intent(this, CadastroActivity::class.java).apply {
                putExtra("user", user)
            }

            startActivity(intent)
            finish()

        } else if (id == R.id.logout){

            val sharedPref = getSharedPreferences("User_preference",MODE_PRIVATE) ?: return false
            sharedPref.edit().clear().commit()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
