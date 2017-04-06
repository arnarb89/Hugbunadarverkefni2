package main.managers;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.List;

import main.model.Contact;

public class ContactManager {

    private ContactDatabaseHelper mDbHelper;
    private Context mContext;
    private NetworkHandler mNetworkHandler;

    public ContactManager(Context context) {
        mContext = context;
        mDbHelper = new ContactDatabaseHelper(context);
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
    public List<Contact> getReceivedFriendRequests() { return mDbHelper.getFriendRequests(); }

    public Contact getContactById(int userid) {
        return mDbHelper.getContact(userid);
    }

    public void sendFriendRequest(Contact contact) {
//        mDbHelper.insertContact(contact);
        mNetworkHandler.sendFriendRequest(contact);
    }

    public void storeFriendRequest(Contact contact) {
        mDbHelper.insertRequest(contact);
    }

    public void storeContact(Contact contact) {
        mDbHelper.insertContact(contact);
    }

    public void acceptFriendRequest(Contact contact) {
        mDbHelper.deleteFriendRequest(contact);
        mNetworkHandler.acceptFriendRequest(contact);
        mDbHelper.insertContact(contact);
    }

    public void declineFriendRequest(Contact contact) {
        mNetworkHandler.declineFriendRequest(contact);
        mDbHelper.deleteFriendRequest(contact);
    }

    public void deleteContact(Contact contact) {
        mDbHelper.deleteContact(contact);
        MessageDatabaseHelper msgDbHelper = new MessageDatabaseHelper(mContext);
        msgDbHelper.deleteMessageHistory(contact);
        //mNetworkHandler.deleteContact(contact);
    }

    public void unfriendContact(Contact contact) {
        mDbHelper.deleteContact(contact);
        MessageDatabaseHelper msgDbHelper = new MessageDatabaseHelper(mContext);
        msgDbHelper.deleteMessageHistory(contact);
        mNetworkHandler.deleteContact(contact);
    }

    public void blockContact(Contact contact) {
        mDbHelper.updateBlocked(contact, true);
        mNetworkHandler.blockContact(contact);
    }

    public void unblockContact(Contact contact) {
        mDbHelper.updateBlocked(contact, false);
        mNetworkHandler.unblockContact(contact);
    }

    public List<Contact> searchContacts(String username) {
        return mDbHelper.searchContacts(username);
    }
}
