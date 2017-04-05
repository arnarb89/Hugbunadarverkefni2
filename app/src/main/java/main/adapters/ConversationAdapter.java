


package main.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.managers.ContactManager;
import main.managers.MessageManager;
import main.managers.PreferencesHelper;
import main.model.Contact;
import main.model.Message;
import testcompany.cloudmessagingtest2.R;

/**
 * Created by arnardesktop on 5.3.2017.
 */

public class ConversationAdapter extends BaseAdapter {

    List<Message> data;
    Context context;
    //boolean isLoading = false;

    LayoutInflater layoutInflater;

    int IdOfWhoYouAreTalkingTo;
    int yourId;

    public ConversationAdapter(List<Message> data, Context context, int IdOfWhoYouAreTalkingTo, int yourId) {
        super();
        this.data = data;
        this.context = context;
        this.IdOfWhoYouAreTalkingTo = IdOfWhoYouAreTalkingTo;
        this.yourId = yourId;
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
        Message thisData = data.get(position);
        int yourId = PreferencesHelper.getUserId(context);

        if(thisData.getSenderId()==yourId){
            convertView= layoutInflater.inflate(R.layout.conversation_you_list_item, parent, false);
        }else{
            convertView= layoutInflater.inflate(R.layout.conversation_to_you_list_item, parent, false);
        }


        TextView timeView=(TextView)convertView.findViewById(R.id.timeTextView);
        SimpleDateFormat spf = new SimpleDateFormat("HH:mm, EEE, dd MMM ''yy");
        String newDateString = spf.format(thisData.getSentDate());
        timeView.setText(newDateString);

        TextView messageView=(TextView)convertView.findViewById(R.id.messageTextView);
        messageView.setText(thisData.getContent());

        if(reachedEndOfList(position)) loadMoreData();

        return convertView;
    }

    private boolean reachedEndOfList(int position) {
        // can check if close or exactly at the end
        return position == getCount() - 1;
    }

    private void loadMoreData() {
        // Perhaps set flag to indicate you're loading and check flag before proceeding with AsyncTask or whatever
        Log.i("testing", "***** ConversationAdapter, trying to load more data.");
        MessageManager messageManager = new MessageManager(context);
        ContactManager contactManager = new ContactManager(context);
        Contact yourContact = contactManager.getContactById(IdOfWhoYouAreTalkingTo);
        List<Message> newMessages;
        if(getCount()==0){
            newMessages = messageManager.getPreviousMessages( yourId, yourContact, new Date() );
        }else{
            newMessages = messageManager.getPreviousMessages( yourId, yourContact, data.get(getCount()-1).getSentDate() );
        }
        data.addAll(newMessages);
        notifyDataSetChanged();
    }

}