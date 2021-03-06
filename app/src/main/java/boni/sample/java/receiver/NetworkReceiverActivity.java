package boni.sample.java.receiver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import boni.sample.java.R;

public class NetworkReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_receiver);

        NetworkUtil networkUtil = new NetworkUtil();

        String status = networkUtil.getConnectivityStatusString(getApplicationContext());

        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
    }
}
