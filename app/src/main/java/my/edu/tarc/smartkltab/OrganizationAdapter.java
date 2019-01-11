package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrganizationAdapter extends ArrayAdapter<Organization> {

    private List<Organization> organization = new ArrayList<>();
    private Activity context;

    public OrganizationAdapter(Activity context, int resource, List<Organization> list) {
        super(context, resource, list);
        this.organization = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Organization org = organization.get(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.organization_record, parent, false);

        TextView textViewOrgName, textViewOrgBranchLocation, textViewOrgContactNum;

        textViewOrgName = (TextView)rowView.findViewById(R.id.textViewOrgName);
        textViewOrgBranchLocation = (TextView)rowView.findViewById(R.id.textViewOrgBranchLocation);
        textViewOrgContactNum = (TextView)rowView.findViewById(R.id.textViewOrgContactNum);
        textViewOrgName.setText(String.format("%s" ,org.getOrgName()));
        textViewOrgBranchLocation.setText(String.format("%s" ,org.getOrgBranchLocation()));
        textViewOrgContactNum.setText(String.format("%s" ,org.getOrgContactNum()));
        return rowView;
    }

    public void removeItems(List<Organization> items){
        for(Organization item:items){
            organization.remove(item);
            deleteOrganization(context, "https://circumgyratory-gove.000webhostapp.com/delete_organization.php",item);
        }
        notifyDataSetChanged();
    }

    public void deleteOrganization(final Context context, String url,Organization item) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        url = url + "?OrganizationID=" + item.getOrganizationID();

        //Send data
        try {
            StringRequest getRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success==0) {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                /*@Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    //params.put("TransportType", transport.getTransportType());
                    //params.put("TransportLine", transport.getTransportLine());
                    //params.put("TransportSchedule", transport.getTransportSchedule());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }*/
            };
            queue.add(getRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItems(List<Organization> items) {
        for(Organization item:items){
            toUpdate(item);
        }

        notifyDataSetChanged();
    }



    public void toUpdate(Organization item){

        Intent intent = new Intent(context,UpdateOrganization.class);
        intent.putExtra("OrganizationID", item.getOrganizationID());

        context.startActivity(intent);
    }
}
