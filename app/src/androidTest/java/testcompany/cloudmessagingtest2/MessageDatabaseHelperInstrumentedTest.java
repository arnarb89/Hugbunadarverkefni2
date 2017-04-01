package testcompany.cloudmessagingtest2;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.managers.MessageDatabaseHelper;
import main.model.Message;

public class MessageDatabaseHelperInstrumentedTest {

    MessageDatabaseHelper dbHelper;

    @Before
    public void createDB() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbHelper = new MessageDatabaseHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.dropTables();
        dbHelper.onCreate(db);
    }

    @Test
    public void messageAddition() {
        Message message1 = new Message("Saell", 1, 2, new Date( (long) 10));
        dbHelper.addMessage(message1);
    }

    @Test
    public void mostRecentMessageRetrieval() {
        List<Message> messages = dbHelper.getMostRecentMessages();
        Assert.assertTrue(messages.isEmpty());
        Message message1 = new Message("Saell", 1, 2, new Date( (long) 10));
        dbHelper.addMessage(message1);
        messages = dbHelper.getMostRecentMessages();
        Assert.assertTrue(areEqual(messages.get(0), message1));
    }

    @Test
    public void mostRecentMessageOrdering() {
        List<Message> messagesBefore = new ArrayList<Message>();
        messagesBefore.add(new Message("hvad er i matinn?", 4, 1, new Date((long) 55)) );
        messagesBefore.add(new Message("Hae, loksins", 5, 1, new Date((long) 50)) );
        messagesBefore.add(new Message("Hae, var upptekinn", 1, 5, new Date((long) 45)) );
        messagesBefore.add(new Message("Hae, svaradu", 5, 1, new Date((long) 40)) );
        messagesBefore.add(new Message("Hae!", 5, 1, new Date((long) 35)) );
        messagesBefore.add(new Message("hringdu i mig", 4, 1, new Date((long) 30)) );
        messagesBefore.add(new Message("Yo", 1, 3, new Date((long) 25)) );
        messagesBefore.add(new Message("hvad segist?", 1, 2, new Date( (long) 20)) );
        messagesBefore.add(new Message("Saell sjalfur", 2, 1, new Date( (long) 15)) );
        messagesBefore.add(new Message("Saell", 1, 2, new Date( (long) 10)) );
        for( Message msg : messagesBefore ) {
            dbHelper.addMessage(msg);
        }
        List<Message> messagesAfter = dbHelper.getMostRecentMessages();
        for(Message msg : messagesAfter) {
            debugPrint(msg);
        }
        Assert.assertTrue(areEqual(messagesBefore.get(0), messagesAfter.get(0)));
        Assert.assertTrue(areEqual(messagesBefore.get(1), messagesAfter.get(1)));
        Assert.assertTrue(areEqual(messagesBefore.get(6), messagesAfter.get(2)));
    }

    public boolean areEqual(Message m1, Message m2) {
        return m1.getSenderId() == m2.getSenderId()
            && m1.getReceiverId() == m2.getReceiverId()
            && m1.getContent().equals(m2.getContent())
            && m1.getSentDate().getTime() == m2.getSentDate().getTime();
    }

    public void debugPrint(Message msg) {
        Log.d("MSGDBH.unit", msg.getContent());
        Log.d("MSGDBH.unit", String.valueOf(msg.getSenderId()));
        Log.d("MSGDBH.unit", String.valueOf(msg.getReceiverId()));
        Log.d("MSGDBH.unit", String.valueOf(msg.getSentDate().getTime()));
        Log.d("MSGDBH.unit", "-");
    }
}
