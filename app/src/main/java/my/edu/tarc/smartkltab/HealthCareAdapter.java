package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

public class HealthCareAdapter extends ArrayAdapter<HealthCare> {
    private List<HealthCare> healthCare = new ArrayList<>();
    private Activity context;

    public HealthCareAdapter(Activity context, int resource, List<HealthCare> list) {
        super(context, resource, list);
        this.healthCare = list;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HealthCare healthcare = healthCare.get(position);

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

    public void removeItems(List<HealthCare> items){
        for(HealthCare item:items){
            healthCare.remove(item);
            deleteHealthCare(context, "https://circumgyratory-gove.000webhostapp.com/delete_healthcare.php",item);
        }
        notifyDataSetChanged();
    }

    public void deleteHealthCare(final Context context, String url,HealthCare item) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        url = url + "?HcID=" + item.getHcID();

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

    public void updateItems(List<HealthCare> items) {
        for(HealthCare item:items){
            toUpdate(item);
        }

        notifyDataSetChanged();
    }



    public void toUpdate(HealthCare item){

        Intent intent = new Intent(context,UpdateHealthCare.class);
        intent.putExtra("HcID", item.getHcID());

        context.startActivity(intent);
    }
}
