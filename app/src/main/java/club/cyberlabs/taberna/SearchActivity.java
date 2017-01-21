package club.cyberlabs.taberna;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static club.cyberlabs.taberna.LoginActivity.hostIP;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.ListItemClickListener{

    SearchAdapter searchAdapter;
    RecyclerView recyclerView;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView=(RecyclerView)findViewById(R.id.rv_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter=new SearchAdapter(this);
        recyclerView.setAdapter(searchAdapter);
        String key=getIntent().getStringExtra("search-key");
        URL url=null;
        try{
            url=new URL(hostIP+"search/"+key);
        }
        catch (MalformedURLException m){}
        get(url);

    }

    public void get(final URL url){

        class SearchTask extends AsyncTask<Void,Void,Void> {

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
                JSONArray jsonArray=null;
                try{
                    jsonArray=jsonObject.getJSONArray("array");
                }catch(JSONException j){}
                if(jsonArray.length()>0){
                    searchAdapter.setData(jsonArray);
                }
                else {
                    Toast.makeText(SearchActivity.this,"No Items Found",Toast.LENGTH_LONG).show();
                }
            }
        }
        SearchTask task=new SearchTask();
        task.execute();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent=new Intent(SearchActivity.this,DetailActivity.class);
        JSONObject object=null;
        try{
            object=jsonObject.getJSONArray("array").getJSONObject(clickedItemIndex);
        }catch(JSONException j){}
        intent.putExtra("json",object.toString());
        startActivity(intent);
    }
}
