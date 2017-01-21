package club.cyberlabs.taberna;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import static club.cyberlabs.taberna.LoginActivity.otp_str;

public class ForgotActivity extends AppCompatActivity {
    static Button button;
    static EditText etNum,etPass;
    static TextView tvPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        button=(Button)findViewById(R.id.but);
        etNum=(EditText)findViewById(R.id.et_phone);
        etPass=(EditText)findViewById(R.id.et_pass);
        tvPass=(TextView)findViewById(R.id.tv_pass);
    }

    public void onButtonClick(View view)
    {
        if(button.getText().toString().equals("RESET PASSWORD"))
            onResetPassword();
        else
            onConfirm();
    }

    void onResetPassword()
    {

        String number=etNum.getText().toString();
        int otp=(int)(Math.random()*10000);
        otp_str=otp+"000";
        URL url=null;
        try{
            url=new URL(hostIP+"resetpassword/"+number+"-"+otp_str.substring(0,4));
        }
        catch (MalformedURLException m){}
        get(url);
    }

    void onConfirm()
    {
        String number=etNum.getText().toString();
        String newPass=etPass.getText().toString();
        if(newPass.length()<8){
            Toast.makeText(this,"Password must be atleast 8 characters",Toast.LENGTH_LONG).show();
            return;
        }
        URL url=null;
        try{
            url=new URL(hostIP+"changepass/"+newPass+"-"+number);
        }
        catch (MalformedURLException m){}
        get(url);
    }

    static void changeView()
    {
        etPass.setVisibility(View.VISIBLE);
        tvPass.setVisibility(View.VISIBLE);
        button.setText("CONFIRM");
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
                    if(button.getText().toString().equals("CONFIRM"))
                    {
                        startActivity(new Intent(ForgotActivity.this,MainActivity.class));
                        manager.setIsLoggedIn(true);
                        finish();
                    }
                    else
                    {
                        Intent intent=new Intent(ForgotActivity.this,OtpActivity.class);
                        intent.putExtra("isForgot",true);
                        startActivity(intent);
                    }
                }
                else {
                    mToast = Toast.makeText(ForgotActivity.this,message, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        }
        PostTask task=new PostTask();
        task.execute();
    }
}
