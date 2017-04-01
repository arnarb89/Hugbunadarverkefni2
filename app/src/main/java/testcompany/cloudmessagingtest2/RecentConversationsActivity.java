package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

public class RecentConversationsActivity extends Activity {

    LoginManager loginManager;
    MessageManager messageManager;
    ContactManager contactsManager;

    ListView listView;
    EditText editText;

    Button recentConversationsButton;
    Button contactsButton;
    Button newFriendsButton;
    Button blockedListButton;
    Button signOutButton;

    RecentConversationsAdapter recentConversationsAdapter;

    final List<Message> recentConversationsListData = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentconversations);

        final int yourId = PreferencesManager.getUserId(RecentConversationsActivity.this);

        contactsManager = new ContactManager(RecentConversationsActivity.this);

        messageManager = new MessageManager(RecentConversationsActivity.this);


        listView = (ListView) findViewById(R.id.recentConversationsList);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);

        List<Message> recentConversationsListDataTemp = messageManager.getMostRecentMessages();
        for(int i = 0; i<recentConversationsListDataTemp.size() ;i++){
            recentConversationsListData.add(recentConversationsListDataTemp.get(i));
        }

        // Populate Recent Conversations list with items
        recentConversationsAdapter = new RecentConversationsAdapter(recentConversationsListData, this.getBaseContext());
        listView.setAdapter(recentConversationsAdapter);

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("update_recent_conversations"));

        // Set Tap onClick listener for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int position = arg2;

                Intent myIntent = new Intent(RecentConversationsActivity.this, ConversationActivity.class);
                if(recentConversationsListData.get(position).getReceiverId()==yourId) {
                    myIntent.putExtra("KEY_contactId", recentConversationsListData.get(position).getSenderId());
                }else{
                    myIntent.putExtra("KEY_contactId", recentConversationsListData.get(position).getReceiverId());
                }
                RecentConversationsActivity.this.startActivity(myIntent);

            }
        });

        // Set Long Press onClick listener for list items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int position = arg2;

                PopupMenu popup = new PopupMenu(RecentConversationsActivity.this, arg1, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.block_contact){
                            if(recentConversationsListData.get(position).getReceiverId()==yourId) {
                                contactsManager.blockContact(contactsManager.getContactById(recentConversationsListData.get(position).getSenderId()));
                            }else{
                                contactsManager.blockContact(contactsManager.getContactById(recentConversationsListData.get(position).getReceiverId()));
                            }
                        }else{
                            if(recentConversationsListData.get(position).getReceiverId()==yourId) {
                                contactsManager.deleteContact(contactsManager.getContactById(recentConversationsListData.get(position).getSenderId()));
                            }else{
                                contactsManager.deleteContact(contactsManager.getContactById(recentConversationsListData.get(position).getReceiverId()));
                            }
                        }
                        return true;
                    }
                });


                popup.show();
                return true;

            }
        });

        // Set menu tab button listeners
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, SearchContactsActivity.class);
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, ContactsActivity.class);
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, NewFriendsActivity.class);
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, BlockedContactsActivity.class);
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(RecentConversationsActivity.this);
                Log.i("testing", "RecentConversationsActivity, signOutButton.onClickListener.signOut()");
                loginManager.signOut();
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {
                recentConversationsListData.clear();
                recentConversationsListData.addAll(messageManager.getMostRecentMessages());
                recentConversationsAdapter.notifyDataSetChanged();
            }
        }
    };
}
