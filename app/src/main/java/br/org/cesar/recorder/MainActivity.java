package br.org.cesar.recorder;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = getString(R.string.app_name);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RecorderFragment())
                    .commit();
        }
    }
}
