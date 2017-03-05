package testcompany.cloudmessagingtest2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ContactsManager contactsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (contactsManager == null) {
            contactsManager = new ContactsManager(this);
        }
    }

    public void handleButton(View view) {
        TextView resultsTextView = (TextView) findViewById(R.id.resultsTextView);
        int userid = 1;
        Contact haukur = new Contact(userid, "Haukur", false);
        contactsManager.addContact(haukur);
        Contact c = contactsManager.getContactById(userid);

        if(c != null) {
            resultsTextView.setText(c.getUsername());
        } else {
            resultsTextView.setText("Could not find contact");
        }


    }
}
