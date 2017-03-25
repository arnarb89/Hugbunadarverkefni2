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

public class ContactsListAdapter extends BaseAdapter {

    List<Contact> data;
    Context context;

    LayoutInflater layoutInflater;

    public ContactsListAdapter(List<Contact> data, Context context) {
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
        Contact thisData = data.get(position);

        convertView= layoutInflater.inflate(R.layout.contacts_list_element, parent, false);

        TextView nameView=(TextView)convertView.findViewById(R.id.contactsListElementNameField);
        nameView.setText(thisData.getUsername());

        return convertView;
    }


}