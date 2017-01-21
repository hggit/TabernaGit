package club.cyberlabs.taberna;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Himanshu on 21-01-2017.
 */

public class CartDbHelper extends SQLiteOpenHelper{
    private static final String name="cart.db";
    public CartDbHelper(Context context)
    {
        super(context, name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + CartContract.CartEntry.TABLE_NAME + " (" +
                CartContract.CartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CartContract.CartEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                CartContract.CartEntry.COLUMN_SUBTITLE + " TEXT NOT NULL, " +
                CartContract.CartEntry.COLUMN_PRICE + " TEXT NOT NULL" +
                CartContract.CartEntry.COLUMN_IMAGE + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
