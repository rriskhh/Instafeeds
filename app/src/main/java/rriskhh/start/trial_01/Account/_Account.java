package rriskhh.start.trial_01.Account;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import rriskhh.start.trial_01.DatabaseHandler._Database_Tables;
import rriskhh.start.trial_01.Parser._RSSParser;

import rriskhh.start.trial_01.R;

public class _Account extends AppCompatActivity {

    TextView welcome;
    TextView disEMAIL;
    PopupWindow changePwdWindow;
    ListView lv;
    Context context;
    _Database_Tables tables;
    _RSSParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Change Title
        getSupportActionBar().setTitle("Account");

        //Initalisation
        context = getApplicationContext();
        tables = new _Database_Tables(context);
        parser = new _RSSParser();
        welcome = (TextView) findViewById(R.id.welcome);
        disEMAIL = (TextView) findViewById(R.id.disEMAIL);
        lv = (ListView) findViewById(R.id.subsListview);
        welcome.setText("Welcome " + ParseUser.getCurrentUser().getUsername() + " ,");
        disEMAIL.setText(ParseUser.getCurrentUser().getEmail());
        new SubscriptionList().execute();
    }

    /*
    *
    * Log out current User
    *
     */
    public void logOut(View v){
        ParseUser.logOut();
        tables.deleteAllLinks();
        Toast.makeText(context, "Logging out.", Toast.LENGTH_SHORT)
                .show();
        finish();
    }

    public void changePassword(View v){
        initiateWindow();
    }

    /*
    *
    * Change Password Popup window
    *
     */
    private void initiateWindow(){
        try{
            LayoutInflater inflater = (LayoutInflater) _Account.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.change_password, (ViewGroup)findViewById(R.id.popUp));
            changePwdWindow = new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
            changePwdWindow.showAtLocation(layout, Gravity.CENTER,0,0);
            final Button change = (Button) layout.findViewById(R.id.changePwd);
            final Button cancel = (Button) layout.findViewById(R.id.cancel);
            final EditText oldPwd  = (EditText) layout.findViewById(R.id.oldPwd);
            final EditText newPwd  = (EditText) layout.findViewById(R.id.newPwd);
            final EditText cnewPwd  = (EditText) layout.findViewById(R.id.cnewPwd);
            /*
            *
            * Close Popup
            *
             */
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePwdWindow.dismiss();
                }
            });
            /*
            *
            * Change Password
            *
             */
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    *
                    * Check if All fields filled
                    *
                     */
                    if(oldPwd.getText() == null || oldPwd.getText().toString().isEmpty()
                            || newPwd.getText() == null || newPwd.getText().toString().isEmpty()
                            || cnewPwd.getText() == null || cnewPwd.getText().toString().isEmpty()){
                        Toast.makeText(context,"Please fill all the fields.",Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    if(!newPwd.getText().toString().equals(cnewPwd.getText().toString())){
                        Toast.makeText(context,"Passwords do not match.",Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    if(newPwd.getText().toString().length() < 6){
                        Toast.makeText(context,"Invalid Password(Min.6 chars)",Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    /*
                    *
                    * Check if old password entered is correct
                    *
                     */
                    ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername().toString(),
                            oldPwd.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if(user == null){
                                        Toast.makeText(context,"Incorrect Password.",Toast.LENGTH_SHORT)
                                                .show();
                                        return;
                                    }else{
                                        /*
                                        *
                                        * Password matches, Makes final Changes
                                        *
                                         */
                                        ParseUser.getCurrentUser().setPassword(newPwd.getText().toString());
                                        Toast.makeText(context,"Password Changed.",Toast.LENGTH_SHORT)
                                                .show();
                                        changePwdWindow.dismiss();
                                        return;
                                    }
                                }
                            });
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
    *
    *
    * GetAndDisplaySubscriptionList
    *
    *
     */
    class SubscriptionList extends AsyncTask<String,String,String> {

        List<String> listArray;
        @Override
        protected String doInBackground(String... params) {
            List<String> list = new ArrayList<String>();
            listArray = new ArrayList<String>();
            list = tables.getAllLinks();
            /*
            *
            * Check if any feeds subscribed
            *
             */
            if(list.size() > 0){
                for(String link : list){
                    //Log.d("Checkpoint",link);
                    listArray.add(parser.getLinkTitle(link));
                }
            }
            /*
            *
            * No subscription
            *
             */
            if(listArray.size() == 0) {
                listArray.add("No subscriptions yet.");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,listArray);
                    lv.setAdapter(adapter);
                }
            });
            return null;
        }
    }
}
