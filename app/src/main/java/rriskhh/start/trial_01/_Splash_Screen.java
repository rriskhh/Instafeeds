package rriskhh.start.trial_01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.Parse;
import com.parse.ParseACL;
import rriskhh.start.trial_01.Main._Main_Screen;

public class _Splash_Screen extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    final Handler handler = new Handler();
    final int time = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Hide Action-Bar
        getSupportActionBar().hide();

        //Initialisation
        context = this;
        final Intent intent = new Intent(context,_Main_Screen.class);
        parseConnectivity();

        //Check first run
        preferences = getSharedPreferences("rriskhh.start.trial_01",MODE_PRIVATE);
        if(preferences.getBoolean("first_time",true)){
            preferences.edit().putBoolean("first_time",false).commit();
        }

        //Delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },time);
    }

    /*
     ** Parse Connectivity
     */
    public void parseConnectivity(){
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                        .applicationId("instafeed4J345NJNNNIN23232ninfinf")
                        .clientKey(null)
                        .server("https://instafeed-01.herokuapp.com/parse/")
                        .build()
        );
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}
