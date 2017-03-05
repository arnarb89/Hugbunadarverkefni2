package testcompany.cloudmessagingtest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;

public class ContactsDatabaseHelper extends SQLiteOpenHelper {

    private static final int mDATABASE_VERSION = 1;
    private static final String mDATABASE_NAME = "contacts_manager";

    private static final String mTABLE_CONTACTS = "contacts";
    private static final String mTABLE_REQUESTS = "contacts";

    private static final String mKEY_USERID = "id";
    private static final String mKEY_USERNAME = "username";
    private static final String mKEY_BLOCKED = "blocked";

    private static final String mCREATE_CONTACTS_TABLE =
            "CREATE TABLE IF NOT EXISTS "
            + mTABLE_CONTACTS
            + "("
            + mKEY_USERID + " INTEGER PRIMARY KEY, "
            + mKEY_USERNAME + " TEXT, "
            + mKEY_BLOCKED + " BOOLEAN"
            + ")";
    private static final String mCREATE_REQUESTS_TABLE =
            "CREATE TABLE IF NOT EXISTS "
            + mTABLE_CONTACTS
            + "("
            + mKEY_USERID + " INTEGER PRIMARY KEY, "
            + mKEY_USERNAME + " TEXT, "
            + mKEY_BLOCKED + " BOOLEAN"
            + ")";

    public ContactsDatabaseHelper(Context context) {
        super(context, mDATABASE_NAME, null, mDATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mCREATE_CONTACTS_TABLE);
        db.execSQL(mCREATE_REQUESTS_TABLE);
    }

    public void dropTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + mTABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + mTABLE_REQUESTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        TODO transfer contacts on upgrade
        db.execSQL("DROP TABLE IF EXISTS " + mTABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + mTABLE_REQUESTS);

        onCreate(db);
    }

    private void addContact(String table, Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(mKEY_USERNAME, contact.getUsername());
        values.put(mKEY_USERID, contact.getId());
        values.put(mKEY_BLOCKED, false);

        db.insert(table, null, values);
    }

    private void deleteContact(String table, Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(mKEY_USERNAME, contact.getUsername());
        values.put(mKEY_USERID, contact.getId());
        values.put(mKEY_BLOCKED, contact.isBlocked());

        db.delete(
                table,
                mKEY_USERID + " = ?",
                new String[]{String.valueOf(contact.getId())}
        );
    }

    public void addRequest(Contact contact) {
        addContact(mTABLE_REQUESTS, contact);
    }

    public void addContact(Contact contact) {
        addContact(mTABLE_CONTACTS, contact);
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
            mTABLE_CONTACTS,
            new String[] {mKEY_USERID, mKEY_USERNAME, mKEY_BLOCKED},
            mKEY_USERID + " = ?",
            new String[] {String.valueOf(id)},
            null, null, null, null
        );
        if(cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            parseBoolean(cursor.getString(2))
        );

        cursor.close();
        return contact;
    }

    public List<Contact> getBlockedContacts() {
        return getContacts(true);
    }

    public List<Contact> getNonBlockedContacts() {
        return getContacts(false);
    }

    private List<Contact> getContacts(boolean blocked) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = db.query(
                mTABLE_CONTACTS,
                new String[] {mKEY_USERID, mKEY_USERNAME, mKEY_BLOCKED},
                mKEY_BLOCKED+ " = ?",
                new String[] {String.valueOf(blocked)},
                null, null, null, null
        );

        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        parseBoolean(cursor.getString(2))
                );
                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contacts;
    }

    public List<Contact> getRequests() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + mTABLE_REQUESTS;
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        parseBoolean(cursor.getString(2))
                );
                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contacts;
    }

    public void acceptRequest(Contact contact) {
        deleteContact(mTABLE_REQUESTS, contact);
        addContact(mTABLE_CONTACTS, contact);
    }

    public void declineRequest(Contact contact) {
        deleteContact(mTABLE_REQUESTS, contact);
    }

    public void deleteContact(Contact contact) {
        deleteContact(mTABLE_CONTACTS, contact);
    }

    public void blockContact(Contact contact) {
        updateBlocked(contact, true);
    }

    public void unblockContact(Contact contact) {
        updateBlocked(contact, true);
    }

    private void updateBlocked(Contact contact, boolean blocked) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(mKEY_BLOCKED, blocked);

        db.update(mTABLE_CONTACTS,
                values,
                mKEY_USERID + " = ?",
                new String[]{String.valueOf(contact.getId())}
        );

    }

    public List<Contact> searchContacts(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = db.query(mTABLE_CONTACTS,
                new String[]{mKEY_USERID, mKEY_USERNAME, mKEY_BLOCKED},
                mKEY_USERNAME + " LIKE ? ",
                new String[]{username},null, null, null
        );

        if(cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        parseBoolean(cursor.getString(2))
                );
                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contacts;
    }
}