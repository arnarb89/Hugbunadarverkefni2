package main.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.adapters.ConversationAdapter;
import main.managers.ContactManager;
import main.managers.MessageManager;
import main.model.Message;
import main.managers.PreferencesHelper;
import testcompany.cloudmessagingtest2.R;

public class ConversationActivity extends Activity {

    Button sendMessageButton;
    Button backButton;
    Button contactNameButton;

    EditText messageInputField;

    ListView conversationListView;

    ContactManager contactManager;
    MessageManager messageManager;

    final List<Message> previousMessages = new ArrayList<Message>();
    ConversationAdapter conversationAdapter;
    int idOfWhoYouAreTalkingToTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        final int IdOfWhoYouAreTalkingTo = getIntent().getIntExtra("KEY_contactId",0);
        final int yourId = PreferencesHelper.getUserId(ConversationActivity.this);
        idOfWhoYouAreTalkingToTemp = IdOfWhoYouAreTalkingTo;

        contactManager = new ContactManager(ConversationActivity.this);
        messageManager = new MessageManager(ConversationActivity.this);

        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        backButton = (Button) findViewById(R.id.btnRecentConversations);
        contactNameButton = (Button) findViewById(R.id.conversationNameField);

        messageInputField = (EditText) findViewById(R.id.messageEditText);

        conversationListView = (ListView) findViewById(R.id.conversationListView);

        //
        List<Message> previousMessagesTemp =  messageManager.getPreviousMessages(yourId, contactManager.getContactById(IdOfWhoYouAreTalkingTo), new Date());
        for(int i = 0; i<previousMessagesTemp.size() ;i++){
            previousMessages.add(previousMessagesTemp.get(i));
        }

        contactNameButton.setText(contactManager.getContactById(IdOfWhoYouAreTalkingTo).getUsername());

        // Populate the conversation list with items
        //final ConversationAdapter conversationAdapter = new ConversationAdapter(previousMessages, this.getBaseContext());
        conversationAdapter = new ConversationAdapter(previousMessages, ConversationActivity.this, IdOfWhoYouAreTalkingTo, yourId);
        conversationListView.setAdapter(conversationAdapter);
        conversationAdapter.notifyDataSetChanged();



        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("update_conversation"));

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(contactDeletionReceiver,
                new IntentFilter("update_conversation_on_delete"));


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sending message.", Toast. LENGTH_SHORT).show();
                String content = messageInputField.getText().toString();
                int senderId = yourId;
                int receiverId = IdOfWhoYouAreTalkingTo;
                Date sentTime = new Date();
                Message message = new Message(content, senderId, receiverId, sentTime);

                messageManager.sendMessage(message);

                previousMessages.add(0, message);
                conversationAdapter.notifyDataSetChanged();
                messageInputField.setText("");
            }
        });

        contactNameButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popup = new PopupMenu(ConversationActivity.this, v, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.block_contact){
                            contactManager.blockContact(contactManager.getContactById(IdOfWhoYouAreTalkingTo));
                            finish();
                        }else{
                            contactManager.unfriendContact(contactManager.getContactById(IdOfWhoYouAreTalkingTo));
                            finish();
                        }

                        return true;
                    }
                });


                popup.show();

                return true;
            }
        });


        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(isTaskRoot()){
                    Intent myIntent = new Intent(ConversationActivity.this, RecentConversationsActivity.class);
                    ConversationActivity.this.startActivity(myIntent);
                }else{
                    finish();
                }

            }
        });
    }



    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {

                String content = intent.getStringExtra("content");
                int senderId = intent.getIntExtra("senderId",0);
                int receiverId = intent.getIntExtra("receiverId",0);
                long sentDate = intent.getLongExtra("sentDate",0);

                if(idOfWhoYouAreTalkingToTemp!=senderId){
                    return;
                }

                //add message to conversation listView
                Message message = new Message(content,senderId, receiverId, new Date(sentDate) );
                previousMessages.add(0, message);
                conversationAdapter.notifyDataSetChanged();
            }
        }
    };

    public BroadcastReceiver contactDeletionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {

                if(isTaskRoot()){
                    Intent myIntent = new Intent(ConversationActivity.this, RecentConversationsActivity.class);
                    ConversationActivity.this.startActivity(myIntent);
                }else{
                    finish();
                }
            }
        }
    };
}
