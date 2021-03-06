package main.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SearchContactsActivity extends Activity {

    LoginManager loginManager;
    ContactManager contactManager;

    ListView listView;
    EditText editText;

    Button recentConversationsButton;
    Button contactsButton;
    Button newFriendsButton;
    Button blockedListButton;
    Button signOutButton;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contacts);

        context = this.getBaseContext();
      
        contactManager = new ContactManager(SearchContactsActivity.this);
      
        listView = (ListView) findViewById(R.id.searchContactsResultsListView);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);



        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(final Editable s) {
                final List<Contact> contactListData = contactManager.searchContacts(s.toString());
                ContactsListAdapter contactsListAdapter = new ContactsListAdapter(contactListData, context);
                ListView listView = listView = (ListView) findViewById(R.id.searchContactsResultsListView);
                listView.setAdapter(contactsListAdapter);

                // Set Tap onClick listener for list items
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        final int position = arg2;

                        Intent myIntent = new Intent(SearchContactsActivity.this, ConversationActivity.class);
                        myIntent.putExtra("KEY_contactId", contactListData.get(position).getId());
                        SearchContactsActivity.this.startActivity(myIntent);
                    }
                });

                // Set Long Press onClick listener for list items
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        final int position = arg2;

                        PopupMenu popup = new PopupMenu(SearchContactsActivity.this, arg1, Gravity.RIGHT);
                        popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                if(item.getItemId()==R.id.block_contact){
                                    contactManager.blockContact(contactListData.get(position));
                                }else{
                                    contactManager.unfriendContact(contactListData.get(position));
                                }
                                return true;
                            }
                        });
                        popup.show();
                        return true;
                    }
                });
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });



        // set listeners for menu tab buttons
        recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, RecentConversationsActivity.class);
                SearchContactsActivity.this.startActivity(myIntent);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, ContactsActivity.class);
                SearchContactsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, NewFriendsActivity.class);
                SearchContactsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, BlockedContactsActivity.class);
                SearchContactsActivity.this.startActivity(myIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(SearchContactsActivity.this);
                loginManager.signOut();
            }
        });
    }
}
