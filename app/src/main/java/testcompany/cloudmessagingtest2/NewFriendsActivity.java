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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

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
        addContactButton.setTextColor(getResources().getColor(R.color.darkred));

        // Populate Recent Conversations list with items
        NewFriendsRequestsAdapter newFriendsRequestsAdapter = new NewFriendsRequestsAdapter(contactsManager.getRequests(), this.getBaseContext());
        listView.setAdapter(newFriendsRequestsAdapter);


        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        lookForContactsEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence searchInput, int start, int before, int count) {
                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int userId = 0;
                        String username = "";
                        try {
                            userId = Integer.getInteger(response.getString("userId"));
                            username = response.getString("username");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewFriendsActivity.this, "There was an error.", Toast.LENGTH_SHORT).show();
                        }
                        final int userId_final = userId;
                        final String username_final = username;

                        addContactButton.setTextColor(getResources().getColor(R.color.darkgreen));
                        addContactButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contactsManager.addContact(new Contact(userId_final, username_final, false));
                                Toast.makeText(NewFriendsActivity.this, "Contact added.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewFriendsActivity.this, "There was an error.", Toast.LENGTH_SHORT).show();
                        addContactButton.setTextColor(getResources().getColor(R.color.darkred));
                        addContactButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });

                    }
                };
                contactsManager.globalSearchForUsername(searchInput.toString(), responseListener,errorListener );
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