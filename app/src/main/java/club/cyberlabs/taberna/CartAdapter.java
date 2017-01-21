package club.cyberlabs.taberna;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import static club.cyberlabs.taberna.LoginActivity.hostIP;

/**
 * Created by Himanshu on 22-01-2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.GuestViewHolder>{
    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param cursor the db cursor with waitlist data to display
     */
    public CartAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        holder.title.setText(mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.COLUMN_TITLE)));
        holder.sub.setText(mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.COLUMN_SUBTITLE)));
        holder.price.setText(mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.COLUMN_PRICE)));

        new DownloadImageTask(holder.image)
                .execute(hostIP+"public/images/"+mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.COLUMN_IMAGE)));

        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID)));
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView title,sub,price;
        // Will display the party size number
        ImageView image;

        public GuestViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.search_title);
            sub=(TextView)itemView.findViewById(R.id.search_subtitle);
            price=(TextView)itemView.findViewById(R.id.search_price);
            image=(ImageView)itemView.findViewById(R.id.search_image);
        }

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
