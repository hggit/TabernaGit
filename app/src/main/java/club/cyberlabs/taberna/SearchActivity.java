package club.cyberlabs.taberna;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView=(RecyclerView)findViewById(R.id.rv_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter=new SearchAdapter(this,this);
        recyclerView.setAdapter(searchAdapter);
        String key=getIntent().getStringExtra("search-key");
        URL url=null;
        try{
            url=new URL(hostIP+"search/"+key);
        }
        catch (MalformedURLException m){}
        get(url);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.Gadgets:
                        Intent intent = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent.putExtra("ans",0);
                        startActivity(intent);
                        return true;
                    case R.id.Appliances:
                        Intent intent1 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent1.putExtra("ans",1);
                        startActivity(intent1);
                        return true;
                    case R.id.Books:
                        Intent intent2 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent2.putExtra("ans",2);
                        startActivity(intent2);
                        return true;
                    case R.id.Toys:
                        Intent intent3 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent3.putExtra("ans",3);
                        startActivity(intent3);
                        return true;
                    case R.id.Office:
                        Intent intent4 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent4.putExtra("ans",4);
                        startActivity(intent4);
                        return true;
                    case R.id.Daily_use:
                        Intent intent5 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent5.putExtra("ans",5);
                        startActivity(intent5);
                        return true;
                    case R.id.Set_Budget:
                        Intent intent6 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent6.putExtra("ans",6);
                        startActivity(intent6);
                        return true;
                    case R.id.My_cart:
                        Intent intent7 = new Intent(getApplicationContext(),CartActivity.class);

                        startActivity(intent7);
                        return true;
                    case R.id.Logout:
                        Intent intent8 = new Intent(getApplicationContext(),LoginActivity.class);

                        startActivity(intent8);
                        return true;

                }
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                Intent i=new Intent(getApplicationContext(),SearchActivity.class);
                i.putExtra("search-key",query);

                startActivity(i);
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;


            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);


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
