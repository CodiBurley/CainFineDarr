package com.acm.cainfinedarr;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.BreakIterator;
import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationReq;
    private boolean requestingLocationUpdates = false;
    private String mLastUpdateTime;

    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private TextView mUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initVars();

        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void initVars() {
        mLatitudeText = (TextView) findViewById(R.id.latitude_text);
        mLongitudeText = (TextView) findViewById(R.id.longitude_text);
        mUpdateTime = (TextView) findViewById(R.id.update_time);

        mLocationReq = new LocationRequest();
        setLocationRequestParameters(mLocationReq);
        requestingLocationUpdates = true;
    }

    protected void setLocationRequestParameters(LocationRequest req) {
        req.setInterval(1000);
        req.setInterval(100);
        req.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    /************** Implemented methods ****************************/
    @Override
    public void onConnected(Bundle connectionHint) {
        Toast t = Toast.makeText(this,"onConnected ran", Toast.LENGTH_LONG);
        t.show();
        /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }*/
        if(requestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast t = Toast.makeText(this,"Connection Suspended",Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast t = Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG);
        t.show();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationReq, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
        mUpdateTime.setText(mLastUpdateTime);

    }

    /******* End Implemented Methods ******************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
