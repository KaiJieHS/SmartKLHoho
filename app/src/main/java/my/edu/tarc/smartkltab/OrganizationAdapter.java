package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrganizationAdapter extends ArrayAdapter<Organization> {
    public OrganizationAdapter(Activity context, int resource, List<Organization> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Organization organization = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.organization_record, parent, false);

        TextView textViewOrgName, textViewOrgBranchLocation, textViewOrgContactNum;

        textViewOrgName = (TextView)rowView.findViewById(R.id.textViewOrgName);
        textViewOrgBranchLocation = (TextView)rowView.findViewById(R.id.textViewOrgBranchLocation);
        textViewOrgContactNum = (TextView)rowView.findViewById(R.id.textViewOrgContactNum);
        textViewOrgName.setText(String.format("%s" ,organization.getOrgName()));
        textViewOrgBranchLocation.setText(String.format("%s" ,organization.getOrgBranchLocation()));
        textViewOrgContactNum.setText(String.format("%s" ,organization.getOrgContactNum()));
        return rowView;
    }
}
