package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by arnardesktop on 5.3.2017.
 */

public class RecentConversationsAdapter extends BaseAdapter {

    Object[] data;
    Context context;

    LayoutInflater layoutInflater;

    public RecentConversationsAdapter(Object[] data, Context context) {
        super();
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return data.length;
    }

    @Override
    public Object getItem(int position) {

        return data[position];
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] thisData = (Object[]) data[position];

        convertView= layoutInflater.inflate(R.layout.recent_conversations_list_item, parent, false);

        TextView nameView=(TextView)convertView.findViewById(R.id.nameField);
        nameView.setText((String)thisData[0]);

        TextView timeView=(TextView)convertView.findViewById(R.id.timeField);
        timeView.setText((String)thisData[1]);

        TextView messageView=(TextView)convertView.findViewById(R.id.messageField);
        messageView.setText((String)thisData[0]+" says: "+(String)thisData[2]);



        return convertView;
    }


}
