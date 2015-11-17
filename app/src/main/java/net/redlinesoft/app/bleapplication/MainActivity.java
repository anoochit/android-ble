package net.redlinesoft.app.bleapplication;


import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.DecimalFormat;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    BeaconManager beaconManager;
    TextView txtRange,txtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtRange= (TextView) findViewById(R.id.txtRange);
        txtId = (TextView) findViewById(R.id.textId);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                for (Beacon beacon : collection) {
                    DecimalFormat df = new DecimalFormat("###.##");
                    logToDisplay(df.format(beacon.getDistance()),beacon.getId1().toString()+"\n"+beacon.getBluetoothAddress());
                    Log.d(TAG, df.format(beacon.getDistance()));
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    private void logToDisplay(final String format, final String id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtRange.setText(format);
                txtId.setText(id);
            }
        });
    }


}
