package club.cyberlabs.taberna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    PrefManager manager;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=new PrefManager(this);
        manager.setFirstTimeLaunch(false);
        editText=(EditText)findViewById(R.id.search_key);
        editText.clearFocus();
    }


    public void Gadgets(View view)
    {
        Intent intent = new Intent(getApplicationContext(),CatalogueActivity.class);
        intent.putExtra("ans",0);
        startActivity(intent);
    }
    public void Appliances(View view)
    {
        Intent intent1 = new Intent(getApplicationContext(),CatalogueActivity.class);
        intent1.putExtra("ans",1);
        startActivity(intent1);
    }
    public void Books(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(),CatalogueActivity.class);
        intent2.putExtra("ans",2);
        startActivity(intent2);
    }
    public void Toys(View view)
    {
        Intent intent3 = new Intent(getApplicationContext(),CatalogueActivity.class);
        intent3.putExtra("ans",3);
        startActivity(intent3);
    }
    public void Stationery(View view)
    {
        Intent intent4 = new Intent(getApplicationContext(),CatalogueActivity.class);
        intent4.putExtra("ans",4);
        startActivity(intent4);
    }
    public void Medicine(View view)
    {
        Intent intent5 = new Intent(getApplicationContext(),CatalogueActivity.class);
        intent5.putExtra("ans",5);
        startActivity(intent5);
    }
    public void DailyUse(View view)
    {
        Intent intent6 = new Intent(getApplicationContext(),CatalogueActivity.class);
        intent6.putExtra("ans",6);
        startActivity(intent6);
    }

    public void onSearch(View view)
    {
        Intent intent=new Intent(this,SearchActivity.class);
        intent.putExtra("search-key","search/"+editText.getText().toString());
        startActivity(intent);
    }
}
