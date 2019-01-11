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

public class TransportAdapter extends ArrayAdapter<Transport> {

    private List<Transport> transports = new ArrayList<>();
    private Activity context;

    public TransportAdapter(Activity context, int resource, List<Transport> list) {
        super(context, resource, list);
        this.transports = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transport transport = transports.get(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.transportline_record, parent, false);

        TextView textViewTransportLine;

        textViewTransportLine = (TextView)rowView.findViewById(R.id.textViewTransportLine);
        textViewTransportLine.setText(String.format("%s" ,transport.getTransportLine()));
        return rowView;
    }

    public void removeItems(List<Transport> items){
        for(Transport item:items){
            transports.remove(item);
            deleteTransport(context, "https://circumgyratory-gove.000webhostapp.com/delete_transport.php",item);
        }
        notifyDataSetChanged();
    }

    public void deleteTransport(final Context context, String url,Transport item) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        url = url + "?TransportID=" + item.getTransportID();

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

    public void updateItems(List<Transport> items) {
        for(Transport item:items){
            toUpdate(item);
        }

        notifyDataSetChanged();
    }



    public void toUpdate(Transport item){

        Intent intent = new Intent(context,UpdateTransport.class);
        intent.putExtra("TransportID", item.getTransportID());

        context.startActivity(intent);
    }
}
