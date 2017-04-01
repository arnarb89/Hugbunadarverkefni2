package testcompany.cloudmessagingtest2;

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

        final List<Contact> contactListData = contactManager.getNonBlockedContacts();


        // Populate contact list with items
        ContactsListAdapter contactsListAdapter;
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
                        }else{
                            contactManager.deleteContact(contactListData.get(position));
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
}
