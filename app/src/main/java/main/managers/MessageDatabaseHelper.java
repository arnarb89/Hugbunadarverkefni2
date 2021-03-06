package main.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.model.Contact;
import main.model.Message;

public class MessageDatabaseHelper extends SQLiteOpenHelper {

    private static final int mDATABASE_VERSION = 2;
    private static final String mDATABASE_NAME = "message_manager";
    private static final String mTABLE_MESSAGES = "messages";

    private static final String mKEY_CONTENT = "content";
    private static final String mKEY_SENDERID = "sender";
    private static final String mKEY_RECEIVERID = "receiver";
    private static final String mKEY_SENT_TIME = "sent";

    private static final String mCREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS "
                + mTABLE_MESSAGES
                + "("
                    + mKEY_CONTENT    + " text, "
                    + mKEY_SENDERID   + " integer, "
                    + mKEY_RECEIVERID + " integer, "
                    + mKEY_SENT_TIME + " integer "
                + ")";

    private static final String mVIEW_MOST_RECENT_UNF = "most_recent_messages_unfiltered";
    private static final String mVIEW_MOST_RECENT = "most_recent_messages";

    private static final String mCREATE_MOST_RECENT_UNFILTERED =
            "CREATE VIEW " + mVIEW_MOST_RECENT_UNF
            + " AS SELECT " + mKEY_CONTENT
            + " , " + mKEY_SENDERID
            + " , " + mKEY_RECEIVERID
            + " ,  MAX( " + mKEY_SENT_TIME + " ) AS " + mKEY_SENT_TIME
            + " FROM " + mTABLE_MESSAGES
            + " GROUP BY " + mKEY_SENDERID + " , " + mKEY_RECEIVERID
            + " LIMIT 40 ";

    private static final String mCREATE_VIEW_MOST_RECENT =
        "CREATE VIEW " + mVIEW_MOST_RECENT + " AS "
                + "SELECT a.* FROM " + mVIEW_MOST_RECENT_UNF + " AS a LEFT JOIN " + mVIEW_MOST_RECENT_UNF + " AS b "
                    + "ON a." + mKEY_SENDERID + " = b." + mKEY_RECEIVERID + " AND a." + mKEY_RECEIVERID + " = b." + mKEY_SENDERID
                    + " WHERE a." + mKEY_SENT_TIME + ">= b." + mKEY_SENT_TIME + " OR b." + mKEY_SENDERID + " IS NULL "
            + " UNION "
                + "SELECT b.* FROM " + mVIEW_MOST_RECENT_UNF + " AS a LEFT JOIN " + mVIEW_MOST_RECENT_UNF + " AS b "
                    + "ON a." + mKEY_SENDERID + " = b." + mKEY_RECEIVERID + " AND a." + mKEY_RECEIVERID + " = b." + mKEY_SENDERID
                    + " WHERE b." + mKEY_SENT_TIME + " > a." + mKEY_SENT_TIME;

    public MessageDatabaseHelper(Context context) {
        super(context, mDATABASE_NAME, null, mDATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mCREATE_TABLE);
        db.execSQL(mCREATE_MOST_RECENT_UNFILTERED);
        db.execSQL(mCREATE_VIEW_MOST_RECENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + mTABLE_MESSAGES);
        db.execSQL("DROP VIEW IF EXISTS " + mVIEW_MOST_RECENT_UNF);
        db.execSQL("DROP VIEW IF EXISTS " + mVIEW_MOST_RECENT);
        onCreate(db);
    }

    public void dropTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + mTABLE_MESSAGES);
        db.execSQL("DROP VIEW IF EXISTS " + mVIEW_MOST_RECENT);
        db.execSQL("DROP VIEW IF EXISTS " + mVIEW_MOST_RECENT_UNF);
    }

    public void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(mKEY_SENDERID, message.getSenderId());
        values.put(mKEY_RECEIVERID, message.getReceiverId());
        values.put(mKEY_CONTENT, message.getContent());
        values.put(mKEY_SENT_TIME, message.getSentDate().getTime());

        db.insert(mTABLE_MESSAGES, null, values);
    }

    /**
     * Get the 20 most recent messages from conversation with given contact that were sent before given time
     * @param contact Conversation partner
     * @param time Time of last displayed message
     */
    public List<Message> getPreviousMessages(int ownerId, Contact contact, Date time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String contactId = String.valueOf(contact.getId());
        Cursor cursor = db.query(mTABLE_MESSAGES
                , new String[] {mKEY_CONTENT, mKEY_SENDERID, mKEY_RECEIVERID, mKEY_SENT_TIME}
                ,   "(" + mKEY_SENDERID + " = ?"
                        + " OR "
                        + mKEY_RECEIVERID + " = ? )"
                    + " AND "
                        + mKEY_SENT_TIME + " < ?"
                , new String[] {contactId, contactId, String.valueOf(time.getTime())}
                , null, null, mKEY_SENT_TIME + " DESC", "20");
        List<Message> messages = new ArrayList<Message>();
        if(cursor.moveToFirst()) {
            do {
                Message message = new Message(
                         cursor.getString(0)
                        ,cursor.getInt(1)
                        ,cursor.getInt(2)
                        ,new Date(cursor.getLong(3))
                );
                messages.add(message);
            } while (cursor.moveToNext());
        }
        return messages;
    }

    /**
     * Gets most recent messages from all contacts
     * @return Gets most recent messages from all contacts
     */
    public List<Message> getMostRecentMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Message> messages = new ArrayList<Message>();
        Cursor cursor = db.query(
                mVIEW_MOST_RECENT
                , new String[] {mKEY_CONTENT, mKEY_SENDERID, mKEY_RECEIVERID, mKEY_SENT_TIME}
                , null, null, null, null, mKEY_SENT_TIME + " DESC ", null
        );
        DatabaseUtils.dumpCursor(cursor);
        if(cursor.moveToFirst()) {
            do {
                debugCursor(cursor,4);
                Message message = new Message(
                        cursor.getString(0)
                        ,Integer.parseInt(cursor.getString(1))
                        ,Integer.parseInt(cursor.getString(2))
                        ,new Date(cursor.getLong(3))
                );
                messages.add(message);
            } while (cursor.moveToNext());
        }
        return messages;
    }

    public void deleteMessageHistory(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mKEY_SENDERID, contact.getId());
        values.put(mKEY_RECEIVERID, contact.getId());
        String contactIdStr = String.valueOf(contact.getId());
        db.delete(mTABLE_MESSAGES,
                mKEY_SENDERID + " = ? OR " + mKEY_RECEIVERID + " = ?",
                new String[]{contactIdStr, contactIdStr});
    }

    private void debugCursor(Cursor cursor, int n) {
        for(int i=0; i<n; i++) {
            Log.d("MSGDBH.Cursor", cursor.getString(i));
        }
        Log.d("MSGDBH.Cursor", "-");
    }
}
