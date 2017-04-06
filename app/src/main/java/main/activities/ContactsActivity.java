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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import main.adapters.ContactsListAdapter;
import main.managers.ContactManager;
import main.managers.LoginManager;
import main.model.Contact;
import testcompany.cloudmessagingtest2.R;

public class ContactsActivity extends Activity {

    LoginManager loginManager;
    ContactManager contactManager;

    ListView listView;
    EditText editText;

    Button recentConversationsButton;
    Button contactsButton;
    Button newFriendsButton;
    Button blockedListButton;
    Button signOutButton;

    ContactsListAdapter contactsListAdapter;

    final List<Contact> contactListData = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactManager = new ContactManager(ContactsActivity.this);

        listView = (ListView) findViewById(R.id.contactsList);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);

        contactListData.addAll(contactManager.getNonBlockedContacts());

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(contactDeletionReceiver,
                new IntentFilter("update_contacts_on_delete"));


        // Populate contact list with items
        contactsListAdapter = new ContactsListAdapter(contactListData, this.getBaseContext());
        listView.setAdapter(contactsListAdapter);

        // Set Tap onClick listener for Contact list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int position = arg2;

                Intent myIntent = new Intent(ContactsActivity.this, ConversationActivity.class);
                myIntent.putExtra("KEY_contactId", contactListData.get(position).getId());
                ContactsActivity.this.startActivity(myIntent);

            }
        });

        // Set Long Press onClick listener for Contact list
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int position = arg2;

                PopupMenu popup = new PopupMenu(ContactsActivity.this, arg1, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId()==R.id.block_contact){
                            contactManager.blockContact(contactListData.get(position));
                            contactListData.remove(position);
                            contactsListAdapter.notifyDataSetChanged();
                        }else{
                            contactManager.unfriendContact(contactListData.get(position));
                            contactListData.remove(position);
                            contactsListAdapter.notifyDataSetChanged();
                        }

                        return true;
                    }
                });

                popup.show();
                return true;

            }
        });






        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ContactsActivity.this, SearchContactsActivity.class);
                ContactsActivity.this.startActivity(myIntent);
            }
        });

        recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ContactsActivity.this, RecentConversationsActivity.class);
                ContactsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ContactsActivity.this, NewFriendsActivity.class);
                ContactsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ContactsActivity.this, BlockedContactsActivity.class);
                ContactsActivity.this.startActivity(myIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(ContactsActivity.this);
                loginManager.signOut();
            }
        });
    }


    public BroadcastReceiver contactDeletionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {
                contactListData.clear();
                contactListData.addAll(contactManager.getNonBlockedContacts());
                contactsListAdapter.notifyDataSetChanged();
            }
        }
    };
}
