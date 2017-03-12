


package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by arnardesktop on 5.3.2017.
 */

public class NewFriendsRequestsAdapter extends BaseAdapter {

    Object[] data;
    Context context;

    LayoutInflater layoutInflater;

    public NewFriendsRequestsAdapter(Object[] data, Context context) {
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

        convertView= layoutInflater.inflate(R.layout.friend_requests_list_item, parent, false);

        TextView nameView=(TextView)convertView.findViewById(R.id.friendRequestNameField);
        nameView.setText((String)thisData[0]);

        Button acceptFriendRequestButton = (Button) convertView.findViewById(R.id.acceptFriendRequestButton);
        Button declineFriendRequestButton = (Button) convertView.findViewById(R.id.declineFriendRequestButton);


        acceptFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Accepting friend request.", Toast. LENGTH_SHORT).show();
            }
        });

        declineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Declining friend request.", Toast. LENGTH_SHORT).show();
            }
        });

        return convertView;
    }


}
