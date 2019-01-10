package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HealthCareAdapter extends ArrayAdapter<HealthCare> {
    public HealthCareAdapter(Activity context, int resource, List<HealthCare> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HealthCare healthcare = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.healthcare_record, parent, false);

        TextView textViewHcBranchName, textViewHcBranchLocation, textViewHcContactNumber;

        textViewHcBranchName = (TextView)rowView.findViewById(R.id.textViewHcBranchName);
        textViewHcBranchLocation = (TextView)rowView.findViewById(R.id.textViewHcBranchLocation);
        textViewHcContactNumber = (TextView)rowView.findViewById(R.id.textViewHcContactNumber);
        textViewHcBranchName.setText(String.format("%s" ,healthcare.getHcBranchName()));
        textViewHcBranchLocation.setText(String.format("%s" ,healthcare.getHcBranchLocation()));
        textViewHcContactNumber.setText(String.format("%s" ,healthcare.getHcContactNumber()));
        return rowView;
    }
}
