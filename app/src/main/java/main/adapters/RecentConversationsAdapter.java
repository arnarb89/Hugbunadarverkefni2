package main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import main.managers.ContactManager;
import main.managers.PreferencesHelper;
import main.model.Message;
import testcompany.cloudmessagingtest2.R;

/**
 * Created by arnardesktop on 5.3.2017.
 */

public class RecentConversationsAdapter extends BaseAdapter {

    ContactManager contactManager;

    List<Message> data;
    Context context;

    LayoutInflater layoutInflater;

    public RecentConversationsAdapter(List<Message> data, Context context) {
        super();
        contactManager = new ContactManager(context);
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
        int yourId = PreferencesHelper.getUserId(context);

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

        SimpleDateFormat spf = new SimpleDateFormat("EEE HH:mm, dd MMM ''yy");
        String newDateString = spf.format(thisData.getSentDate());

        timeView.setText(newDateString);






        return convertView;
    }


}
