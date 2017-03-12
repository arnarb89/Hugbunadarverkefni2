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

        listView = (ListView) findViewById(R.id.friendRequestsList);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);

        lookForContactsEditText = (EditText)findViewById(R.id.lookForContactsEditText);
        addContactButton = (Button) findViewById(R.id.sendFriendRequestButton);


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

        // Populate Recent Conversations list with items
        NewFriendsRequestsAdapter newFriendsRequestsAdapter = new NewFriendsRequestsAdapter(theData, this.getBaseContext());
        listView.setAdapter(newFriendsRequestsAdapter);







        // Set Search Contacts to go to SearchContactsActivity
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, RecentConversationsActivity.class); // TODO: vantar að breyta seinna viðfanginu í SearchContactsActivity
                Toast.makeText(getApplicationContext(), "Opening SearchContactsActivity.", Toast. LENGTH_SHORT).show();
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, RecentConversationsActivity.class); // TODO: það á örugglega ekki að vera neitt onClick núna
                Toast.makeText(getApplicationContext(), "Opening RecentConversationsActivity.", Toast. LENGTH_SHORT).show();
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, RecentConversationsActivity.class); // TODO: vantar að breyta seinna viðfanginu í ContactsActivity
                Toast.makeText(getApplicationContext(), "Opening Contacts Activity.", Toast. LENGTH_SHORT).show();
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, NewFriendsActivity.class); // TODO: vantar að breyta seinna viðfanginu í NewFriendsActivity
                Toast.makeText(getApplicationContext(), "Opening NewFriendsActivity.", Toast. LENGTH_SHORT).show();
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, RecentConversationsActivity.class); // TODO: vantar að breyta seinna viðfanginu í BlockedContactsActivity
                Toast.makeText(getApplicationContext(), "Opening BlockedContactsActivity.", Toast. LENGTH_SHORT).show();
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NewFriendsActivity.this, RecentConversationsActivity.class); // TODO: vantar að breyta þessu yfir í sign out
                Toast.makeText(getApplicationContext(), "Pretending to sign out.", Toast. LENGTH_SHORT).show();
                NewFriendsActivity.this.startActivity(myIntent);
            }
        });

        lookForContactsEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Toast.makeText(getApplicationContext(), "Look For Contacts text has changed.", Toast. LENGTH_SHORT).show(); //TODO: needs to query the app-server
            }
        });

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pretending to send friend request.", Toast. LENGTH_SHORT).show(); // TODO: needs to call app-server api
            }
        });
    }
}
