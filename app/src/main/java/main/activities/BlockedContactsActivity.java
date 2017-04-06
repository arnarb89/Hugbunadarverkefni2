package main.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.List;

import main.adapters.ContactsListAdapter;
import main.managers.ContactManager;
import main.managers.LoginManager;
import main.model.Contact;
import testcompany.cloudmessagingtest2.R;

public class BlockedContactsActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_contacts);

        contactManager = new ContactManager(BlockedContactsActivity.this);

        listView = (ListView) findViewById(R.id.blockedContactsList);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);

        final List<Contact> contactListData = contactManager.getBlockedContacts();


        // Populate blocked contact list with items
        contactsListAdapter = new ContactsListAdapter(contactListData, this.getBaseContext());
        listView.setAdapter(contactsListAdapter);

        // Set Long Press onClick listener for list items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int position = arg2;

                PopupMenu popup = new PopupMenu(BlockedContactsActivity.this, arg1, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.unblock_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.unblock_contact){
                            contactManager.unblockContact(contactListData.get(position));
                            contactListData.remove(position);
                            contactsListAdapter.notifyDataSetChanged();
                        }else{
                            contactManager.deleteContact(contactListData.get(position));
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
                Intent myIntent = new Intent(BlockedContactsActivity.this, SearchContactsActivity.class);
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, RecentConversationsActivity.class);
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, ContactsActivity.class);
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, NewFriendsActivity.class);
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(BlockedContactsActivity.this);
                loginManager.signOut();
            }
        });
    }
}
