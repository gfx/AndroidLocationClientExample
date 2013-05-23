package com.example.locationexample;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

// https://developers.google.com/maps/documentation/android/location

public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

	private LocationClient locationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// check Google Play service APK is available and up to date.
		// see http://developer.android.com/google/play-services/setup.html
		final int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (result != ConnectionResult.SUCCESS) {
			Toast.makeText(this, "Google Play service is not available (status=" + result + ")", Toast.LENGTH_LONG).show();
			finish();
		}

		locationClient = new LocationClient(this, this, this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		locationClient.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();

		locationClient.disconnect();
	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();

		Location loc = locationClient.getLastLocation();
		Log.d("XXX", "location=" + loc.toString());

		Geocoder geocoder = new Geocoder(this);
		try {
			List<Address> result = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

			TextView textView = (TextView)findViewById(R.id.address);
			textView.setText(addressToText(result.get(0)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private CharSequence addressToText(Address address) {
		final StringBuilder addressText = new StringBuilder();
		for (int i = 0, max = address.getMaxAddressLineIndex(); i < max; ++i) {
			addressText.append(address.getAddressLine(i));
			if ((i+1) < max) {
				addressText.append(", ");
			}
		}
		addressText.append(", ");
		addressText.append(address.getCountryName());
		return addressText;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
	}
	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected", Toast.LENGTH_LONG).show();
	}


}
