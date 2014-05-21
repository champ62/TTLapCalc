package uk.org.champ.ttlapcalc.ttlapcalc;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
//import android.support.v7.app.ActionBarActivity;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //validate the mins and seconds input
        final EditText etMins = (EditText) findViewById(R.id.laptime_mins);
        //final Button btSpeed = (Button) findViewById(R.id.calcSpeed);

        etMins.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Boolean b = validateSecs(etMins);
                }
                return false;
            }

        });

        final EditText etSecs = (EditText) findViewById(R.id.laptime_secs);
        etSecs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Boolean b = validateSecs(etSecs);
                }
                return false;
            }
        });

        //validate the speed input
        final EditText etSpeed = (EditText) findViewById(R.id.speed);
        //final Button btTime = (Button) findViewById(R.id.calcTime);

        etSpeed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Boolean b = validateSpeed (etSpeed);
                }
                return false;
            }
        });
    }

    public boolean validateMins ( EditText et) {
        int iMins;
        String laptimeMins = et.getText().toString();

        if (TextUtils.isEmpty(laptimeMins)) {
            iMins = 0;
            et.setText(String.format("%d", iMins));
        }
        else {
            iMins = Integer.parseInt(laptimeMins);
        }

        if (iMins > 40) {
            et.setError("Too slow!");
            return false;
        } else {
            if (iMins < 16) {
                et.setError("Too fast!");
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean validateSecs (EditText et) {
        double dSecs;
        String laptimeSecs = et.getText().toString();

        if (TextUtils.isEmpty(laptimeSecs)) {
            dSecs = 0;
            et.setText(String.format("%.2f", dSecs));
        }
        else {
            dSecs = Double.valueOf(laptimeSecs);
        }
        if (dSecs >= 60) {
            et.setError("Only 60 seconds in a minute!");
            return false;
        }
        else {
            if (dSecs < 0) {
                et.setError("No negative numbers, please");
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean validateSpeed (EditText et) {
        double dSpeed;
        String speed = et.getText().toString();

        if (TextUtils.isEmpty(speed)) {
            dSpeed = 0;
            et.setText(String.format("%.2f", dSpeed));
        }
        else {
            dSpeed = Double.valueOf(speed);
        }

        if (dSpeed > 141.49) {
            et.setError("Too fast!");
            return false;
        }
        else
        {
            if (dSpeed < 56.60) {
                et.setError("Too slow!");
                return false;
            }
            else {
                return true;
            }
        }
    }

/*     @Override
       public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }*/

    public void calcSpeed (View view ) {
        EditText etMins = (EditText) findViewById(R.id.laptime_mins);
        EditText etSecs = (EditText) findViewById(R.id.laptime_secs);
//        Button btSpeed = (Button)   findViewById(R.id.calcSpeed);
        if ((validateMins(etMins)) && (validateSecs(etSecs)))
        {
            String laptime_mins = etMins.getText().toString();
            String laptime_secs = etSecs.getText().toString();

            int iMins = Integer.parseInt(laptime_mins);
            double dSecs;
            if (TextUtils.isEmpty(laptime_secs)) {
                dSecs = 0;
            }
            else {
                dSecs = Double.valueOf(laptime_secs);
            }
            double totalSecs = (iMins * 60) + dSecs;

            //calc lap speed
            double speed = (37.73 / totalSecs) * 3600;

            //write it
            EditText etSpeed = (EditText) findViewById(R.id.speed);
            etSpeed.setText(String.format("%.2f", speed));
        }
    }

    public void calcTime (View view ) {
        EditText etSpeed = (EditText) findViewById(R.id.speed);
        if (validateSpeed (etSpeed))
        {
            String speed = etSpeed.getText().toString();

            //convert speed string
            double dSpeed = Double.valueOf(speed);

            // calc laptime
            double totalSecs = ((3600 * 37.73) / dSpeed);
            int iMins = (int) totalSecs / 60;
            double iSecs = totalSecs - (iMins * 60);

            String strLaptime_mins = String.format("%02d", iMins);
            String strLaptime_secs = String.format("%2.2f", iSecs);

            //write it
            EditText etLaptime_mins = (EditText) findViewById(R.id.laptime_mins);
            etLaptime_mins.setText(strLaptime_mins);
            EditText etLaptime_secs = (EditText) findViewById(R.id.laptime_secs);
            etLaptime_secs.setText(strLaptime_secs);
        }
    }

    public void clearMins (View view) {
        EditText etMins = (EditText) findViewById(R.id.laptime_mins);
    }

    public void clearSecs (View view) {
        EditText etSecs = (EditText) findViewById(R.id.laptime_secs);
        clearField(etSecs);
    }

    public void clearSpeed (View view) {
        EditText etSecs = (EditText) findViewById(R.id.speed);
        clearField(etSecs);
    }

    public void clearField (EditText et) {
        et.setText("");
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

}
