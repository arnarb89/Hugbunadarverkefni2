package main.managers;

import android.content.Context;

import java.util.Date;
import java.util.List;

import main.model.Contact;
import main.model.Message;

public class MessageManager {

    private MessageDatabaseHelper mDbHelper;
    private NetworkHandler mNetworkHandler;

    public MessageManager(Context context) {
        mNetworkHandler = new NetworkHandler(context);
        mDbHelper = new MessageDatabaseHelper(context);
    }


    public void addMessage(Message message) {
        mDbHelper.addMessage(message);
    }

    public void sendMessage(Message message) {
        addMessage(message);
        mNetworkHandler.sendMessage(message);
    }

    public List<Message> getPreviousMessages(int ownerId, Contact contact, Date time) {
        return mDbHelper.getPreviousMessages(ownerId, contact, time);
    }

    public List<Message> getMostRecentMessages() {
        return mDbHelper.getMostRecentMessages();
    }
}
