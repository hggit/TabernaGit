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
    }

    public void showCatalogue(View view)
    {
        startActivity(new Intent(this,CatalogueActivity.class));
    }

    public void onSearch(View view)
    {
        Intent intent=new Intent(this,SearchActivity.class);
        intent.putExtra("search-key",editText.getText().toString());
        startActivity(intent);
    }
}
