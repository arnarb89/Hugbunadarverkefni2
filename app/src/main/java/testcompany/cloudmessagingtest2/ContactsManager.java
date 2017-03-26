package testcompany.cloudmessagingtest2;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class ContactsManager {

    private ContactsDatabaseHelper mDbHelper;
    //    TODO AuthHelper mAuthHelper;
    private static final String mURL_CONTACTS = "http://something.com"; // TODO
    private NetworkHandler mNetworkHandler;

    public ContactsManager(Context context) {
        mDbHelper = new ContactsDatabaseHelper(context);
        mNetworkHandler = new NetworkHandler(context);
    }

    public List<Contact> getBlockedContacts() {
        return mDbHelper.getBlockedContacts();
    }

    public List<Contact> getNonBlockedContacts() {
        return mDbHelper.getNonBlockedContacts();
    }

    public void globalSearchForUsername(
            String username,
            Response.Listener<JSONObject> responseListener,
            Response.ErrorListener errorListener)
    {
        mNetworkHandler.searchForContact(username, responseListener, errorListener);
    }
    public List<Contact> getRequests() { return mDbHelper.getRequests(); }

    public Contact getContactById(int userid) {
        return mDbHelper.getContact(userid);
    }

    public void addContact(Contact contact) {
        mDbHelper.addContact(contact);
    }

    public void addRequest(Contact contact) {
        mDbHelper.addRequest(contact);
    }

    public void acceptRequest(Contact contact) {
        mDbHelper.acceptRequest(contact);
    }

    public void declineRequest(Contact contact) {
        mDbHelper.declineRequest(contact);
    }

    public void deleteContact(Contact contact) {
        mDbHelper.deleteContact(contact);
    }

    public void blockContact(Contact contact) {
        mDbHelper.blockContact(contact);
    }

    public void unblockContact(Contact contact) {
        mDbHelper.unblockContact(contact);
    }

    public List<Contact> searchContacts(String username) {
        return mDbHelper.searchContacts(username);
    }
}
