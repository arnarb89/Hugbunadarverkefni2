package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class ConversationActivity extends Activity {

    Button sendMessageButton;
    Button backButton;
    Button contactNameButton;

    EditText messageInputField;

    ListView conversationListView;

    ContactsManager contactManager;
    MessageManager messageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        final int IdOfWhoYouAreTalkingTo = getIntent().getIntExtra("KEY_contactId",0);
        final int yourId = PreferencesManager.getUserId(ConversationActivity.this);

        contactManager = new ContactsManager(ConversationActivity.this);
        messageManager = new MessageManager(ConversationActivity.this);

        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        backButton = (Button) findViewById(R.id.btnRecentConversations);
        contactNameButton = (Button) findViewById(R.id.conversationNameField);

        messageInputField = (EditText) findViewById(R.id.messageEditText);

        conversationListView = (ListView) findViewById(R.id.conversationListView);

        //
        List<Message> previousMessages =  messageManager.getPreviousMessages(yourId, contactManager.getContactById(IdOfWhoYouAreTalkingTo), new Date());

        // Populate the conversation list with items
        ConversationAdapter conversationAdapter = new ConversationAdapter(previousMessages, this.getBaseContext());
        conversationListView.setAdapter(conversationAdapter);






        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sending message.", Toast. LENGTH_SHORT).show();
                String content = messageInputField.getText().toString();
                int senderId = yourId;
                int receiverId = IdOfWhoYouAreTalkingTo;
                Date sentTime = new Date();
                Message message = new Message(content, senderId, receiverId, sentTime);
                // TODO: vantar messageManager.sendMessage(message);
                // TODO: vantar update conversation, kannski update-að frá MessageManager...
            }
        });

        contactNameButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popup = new PopupMenu(ConversationActivity.this, v, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.block_contact){
                            contactManager.blockContact(contactManager.getContactById(IdOfWhoYouAreTalkingTo));
                            finish();
                        }else{
                            contactManager.deleteContact(contactManager.getContactById(IdOfWhoYouAreTalkingTo));
                            finish();
                        }

                        return true;
                    }
                });


                popup.show();

                return true;
            }
        });


        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
