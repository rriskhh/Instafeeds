package rriskhh.start.trial_01.Main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.parse.ParseUser;

import rriskhh.start.trial_01.R;
import rriskhh.start.trial_01.Lists._List_Screen;
import rriskhh.start.trial_01.Lists._Bookmark_List;
import rriskhh.start.trial_01.Account._LoginOrRegister;
import rriskhh.start.trial_01.Account._Account;

public class _Main_Screen extends AppCompatActivity {

    TextView accountOrLogin;
    Context context;
    Intent intent;
    Bundle bundle;
    final String KEY_CATEGORY = "category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Hide Action Bar
        getSupportActionBar().hide();

        //Initialisation
        context = getApplicationContext();
        intent = new Intent(context,_List_Screen.class);
        bundle = new Bundle();
        accountOrLogin = (TextView) findViewById(R.id.accountOrLogin);

        /*
        *
        * Set Account Text
        *
         */
        if(ParseUser.getCurrentUser() != null)
            accountOrLogin.setText("Account");
        //Mobile Ad Initialisation
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7599807818943725~4083193091");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    public void world(View v){
        bundle.putString(KEY_CATEGORY,"World");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void business(View v){
        bundle.putString(KEY_CATEGORY,"Business");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void sports(View v){
        bundle.putString(KEY_CATEGORY,"Sports");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void technology(View v){
        bundle.putString(KEY_CATEGORY,"Technology");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void health(View v){
        bundle.putString(KEY_CATEGORY,"Health");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void movies(View v){
        bundle.putString(KEY_CATEGORY,"Movies");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void bookmarks(View v){
        Intent i = new Intent(getApplicationContext(),_Bookmark_List.class);
        startActivity(i);
    }

    public void custom(View v){
        bundle.putString(KEY_CATEGORY,"Custom");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void loginOrRegister(View v){
        Intent i = new Intent(context,_LoginOrRegister.class);;
        if(ParseUser.getCurrentUser() != null){
            i = new Intent(context,_Account.class);
        }
        startActivity(i);
    }
}
