package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NewFriendsActivity extends Activity {

    LoginManager loginManager;
    ContactsManager contactsManager;

    ListView listView;
    EditText editText;


    Button recentConversationsButton;
    Button contactsButton;
    Button newFriendsButton;
    Button blockedListButton;
    Button signOutButton;

    EditText lookForContactsEditText;
    Button addContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);

        contactsManager = new ContactsManager(getBaseContext());

        listView = (ListView) findViewById(R.id.friendRequestsList);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);

        lookForContactsEditText = (EditText)findViewById(R.id.lookForContactsEditText);
        addContactButton = (Button) findViewById(R.id.sendFriendRequestButton);

        // Populate Recent Conversations list with items
        NewFriendsRequestsAdapter newFriendsRequestsAdapter = new NewFriendsRequestsAdapter(contactsManager.getRequests(), this.getBaseContext());
        listView.setAdapter(newFriendsRequestsAdapter);




        lookForContactsEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Toast.makeText(getApplicationContext(), "Look For Contacts text has changed.", Toast. LENGTH_SHORT).show();
                // TODO: contactManager.globalSearchForUsername
                addContactButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactsManager.addContact(new Contact(0,"temp",false)); // TODO: should use contact given by contactManager.globalSearchForUsername above
                    }
                });
            }
        });




        // Set menu tab button listeners
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, SearchContactsActivity.class);
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, RecentConversationsActivity.class);
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, ContactsActivity.class);
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, BlockedContactsActivity.class);
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(NewFriendsActivity.this);
                loginManager.signOut();
            }
        });
    }
}
