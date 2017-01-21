package club.cyberlabs.taberna;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Budget extends Activity {
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget);
        et=(EditText)findViewById(R.id.budget);
    }

    public void onSetBudget(View view)
    {
        finish();
    }
}
