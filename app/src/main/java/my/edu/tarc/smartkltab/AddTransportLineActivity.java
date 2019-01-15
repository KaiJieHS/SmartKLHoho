package my.edu.tarc.smartkltab;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddTransportLineActivity extends AppCompatActivity {

    String items[] = new String [] {"KTM","LRT","Bus"};
    EditText editTextTransportLine, editTextScheduleURL;
    Spinner spinnerType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transport_line);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spinnerType = findViewById(R.id.spinnerTransportType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,items);
        spinnerType.setAdapter(adapter);

        editTextTransportLine = findViewById(R.id.editTextTransportLine);
        editTextScheduleURL =  findViewById(R.id.editTextScheduleURL);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveRecordTl(View v) {
        Transport transport = new Transport();

        if(editTextTransportLine.getText().toString().matches("")){
            Toast.makeText(this,"Please enter the Transport Line",Toast.LENGTH_SHORT).show();
            return;
        }else if(editTextScheduleURL.getText().toString().matches("")){
            Toast.makeText(this,"Please enter the URL",Toast.LENGTH_SHORT).show();
            return;

        }else {
            transport.setTransportType(spinnerType.getSelectedItem().toString());
            transport.setTransportLine(editTextTransportLine.getText().toString());
            transport.setTransportSchedule(editTextScheduleURL.getText().toString());


            try {
                //TODO: Please update the URL to point to your own server
                addTransport(this, "https://circumgyratory-gove.000webhostapp.com/insert_transport.php", transport);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addTransport(Context context, String url, final Transport transport) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
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
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("TransportType", transport.getTransportType());
                    params.put("TransportLine", transport.getTransportLine());
                    params.put("TransportSchedule", transport.getTransportSchedule());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
