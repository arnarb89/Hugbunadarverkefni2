


package main.adapters;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import java.text.SimpleDateFormat;
        import java.util.List;

        import main.model.Message;
        import main.managers.PreferencesHelper;
        import testcompany.cloudmessagingtest2.R;

/**
 * Created by arnardesktop on 5.3.2017.
 */

public class ConversationAdapter extends BaseAdapter {

    List<Message> data;
    Context context;

    LayoutInflater layoutInflater;

    public ConversationAdapter(List<Message> data, Context context) {
        super();
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
        Message thisData = data.get(position);
        int yourId = PreferencesHelper.getUserId(context);

        if(thisData.getSenderId()==yourId){
            convertView= layoutInflater.inflate(R.layout.conversation_you_list_item, parent, false);
        }else{
            convertView= layoutInflater.inflate(R.layout.conversation_to_you_list_item, parent, false);
        }


        TextView timeView=(TextView)convertView.findViewById(R.id.timeTextView);
        SimpleDateFormat spf = new SimpleDateFormat("EEE HH:mm, dd MMM ''yy");
        String newDateString = spf.format(thisData.getSentDate());
        timeView.setText(newDateString);

        TextView messageView=(TextView)convertView.findViewById(R.id.messageTextView);
        messageView.setText(thisData.getContent());



        return convertView;
    }


}