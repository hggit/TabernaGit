package club.cyberlabs.taberna;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static club.cyberlabs.taberna.LoginActivity.hostIP;
import static club.cyberlabs.taberna.LoginActivity.mToast;
import static club.cyberlabs.taberna.LoginActivity.manager;
import static club.cyberlabs.taberna.LoginActivity.number;
import static club.cyberlabs.taberna.LoginActivity.otp_str;
import static club.cyberlabs.taberna.LoginActivity.password;

public class OtpActivity extends Activity {
    EditText etOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_otp);
        etOtp=(EditText)findViewById(R.id.et_otp);
    }
    public void onVerifyOtp(View view){
        if(etOtp.getText().toString().equals(otp_str.substring(0,4))){
            if(getIntent().getBooleanExtra("isForgot",false)){
                ForgotActivity.changeView();
                finish();
            }
            else
            {
                URL url=null;
                try{
                    url=new URL(getIntent().getStringExtra("url"));
                }
                catch (MalformedURLException m){}
                get(url);
            }
        }
        else
            Toast.makeText(this,"Incorrect OTP",Toast.LENGTH_LONG).show();
    }
    public void get(final URL url){

        class PostTask extends AsyncTask<Void,Void,Void> {
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
                    startActivity(new Intent(OtpActivity.this,MainActivity.class));
                    manager.setIsLoggedIn(true);
                    finish();
                }
                else {
                    mToast = Toast.makeText(OtpActivity.this,message, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        }
        PostTask task=new PostTask();
        task.execute();
    }
}
