package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class RecentConversationsActivity extends Activity {

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
        setContentView(R.layout.activity_recentconversations);

        Log.i("testing", "RecentConversationsActivity.onCreate()");

        listView = (ListView) findViewById(R.id.recentConversationsList);
        editText = (EditText) findViewById(R.id.btnSearchContacts);

        recentConversationsButton = (Button) findViewById(R.id.btnRecentConversations);
        contactsButton = (Button) findViewById(R.id.btnContacts);
        newFriendsButton = (Button) findViewById(R.id.btnNewContacts);
        blockedListButton = (Button) findViewById(R.id.btnBlockedContacts);
        signOutButton = (Button) findViewById(R.id.btnSignOut);

        // TODO: dummy test data, can delete after more of the project is finished
        final Object[] theData = new Object[]{
                new Object[]{"Arnar","13:37","I am da best."},
                new Object[]{"Haukur","14:37","I am an idiot."} ,
                new Object[]{"Simon","15:37","I am also an idiot."},
                new Object[]{"Jonni","16:37","I am also an idiot."} ,
                new Object[]{"Baddi","17:37","I am also an idiot."},
                new Object[]{"Kári","18:37","I am also an idiot."} ,
                new Object[]{"Lárus","19:37","I am also an idiot."},
                new Object[]{"Tómas","20:37","I am also an idiot."} ,
                new Object[]{"Indriði","21:37","I am also an idiot."},
                new Object[]{"Auddi","22:37","I am also an idiot."} ,
                new Object[]{"Sveppi","23:37","I am also an idiot."}
        };

        // Populate Recent Conversations list with items
        RecentConversationsAdapter recentConversationsAdapter = new RecentConversationsAdapter(theData, this.getBaseContext());
        listView.setAdapter(recentConversationsAdapter);

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

                PopupMenu popup = new PopupMenu(RecentConversationsActivity.this, arg1, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(RecentConversationsActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
                Intent myIntent = new Intent(RecentConversationsActivity.this, SearchContactsActivity.class); // TODO: vantar að breyta seinna viðfanginu í SearchContactsActivity
                Toast.makeText(getApplicationContext(), "Opening Search Contacts Activity.", Toast. LENGTH_SHORT).show();
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        /*recentConversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, RecentConversationsActivity.class); // TODO: það á örugglega ekki að vera neitt onClick núna
                Toast.makeText(getApplicationContext(), "This button should probably not do anything within this activity.", Toast. LENGTH_SHORT).show();
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });*/

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, ContactsActivity.class); // TODO: vantar að breyta seinna viðfanginu í ContactsActivity
                Toast.makeText(getApplicationContext(), "Opening Contacts Activity.", Toast. LENGTH_SHORT).show();
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        newFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, NewFriendsActivity.class); // TODO: vantar að breyta seinna viðfanginu í NewFriendsActivity
                Toast.makeText(getApplicationContext(), "Opening NewFriendsActivity.", Toast. LENGTH_SHORT).show();
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        blockedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RecentConversationsActivity.this, BlockedContactsActivity.class); // TODO: vantar að breyta seinna viðfanginu í BlockedContactsActivity
                Toast.makeText(getApplicationContext(), "Opening BlockedContactsActivity.", Toast. LENGTH_SHORT).show();
                RecentConversationsActivity.this.startActivity(myIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(RecentConversationsActivity.this);
                loginManager.signOut();
            }
        });
    }
}
