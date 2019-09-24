package com.example.li_evoy.myRemote_Client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RemoteActivity extends Activity {

    // UI Members
    private Button btnTab;
    private EditText edtInput;
    private Button btnEnter;
    private TextView txtMousePad;
    private Button btnLeft;
    private Button btnRight;
    private ImageButton btnScrollUp;
    private ImageButton btnScrollDown;
    private ImageButton playButton;
    private ImageButton volumeUpButton;
    private ImageButton volumeDownButton;
    private ImageButton btnFullScreen;
    private Button btnNetflix;
    private Button btnYoutube;

    // Remote Specific Members
    private Socket socket;
    private PrintWriter out;

    // MousePad Position Variables
    private float startX = 0;
    private float startY = 0;
    private float endX = 0;
    private float endY = 0;
    private float mouseSpeed = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        /* Store View References */
        getViewReferences();

        // used to avoid warnings affecting compilation
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // get user inputted IP and port from calling activity
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");

        Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_LONG).show();

        // attempt to connect socket
        ConnectionTask connectionTask = new ConnectionTask();
        connectionTask.execute(ip, port);

        /* Set All Button Listeners */
        setBtnListeners();

    }

    private void getViewReferences() {
        btnTab = findViewById(R.id.btn_tab);
        edtInput = findViewById(R.id.edt_input);
        btnEnter = findViewById(R.id.btn_enter);
        txtMousePad = findViewById(R.id.txt_mouse_pad);
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        btnScrollUp = findViewById(R.id.btn_scroll_up);
        btnScrollDown = findViewById(R.id.btn_scroll_down);
        btnNetflix = findViewById(R.id.btn_flix);
        btnYoutube = findViewById(R.id.btn_tube);
        playButton = findViewById(R.id.btn_play);
        volumeUpButton = findViewById(R.id.btn_vol_up);
        volumeDownButton = findViewById(R.id.btn_vol_down);
        btnFullScreen = findViewById(R.id.btn_full_screen);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setBtnListeners() {

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("left");
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("right");
            }
        });

        btnNetflix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("netflix");
            }
        });

        btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("youtube");
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("space");
            }
        });

        volumeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("vol_up");
            }
        });

        volumeDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("vol_down");
            }
        });

        btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("fullscreen");
            }
        });

        btnScrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("scroll_up");
            }
        });

        btnScrollDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (out != null) out.println("scroll_down");
            }
        });

        btnTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (out != null) out.println("tab");
                closeKeyboardIfOpen();
            }
        });

        // Keyboard Input / Enter Listener
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String line = edtInput.getText().toString();
                StringBuilder sb = new StringBuilder();
                sb.append("$$$"); // used on the server end to identify this as keyboard input
                sb.append(line);

                if (out != null) out.println(sb.toString());
                edtInput.setText(null);

                closeKeyboardIfOpen();
            }
        });

        // Mouse Pad Listener
        // Code Reference: http://codesmith.in/control-pc-from-android-app-using-java/
        txtMousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (out != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // save X and Y positions when user touches the MousePad
                            startX = motionEvent.getX();
                            startY = motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            endX = motionEvent.getX() - startX; // mouse movement in X direction
                            endY = motionEvent.getY() - startY; // mouse movement in Y direction
                            // set start coordinates to new position to allow for continuous movement
                            startX = motionEvent.getX();
                            startY = motionEvent.getY();
                            // send movement to server
                            if (endX != 0 || endY != 0)
                                out.println((endX * mouseSpeed) + "," + (endY * mouseSpeed));
                            break;
                    }
                }
                return true;
            }
        });
    }

    /**
     * Close socket and return to MainActivity
     */
    private void disconnect(Result result) {
        if (socket != null && out != null) {
            try {
                // tell server to exit
                out.println("exit");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (result == Result.DISCONNECTED)
            setResult(RESULT_OK, intent);
        else if (result == Result.FAILED)
            setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Minimizes virtual keyboard if open
     * Code Reference: https://stackoverflow.com/questions/3400028/close-virtual-keyboard-on-button-press
     */
    private void closeKeyboardIfOpen() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ?
                    null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Load in user preferences
     */
    @Override
    protected void onResume() {
        super.onResume();

        // get the Shared Preferences that are used by the preference screens
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // update mouseSpeed multiplier used calculating track distance before sending
        // coordinates to server
        String mouseSpeedString = userPrefs.getString((getString(R.string.preference_mouse_speed)), "1");
        mouseSpeed = Float.parseFloat(mouseSpeedString);

    }

    /**
     * Back button overridden to tell server to exit upon going back to login screen
     */
    @Override
    public void onBackPressed() {
        disconnect(Result.DISCONNECTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect(Result.DISCONNECTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(getApplicationContext(),
                    UserPrefsActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_disconnect) {
            disconnect(Result.DISCONNECTED);
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * Declares members to be used by the disconnect() function to determine which Intent
     * result should be returned to the MainActivity upon disconnection. This will help discern
     * which message is most appropriate to be displayed to the user.
     */
    private enum Result {
        DISCONNECTED,   // purposeful disconnection - OK
        FAILED;         // erroneous disconnection - CANCELLED
    }

    /**
     * This task attempts to connect client to server using validated input from user. If a connection
     * fails to establish, returns back to MainActivity.
     */
    public class ConnectionTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            Boolean result = true;

            // attempt to create a socket to inputted IP and port
            try {
                InetAddress serverIP = InetAddress.getByName(params[0]);
                int port = Integer.parseInt(params[1]);
                socket = new Socket();
                // test connection for 5 seconds
                socket.connect(new InetSocketAddress(serverIP.getHostAddress(), port), 5000);
            } catch (IOException e) {
                Log.e("ConnectionError", e.toString());
                result = false;
            }

            // if socket happens to be null, automatically return rather than continuing
            if (socket == null)
                return false;

            // try to get PrintWriter from socket
            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (IOException e) {
                Log.e("OutputStreamError", e.toString());
                result = false;
            }

            // if PrintWriter is null, disconnect
            if (out == null)
                result = false;

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(), "Connection Established", Toast.LENGTH_LONG).show();
            else {
                disconnect(Result.FAILED);
            }
        }
    }
}
