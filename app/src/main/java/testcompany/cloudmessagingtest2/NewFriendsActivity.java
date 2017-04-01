package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

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

    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms

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

            public void afterTextChanged(final Editable s) {
                if (s.length() < 3){
                    return;
                }

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable(){

                            @Override
                            public void run(){
                                // update ui here

                                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
//                        int userId = 0;
//                        String username = "";
                                        try {
                                            final int userId = response.getInt("userId");
                                            final String username = response.getString("username");
                                            Log.i("testing", "userId: " + userId + " username: " + username);

//                            final int userId_final = userId;
//                            final String username_final = username;

                                            addContactButton.setTextColor(getResources().getColor(R.color.darkgreen));
                                            addContactButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //contactsManager.addContact(new Contact(userId, username, false));
                                                    contactsManager.addRequest(new Contact(userId, username, false));
                                                    Toast.makeText(NewFriendsActivity.this, "Contact added.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(NewFriendsActivity.this, "No user by that name.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                Response.ErrorListener errorListener = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(NewFriendsActivity.this, "No user by that name.", Toast.LENGTH_SHORT).show();
                                        addContactButton.setTextColor(getResources().getColor(R.color.darkred));
                                        addContactButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        });

                                    }
                                };
                                contactsManager.globalSearchForUsername(s.toString(), responseListener, errorListener);
                            }
                        });
                    }
                }, DELAY);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence searchInput, int start, int before, int count) {
                if(timer != null){
                    timer.cancel();
                }
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
