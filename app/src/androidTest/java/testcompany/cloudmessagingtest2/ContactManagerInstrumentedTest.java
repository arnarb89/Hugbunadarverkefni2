package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import main.managers.ContactDatabaseHelper;
import main.model.Contact;

@RunWith(AndroidJUnit4.class)
public class ContactManagerInstrumentedTest {

    ContactDatabaseHelper dbHelper;
    Contact haukur;
    Contact contact2;
    Contact blockedContact3;

    @Before
    public void createDB() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbHelper = new ContactDatabaseHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.dropTables();
        dbHelper.onCreate(db);
        haukur = new Contact(1, "Haukur", false);
        contact2 = new Contact(1, "Arnar", false);
    }

    @Test
    public void requestAcceptable() {
/*//        Add friend request
        dbHelper.insertRequest(haukur);
        Contact contact;
        List<Contact> friendRequests = dbHelper.getFriendRequests();
        contact = friendRequests.get(0);

        Assert.assertTrue(areEqual(haukur, contact));

//        Accept friend request
        dbHelper.acceptRequest(contact);

//        haukur should be in contacts table
        contact = dbHelper.getContact(haukur.getId());
        Assert.assertTrue(areEqual(haukur, contact));*/
    }

    public boolean areEqual(Contact c1, Contact c2) {
        return c1.getId() == c2.getId() && c1.isBlocked() == c2.isBlocked() && c1.getUsername().equals(c2.getUsername());
    }
}
