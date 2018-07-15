package joy.common;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.cast.framework.media.NotificationOptions;

public class JoyLocationUtil {
    private static final String TAG = "JoyLocationUtil";
    private static JoyLocationUtil instance = null;
    private boolean gpsLocationActivated = false;
    private Location lastLocation = null;
    private LocationManager lm = null;
    protected LocationListenerAdaptor mGpsLocationListener;
    protected LocationListener mLocationReceiver;
    protected LocationListenerAdaptor mNetworkLocationListener;
    private Activity mainActivity;
    private Context mainContext;
    private boolean networkLocationActivated = false;

    private class LocationListenerAdaptor implements LocationListener {
        private LocationListenerAdaptor() {
        }

        public void onLocationChanged(Location location) {
            JoyLocationUtil.this.lastLocation = location;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(JoyLocationUtil.this.bestProvider())) {
                JoyLocationUtil.this.lastLocation = JoyLocationUtil.this.lm.getLastKnownLocation(provider);
            }
        }

        public void onProviderEnabled(String a) {
        }

        public void onProviderDisabled(String a) {
        }
    }

    private JoyLocationUtil(Context context) {
        this.mainContext = context;
        this.lm = (LocationManager) this.mainContext.getSystemService("location");
    }

    public static JoyLocationUtil getInstance(Context context) {
        if (instance == null) {
            instance = new JoyLocationUtil(context);
        }
        return instance;
    }

    public boolean isGPSAvailable() {
        return this.lm.isProviderEnabled("gps");
    }

    public boolean isWifiOrCellIDLocationAvailable() {
        return this.lm.isProviderEnabled("network");
    }

    public Location getLastLocation() {
        return this.lastLocation;
    }

    public synchronized void start() {
        Log.d(TAG, "LocationHandler Start");
        if (!this.networkLocationActivated && isWifiOrCellIDLocationAvailable()) {
            this.networkLocationActivated = true;
            this.mNetworkLocationListener = new LocationListenerAdaptor();
            this.lm.requestLocationUpdates("network", 120000, 10.0f, this.mNetworkLocationListener);
        }
        if (!this.gpsLocationActivated && isGPSAvailable()) {
            this.gpsLocationActivated = true;
            this.mGpsLocationListener = new LocationListenerAdaptor();
            this.lm.requestLocationUpdates("gps", 120000, 10.0f, this.mGpsLocationListener);
        }
        try {
            this.lastLocation = this.lm.getLastKnownLocation(bestProvider());
        } catch (Exception e) {
            Log.e(TAG, "Error getting the first location");
        }
    }

    public synchronized void stop() {
        Log.v(TAG, "LocationHandler Stop");
        try {
            this.lm.removeUpdates(this.mGpsLocationListener);
            this.networkLocationActivated = false;
            this.mGpsLocationListener = null;
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Ignoring: " + e.getMessage());
        }
        try {
            this.lm.removeUpdates(this.mNetworkLocationListener);
            this.gpsLocationActivated = false;
            this.mNetworkLocationListener = null;
        } catch (IllegalArgumentException e2) {
            Log.v(TAG, "Ignoring: " + e2.getMessage());
        }
    }

    protected String bestProvider() {
        if (this.networkLocationActivated && isBestProvider(this.lm.getLastKnownLocation("network"))) {
            return "network";
        }
        if (this.gpsLocationActivated) {
            return "gps";
        }
        return null;
    }

    private boolean isBestProvider(Location myLocation) {
        if (myLocation == null) {
            return false;
        }
        String myProvider = myLocation.getProvider();
        boolean gpsCall = myProvider.equalsIgnoreCase("gps");
        boolean networkCall = myProvider.equalsIgnoreCase("network");
        float gpsAccuracy = Float.MAX_VALUE;
        long gpsTime = 0;
        if (this.gpsLocationActivated) {
            Location lastGpsLocation = this.lm.getLastKnownLocation("gps");
            if (lastGpsLocation != null) {
                gpsAccuracy = lastGpsLocation.getAccuracy();
                gpsTime = lastGpsLocation.getTime();
            }
        }
        float networkAccuracy = Float.MAX_VALUE;
        if (this.networkLocationActivated) {
            Location lastNetworkLocation = this.lm.getLastKnownLocation("network");
            if (lastNetworkLocation != null) {
                networkAccuracy = lastNetworkLocation.getAccuracy();
            }
        }
        float currentAccuracy = myLocation.getAccuracy();
        long currentTime = myLocation.getTime();
        boolean case1 = gpsCall && !this.networkLocationActivated;
        boolean case2 = gpsCall && this.networkLocationActivated && currentAccuracy < networkAccuracy;
        boolean case3 = networkCall && !this.gpsLocationActivated;
        boolean case4 = networkCall && this.gpsLocationActivated && (currentAccuracy < gpsAccuracy || currentTime > NotificationOptions.SKIP_STEP_THIRTY_SECONDS_IN_MS + gpsTime);
        if (case1 || case2 || case3 || case4) {
            return true;
        }
        return false;
    }
}
