package club.cyberlabs.taberna;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import static club.cyberlabs.taberna.LoginActivity.hostIP;

/**
 * Created by Himanshu on 20-01-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultHolder> {

    int count;
    ListItemClickListener clickListener;
    JSONArray jsonArray;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    SearchAdapter(ListItemClickListener listener)
    {
        clickListener=listener;
        count=0;
        setHasStableIds(true);
    }

    public void onCart(View view)
    {

    }

    @Override
    public SearchAdapter.ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.search_result;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ResultHolder viewHolder = new ResultHolder(view);

        count++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ResultHolder holder, int position) {
        if(jsonArray!=null)
        {
            JSONObject object;
            try{
                object=jsonArray.getJSONObject(position);
            new DownloadImageTask((ImageView) holder.itemView.findViewById(R.id.search_image))
                    .execute(hostIP+"public/images/"+object.getString("image"));
                TextView title=(TextView)holder.itemView.findViewById(R.id.search_title);
                title.setText(object.getString("title"));
                TextView sub=(TextView)holder.itemView.findViewById(R.id.search_subtitle);
                sub.setText(object.getString("subtitle"));
                TextView price=(TextView)holder.itemView.findViewById(R.id.search_price);
                price.setText(object.getString("price"));
                if(!object.getBoolean("near"))
                {
                    LinearLayout nearby=(LinearLayout)holder.itemView.findViewById(R.id.near_you);
                    nearby.setVisibility(View.INVISIBLE);
                }
            }catch (JSONException j){}

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(jsonArray==null)
            return 0;
        else
            return jsonArray.length();
    }
    class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView cart;

        ResultHolder(View view)
        {
            super(view);
            view.setOnClickListener(this);
            cart=(ImageView)view.findViewById(R.id.cart);
            cart.setTag(R.drawable.cart_black);
            cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==cart.getId())
            {
                if(R.drawable.cart_black==(Integer)cart.getTag())
                {
                    cart.setImageResource(R.drawable.cart_green);
                    cart.setTag(R.drawable.cart_green);
                }
                else
                {
                    cart.setImageResource(R.drawable.cart_black);
                    cart.setTag(R.drawable.cart_black);
                }
            }
            else
                clickListener.onListItemClick(getAdapterPosition());
        }
    }

    void setData(JSONArray array)
    {
        jsonArray=array;
        notifyDataSetChanged();

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
