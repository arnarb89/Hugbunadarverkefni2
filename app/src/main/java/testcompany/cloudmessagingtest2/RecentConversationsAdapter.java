package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by arnardesktop on 5.3.2017.
 */

public class RecentConversationsAdapter extends BaseAdapter {

    ContactsManager contactManager;

    List<Message> data;
    Context context;

    LayoutInflater layoutInflater;

    public RecentConversationsAdapter(List<Message> data, Context context) {
        super();
        contactManager = new ContactsManager(context);
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message thisData =  data.get(position);
        int yourId = PreferencesManager.getUserId(context);

        convertView= layoutInflater.inflate(R.layout.recent_conversations_list_item, parent, false);

        TextView nameView=(TextView)convertView.findViewById(R.id.nameField);
        TextView timeView=(TextView)convertView.findViewById(R.id.timeField);
        TextView messageView=(TextView)convertView.findViewById(R.id.messageField);

        if(thisData.getSenderId()==yourId){
            nameView.setText(contactManager.getContactById(thisData.getReceiverId()).getUsername());
            messageView.setText("You said: "+thisData.getContent());
        }else{
            nameView.setText(contactManager.getContactById(thisData.getSenderId()).getUsername());
            messageView.setText(contactManager.getContactById(thisData.getSenderId()).getUsername()+" says: "+thisData.getContent());
        }


        timeView.setText(thisData.getSentDate().toString());






        return convertView;
    }


}
