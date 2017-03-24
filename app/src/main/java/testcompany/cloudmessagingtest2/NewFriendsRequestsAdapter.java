


package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by arnardesktop on 5.3.2017.
 */

public class NewFriendsRequestsAdapter extends BaseAdapter {

    List<Contact> data;
    Context context;

    LayoutInflater layoutInflater;

    public NewFriendsRequestsAdapter(List<Contact> data, Context context) {
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
        final int pos = position;
        final Contact thisData =  data.get(position);

        convertView= layoutInflater.inflate(R.layout.friend_requests_list_item, parent, false);

        TextView nameView=(TextView)convertView.findViewById(R.id.friendRequestNameField);
        nameView.setText(thisData.getUsername());

        Button acceptFriendRequestButton = (Button) convertView.findViewById(R.id.acceptFriendRequestButton);
        Button declineFriendRequestButton = (Button) convertView.findViewById(R.id.declineFriendRequestButton);


        acceptFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsManager contactManager = new ContactsManager(context);
                contactManager.acceptRequest(thisData);

                data.remove(pos);
                notifyDataSetChanged();
            }
        });

        declineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsManager contactManager = new ContactsManager(context);
                contactManager.declineRequest(thisData);

                data.remove(pos);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


}
