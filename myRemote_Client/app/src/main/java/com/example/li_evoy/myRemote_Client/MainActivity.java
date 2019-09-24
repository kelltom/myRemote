package com.example.li_evoy.myRemote_Client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String IP_REGEX = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
    private static final String PORT_REGEX = "^[1-9]\\d*$";
    private static final int REMOTE_ACTIVITY = 1;
    // Views
    private EditText edtIP;
    private EditText edtPort;
    private CheckBox chkSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* View References */
        edtIP = findViewById(R.id.edt_ip);
        edtPort = findViewById(R.id.edt_port);
        chkSave = findViewById(R.id.chk_save);
        Button btnConnect = findViewById(R.id.btn_connect);
        Button btnHelp = findViewById(R.id.btn_help);

        loadPrefs();

        /* Connect Button Listener */
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        /* Help Button Listener */
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method takes the user input and ensures it is valid. If input is good, launches
     * RemoteActivity where the socket connection is attempted. In the case that connection is
     * unsuccessful, the app will revert back to MainActivity and notify user of the failed attempt.
     */
    private void connect() {

        Boolean isIPValid = true;
        Boolean isPortValid = true;

        // get user input
        String ip = edtIP.getText().toString();
        String port = edtPort.getText().toString();

        if (!ip.matches(IP_REGEX)) {
            edtIP.setError("Must enter valid IP in format: xxx.xxx.xxx.xxx");
            isIPValid = false;
        }

        if (!port.matches(PORT_REGEX)) {
            edtPort.setError("Must enter a valid port number: positive integer");
            isPortValid = false;
        }

        // start RemoteActivity for result to create socket connection
        if (isIPValid && isPortValid) {

            // first, save IP if the user wishes
            savePrefs();

            // open remote activity and try to connect
            Intent intent = new Intent(getApplicationContext(), RemoteActivity.class);
            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            startActivityForResult(intent, REMOTE_ACTIVITY);
        }
    }

    /**
     * Gets stored IP and chkSave's state from SharedPreferences, sets fields accordingly.
     */
    void loadPrefs() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String ip = prefs.getString("saved_ip", null);
        Boolean checked = prefs.getBoolean("save_checked", false);

        edtIP.setText(ip);
        chkSave.setChecked(checked);
    }

    /**
     * Saves checkbox status, and if checked, saves user inputted IP as well.
     */
    void savePrefs() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean isChecked = chkSave.isChecked();
        if (isChecked) {
            editor.putString("saved_ip", edtIP.getText().toString());
        } else {
            editor.putString("saved_ip", null);
        }
        editor.putBoolean("save_checked", isChecked);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REMOTE_ACTIVITY) {
            edtPort.setText(null);
            if (resultCode == RESULT_OK) {
                loadPrefs();
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
            }
            if (resultCode == RESULT_CANCELED) {
                edtIP.setText(null);
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPrefs();
    }
}
