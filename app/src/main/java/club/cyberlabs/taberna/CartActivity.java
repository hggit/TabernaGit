package club.cyberlabs.taberna;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
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

public class CartActivity extends AppCompatActivity {
    SQLiteDatabase db;
    CartAdapter adapter;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        RecyclerView rv=(RecyclerView)findViewById(R.id.rv_cart);
        rv.setLayoutManager(new LinearLayoutManager(this));
        CartDbHelper helper=new CartDbHelper(this);
        db=helper.getWritableDatabase();
        adapter=new CartAdapter(this,getCart());
        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                removeItem(id);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                adapter.swapCursor(getCart());
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(rv);
    }

    private Cursor getCart() {
        Cursor cursor= db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        key="";
        for(int i=0;i<cursor.getCount();i++)
        {
            cursor.moveToPosition(i);
            key+="-"+cursor.getString(cursor.getColumnIndex(CartContract.CartEntry.COLUMN_IMAGE));
        }
        return cursor;
    }
    void removeItem(long id)
    {
        db.delete(CartContract.CartEntry.TABLE_NAME, CartContract.CartEntry._ID+"="+id,null);
    }

    public void onConfirm(View view)
    {
        startActivity(new Intent(this,MapActivity.class));
    }
}
