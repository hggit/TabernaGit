package club.cyberlabs.taberna;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import static club.cyberlabs.taberna.LoginActivity.hostIP;

public class DetailActivity extends AppCompatActivity {

    TextView title,sub,price;
    ImageView image;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        title=(TextView)findViewById(R.id.detail_title);
        sub=(TextView)findViewById(R.id.detail_sub);
        price=(TextView)findViewById(R.id.detail_price);
        image=(ImageView)findViewById(R.id.detail_image);
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("json"));
            new DownloadImageTask(image)
                    .execute(hostIP+"public/images/"+jsonObject.getString("image"));
            title.setText(jsonObject.getString("title"));
            sub.setText(jsonObject.getString("subtitle"));

        }catch (JSONException j){}

    }

    public void onAddToCart(View view)
    {

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
