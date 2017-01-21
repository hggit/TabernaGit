package club.cyberlabs.taberna;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {
    EditText phone,pass;
    TextView tvForgot,tvChange;
    static Toast mToast;
    Button button;
    static String hostIP="http://172.16.40.135:5000/",number,password,otp_str;
    static PrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager=new PrefManager(this);
        tvForgot=(TextView)findViewById(R.id.forgot);
        tvChange=(TextView)findViewById(R.id.change);
        phone=(EditText)findViewById(R.id.et_phone);
        pass=(EditText)findViewById(R.id.et_pass);
        button=(Button)findViewById(R.id.but);
    }

    public void onButtonClick(View view)
    {
        if(button.getText().toString().equals("SIGN IN"))
            onSignIn();
        else
            onCreateAccount();
    }

    public void onSignIn(){
        number=phone.getText().toString();
        password=pass.getText().toString();
        URL url=null;
        try{
            url=new URL(hostIP+"login/"+password+"-"+number);

        }catch (MalformedURLException m){}
        get(url);
    }

    public void onCreateAccount(){

        number=phone.getText().toString();
        password=pass.getText().toString();
        if(number.length()<13){
            Toast.makeText(this,"Enter 10-digit phone number",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length()<8){
            Toast.makeText(this,"Password must be atleast 8 characters",Toast.LENGTH_LONG).show();
            return;
        }
        int otp=(int)(Math.random()*10000);
        otp_str=otp+"000";

        URL url=null;
        try{
            url=new URL(hostIP+"otp/"+number+"-"+otp_str.substring(0,4));
        }
        catch (MalformedURLException m){}
        get(url);

    }

    public void get(final URL url){

        class PostTask extends AsyncTask<Void,Void,Void>{
            JSONObject jsonObject;
            @Override
            protected Void doInBackground(Void... voids) {
                BufferedReader reader = null;

                try {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //con.setDoOutput(true);
                    con.connect();
                    /*OutputStreamWriter writer = new OutputStreamWriter(
                            con.getOutputStream());
                    writer.write(object.toString());
                    writer.flush();*/
                    //Thread.sleep(1000);
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    jsonObject=new JSONObject(sb.toString());
                    con.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("exception", "" + e.getMessage());
                    return null;
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                boolean success=false;
                String message="";
                try{
                    success=jsonObject.getBoolean("success");
                    message=jsonObject.getString("message");

                }catch(JSONException j){}
                if(success){
                    if(button.getText().toString().equals("SIGN IN"))
                    {
                        manager.setIsLoggedIn(true);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                    else
                    {
                        Intent intent=new Intent(LoginActivity.this,OtpActivity.class);
                        intent.putExtra("url",hostIP+"register/"+password+"-"+number);
                        intent.putExtra("isForget",false);
                        startActivity(intent);
                    }
                }
                else {
                    mToast = Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        }
        PostTask task=new PostTask();
        task.execute();
    }



    public void changeButton(View view){
        if(button.getText().toString().equals("SIGN IN"))
        {
            button.setText("CREATE ACCOUNT");
            tvChange.setText("Already Registered? Sign in.");
            tvForgot.setVisibility(View.INVISIBLE);
        }
        else
        {
            button.setText("SIGN IN");
            tvChange.setText("Don't have an account? Create one.");
            tvForgot.setVisibility(View.VISIBLE);
        }

    }

    public void onForgotPassword(View view)
    {
        startActivity(new Intent(this,ForgotActivity.class));
    }

}

