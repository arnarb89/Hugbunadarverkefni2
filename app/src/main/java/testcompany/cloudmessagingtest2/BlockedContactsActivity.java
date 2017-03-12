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
import android.widget.Toast;

public class BlockedContactsActivity extends Activity {

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
        setContentView(R.layout.activity_blocked_contacts);

        listView = (ListView) findViewById(R.id.blockedContactsList);
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

        // Populate Recent Conversations list with items
        ContactsListAdapter contactsListAdapter = new ContactsListAdapter(theData, this.getBaseContext());
        listView.setAdapter(contactsListAdapter);

        // Set Long Press onClick listener for list items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Object[] thisData = (Object[]) theData[arg2];
                Toast.makeText(getApplicationContext(), "You clicked "+(String)thisData[0]+"'s conversation. LONG PRESS.", Toast. LENGTH_SHORT).show();


                PopupMenu popup = new PopupMenu(BlockedContactsActivity.this, arg1, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.unblock_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(BlockedContactsActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
                return true;

            }
        });


        // Set Search Contacts to go to SearchContactsActivity
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, BlockedContactsActivity.class); // TODO: vantar að breyta seinna viðfanginu í SearchContactsActivity
                Toast.makeText(getApplicationContext(), "Opening Search Contacts Activity.", Toast. LENGTH_SHORT).show();
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, RecentConversationsActivity.class); // TODO: það á örugglega ekki að vera neitt onClick núna
                Toast.makeText(getApplicationContext(), "This button should probably not do anything within this activity.", Toast. LENGTH_SHORT).show();
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, ContactsActivity.class); // TODO: vantar að breyta seinna viðfanginu í NewFriendsActivity
                Toast.makeText(getApplicationContext(), "Opening Contacts Activity.", Toast. LENGTH_SHORT).show();
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, NewFriendsActivity.class); // TODO: vantar að breyta seinna viðfanginu í NewFriendsActivity
                Toast.makeText(getApplicationContext(), "Opening NewFriendsActivity.", Toast. LENGTH_SHORT).show();
                BlockedContactsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You are in BlockedContactsActivity.", Toast. LENGTH_SHORT).show();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(BlockedContactsActivity.this);
                loginManager.signOut();
//                Intent myIntent = new Intent(RecentConversationsActivity.this, RecentConversationsActivity.class); // TODO: vantar að breyta þessu yfir í sign out
//                Toast.makeText(getApplicationContext(), "Pretending to sign out.", Toast. LENGTH_SHORT).show();
//                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });
    }
}
