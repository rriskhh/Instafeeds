package rriskhh.start.trial_01.Account;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import java.util.List;

import rriskhh.start.trial_01.R;

public class _LoginOrRegister extends AppCompatActivity {

        /*
        *** L -> true
        *** R -> false
         */
    static boolean type_log_reg = true;
    String forgetEmail = "";
    TextView text1;
    TextView text2;
    Button logreg;
    EditText username;
    EditText email;
    EditText pwd;
    EditText cpwd;
    TextView error;
    TextView forget;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        //Change Title
        getSupportActionBar().setTitle("Login");

        //Initialisation
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        cpwd = (EditText) findViewById(R.id.cpwd);
        logreg = (Button) findViewById(R.id.logreg);
        forget = (TextView) findViewById(R.id.forget);
        error = (TextView) findViewById(R.id.error);
        context = getApplicationContext();
    }

    /*
    *
    * Login Option Selected
    *
     */
    public void logIn(View v){
        type_log_reg = true;
        //Change Title
        getSupportActionBar().setTitle("Login");
        //Change Option
        text1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        text2.setTextColor(getResources().getColor(R.color.colorAccent));
        error.setText("");
        /*
        ** Change Visibility
         */
        forget.setVisibility(TextView.VISIBLE);
        email.setVisibility(EditText.GONE);
        cpwd.setVisibility(EditText.GONE);
        logreg.setText(" Log In ");

    }

    /*
    *
    * Forget Password Option Selected
    *
     */
    public void forgetPwd(View v){
        /*
        *
        * Get Email for sending the rest link
        *
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        getEmail(builder);
        if(!forgetEmail.isEmpty()){
            ParseUser.requestPasswordResetInBackground(forgetEmail, new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(context,"Rest Link sent to your mail.",Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
            });
        }
    }

    /*
    *
    * Get Email through AlertDialog
    *
     */
    private void getEmail(AlertDialog.Builder builder){
        builder.setTitle("Forget Password ?");

        /*
        *
        * Set up Edit Text
        *
         */
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("  Email Address Here.");
        builder.setView(input);

        /*
        *
        * Set up Buttons
        *
         */
        // OK button
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().toLowerCase().matches("^(.+)@(.+).(.+)")){
                    Toast.makeText(context,"Invalid Email",Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                forgetEmail = input.getText().toString().toLowerCase();
            }
        });
        // Cancel Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /*
    *
    * Registration Option Selected
    *
     */
    public void register(View v){
        type_log_reg = false;
        //Change Title
        getSupportActionBar().setTitle("Register");
        //Change Options
        text1.setTextColor(getResources().getColor(R.color.colorAccent));
        text2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        error.setText("Password should be of min. 6 characters.");
        /*
        ** Change Visibility
         */
        forget.setVisibility(TextView.GONE);
        email.setVisibility(EditText.VISIBLE);
        cpwd.setVisibility(EditText.VISIBLE);
        logreg.setText(" Register ");
    }

    public void logregFunction(View v){
        if(username.getText().toString() == null || username.getText().toString().isEmpty()){
            error.setText("Username not Entered.");
            return;
        }

        if(!type_log_reg){
            if(username.getText().toString().length() < 4){
                error.setText("Username should contain atleast 4 characters.");
                return;
            }
            if(!email.getText().toString().toLowerCase().matches("^(.+)@(.+).(.+)")){
                error.setText("Invalid Email.");
                return;
            }
        }

        if(pwd.getText().toString().trim().length() < 6){
            error.setText("Invalid Password.");
            return;
        }
        if(!type_log_reg){
            if(!pwd.getText().toString().equals(cpwd.getText().toString())){
                error.setText("Passwords do not match.");
                return;
            }

        }
        /*
        ** Register or Log in user.
         */
        /*
        ** Log In
         */
        if (type_log_reg) {
            ParseUser.logInInBackground(username.getText().toString(), pwd.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Toast.makeText(context, "Login Successful.", Toast.LENGTH_SHORT)
                                .show();
                        finish();
                    } else {
                        Toast.makeText(context, "Invalid Details.", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
            return;
        }
        /*
        ** Register
         */
        if(!type_log_reg){
            /*
            Parse Connection;
             */

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("email",email.getText().toString().toLowerCase());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e == null){
                        if(objects.size() > 0){
                            Toast.makeText(context,"User Already exists.",Toast.LENGTH_SHORT)
                                    .show();
                            email.setText("");
                            return;
                        }else{
                            /*
                            *
                            * Signup User
                            *
                             */
                            ParseUser user = new ParseUser();
                            user.setUsername(username.getText().toString());
                            user.setEmail(email.getText().toString());
                            user.setPassword(pwd.getText().toString());
                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Toast.makeText(context,"Signup Successful",Toast.LENGTH_SHORT)
                                                .show();
                                        finish();
                                    }else
                                        Toast.makeText(context,"Connection Problems.",Toast.LENGTH_SHORT)
                                                .show();
                                }
                            });
                        }

                    }else
                        Toast.makeText(context,"Connection Problems.",Toast.LENGTH_SHORT)
                                .show();
                }
            });
            return;
        }
    }

}
