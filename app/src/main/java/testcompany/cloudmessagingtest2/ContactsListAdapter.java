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

public class ContactsListAdapter extends BaseAdapter {

    Object[] data;
    Context context;

    LayoutInflater layoutInflater;

    public ContactsListAdapter(Object[] data, Context context) {
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

        convertView= layoutInflater.inflate(R.layout.contacts_list_element, parent, false);

        TextView nameView=(TextView)convertView.findViewById(R.id.contactsListElementNameField);
        nameView.setText((String)thisData[0]);

        return convertView;
    }


}