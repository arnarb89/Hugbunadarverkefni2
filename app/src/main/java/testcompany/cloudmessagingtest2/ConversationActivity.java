package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class ConversationActivity extends Activity {

    Button sendMessageButton;
    Button backButton;
    Button contactNameButton;

    ListView conversationListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        backButton = (Button) findViewById(R.id.btnRecentConversations);
        contactNameButton = (Button) findViewById(R.id.conversationNameField);

        conversationListView = (ListView) findViewById(R.id.conversationListView);

        // TODO: dummy test data, can delete after more of the project is finished
        final Object[] theData = new Object[]{
                new Object[]{"You","13:37","I am da best."},
                new Object[]{"Jónas","14:37","I am an idiot."} ,
                new Object[]{"You","15:37","I am also an idiot."},
                new Object[]{"You","16:37","I am also an idiot. asdlf iasdf asdf awer asdf wer asdf awert asdf awer asdf waera asdf wer asdf awer asdf awer asdf awer asdf awer asdf awerasdf awer asdf awer asdf awer asdf"} ,
                new Object[]{"Jónas","17:37","I am also an idiot. asdlf iasdf asdf awer asdf wer asdf awert asdf awer asdf waera asdf wer asdf awer asdf awer asdf awer asdf awer asdf awerasdf awer asdf awer asdf awer asdf"},
                new Object[]{"You","18:37","I am also an idiot."} ,
                new Object[]{"Jónas","19:37","I am also an idiot."},
                new Object[]{"Jónas","20:37","I am also an idiot."} ,
                new Object[]{"You","21:37","I am also an idiot."},
                new Object[]{"Jónas","22:37","I am also an idiot."} ,
                new Object[]{"You","23:37","I am also an idiot."}
        };

        // Populate the conversation list with items
        ConversationAdapter conversationAdapter = new ConversationAdapter(theData, this.getBaseContext());
        conversationListView.setAdapter(conversationAdapter);






        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sending message.", Toast. LENGTH_SHORT).show();
            }
        });

        contactNameButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popup = new PopupMenu(ConversationActivity.this, v, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.block_or_remove_popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(ConversationActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
