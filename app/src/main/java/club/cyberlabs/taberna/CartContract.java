package club.cyberlabs.taberna;

import android.provider.BaseColumns;

/**
 * Created by Himanshu on 21-01-2017.
 */

public class CartContract{
    public static final class CartEntry implements BaseColumns{
        public static final String TABLE_NAME = "cart";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SUBTITLE = "sub";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE = "img";
    }
}
