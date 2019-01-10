package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TransportAdapter extends ArrayAdapter<Transport> {
    public TransportAdapter(Activity context, int resource, List<Transport> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transport transport = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.transportline_record, parent, false);

        TextView textViewTransportLine;

        textViewTransportLine = (TextView)rowView.findViewById(R.id.textViewTransportLine);
        textViewTransportLine.setText(String.format("%s" ,transport.getTransportLine()));
        return rowView;
    }
}
