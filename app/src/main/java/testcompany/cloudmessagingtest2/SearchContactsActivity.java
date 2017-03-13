package testcompany.cloudmessagingtest2;

import android.app.Activity;
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
import android.widget.Toast;

public class SearchContactsActivity extends Activity {

    LoginManager loginManager;

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
        setContentView(R.layout.activity_search_contacts);

        listView = (ListView) findViewById(R.id.searchContactsResultsListView);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);

        // TODO: dummy test data, can delete after more of the project is finished
        final Object[] theData = new Object[]{
                new Object[]{"Arnar"},
                new Object[]{"Haukur"} ,
                new Object[]{"Simon"},
                new Object[]{"Jonni"} ,
                new Object[]{"Baddi"},
                new Object[]{"Kári"} ,
                new Object[]{"Lárus"},
                new Object[]{"Tómas"} ,
                new Object[]{"Indriði"},
                new Object[]{"Auddi"} ,
                new Object[]{"Sveppi"}
        };

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Toast.makeText(getApplicationContext(), "Search Contacts text has changed.", Toast. LENGTH_SHORT).show(); //TODO: needs to query the DB
            }
        });


        // Populate Recent Conversations list with items
        ContactsListAdapter contactsListAdapter = new ContactsListAdapter(theData, this.getBaseContext());
        listView.setAdapter(contactsListAdapter);

        // Set Tap onClick listener for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Object[] thisData = (Object[]) theData[arg2];
                Toast.makeText(getApplicationContext(), "You clicked "+(String)thisData[0]+"'s conversation. TAP.", Toast. LENGTH_SHORT).show();
            }
        });

        // Set Long Press onClick listener for list items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Object[] thisData = (Object[]) theData[arg2];
                Toast.makeText(getApplicationContext(), "You clicked "+(String)thisData[0]+"'s conversation. LONG PRESS.", Toast. LENGTH_SHORT).show();


                PopupMenu popup = new PopupMenu(SearchContactsActivity.this, arg1, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(SearchContactsActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
                return true;

            }
        });




        recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, RecentConversationsActivity.class); // TODO: það á örugglega ekki að vera neitt onClick núna
                Toast.makeText(getApplicationContext(), "Opening RecentConversationsActivity.", Toast. LENGTH_SHORT).show();
                SearchContactsActivity.this.startActivity(myIntent);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, ContactsActivity.class); // TODO: það á örugglega ekki að vera neitt onClick núna
                Toast.makeText(getApplicationContext(), "Opening Contacts Activity.", Toast. LENGTH_SHORT).show();
                SearchContactsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, NewFriendsActivity.class); // TODO: vantar að breyta seinna viðfanginu í NewFriendsActivity
                Toast.makeText(getApplicationContext(), "Opening NewFriendsActivity.", Toast. LENGTH_SHORT).show();
                SearchContactsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchContactsActivity.this, BlockedContactsActivity.class); // TODO: vantar að breyta seinna viðfanginu í BlockedContactsActivity
                Toast.makeText(getApplicationContext(), "Opening BlockedContactsActivity.", Toast. LENGTH_SHORT).show();
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
