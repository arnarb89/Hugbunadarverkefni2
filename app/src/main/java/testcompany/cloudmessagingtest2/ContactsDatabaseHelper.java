package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

/**
 * Created by Notandi on 04-Mar-17.
 */

public class ContactsDatabaseHelper extends SQLiteOpenHelper {

    private static final int mDATABASE_VERSION = 1;
    private static final String mDATABASE_NAME = "contacts_manager";

    private static final String mTABLE_CONTACTS = "contacts";

    private static final String mKEY_USER_ID = "id";
    private static final String mKEY_NAME = "name";
    private static final String mKEY_BLOCKED = "blocked";

    public ContactsDatabaseHelper(Context context) {
        super(context, mDATABASE_NAME, null, mDATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE" + mTABLE_CONTACTS
                + "("
                + mKEY_USER_ID + "STRING PRIMARY KEY,"
                + mKEY_NAME + " TEXT, "
                + mKEY_BLOCKED + " BOOLEAN"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + mTABLE_CONTACTS);

        onCreate(db);
    }
}
