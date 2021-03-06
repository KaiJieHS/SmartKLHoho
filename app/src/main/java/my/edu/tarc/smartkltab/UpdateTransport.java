package my.edu.tarc.smartkltab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateTransport extends AppCompatActivity {

    String items[] = new String [] {"TransportLine","TransportSchedule"};
    EditText editTextUpdateContent;
    Spinner spinnerUpdateItem;
    String updateItem,updateContent;
    int TransportID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transport);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TransportID = getIntent().getIntExtra("TransportID",0);

        spinnerUpdateItem = findViewById(R.id.spinnerUpdateItem);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,items);
        spinnerUpdateItem.setAdapter(adapter);

        editTextUpdateContent = findViewById(R.id.editTextUpdateContent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void updateRecordT(View v) {

        if(editTextUpdateContent.getText().toString().matches("")){
            Toast.makeText(this,"Please enter the Update Content",Toast.LENGTH_SHORT).show();
            return;

        }else {

            updateItem = spinnerUpdateItem.getSelectedItem().toString();
            updateContent = editTextUpdateContent.getText().toString();

            try {
                //TODO: Please update the URL to point to your own server
                updateOrganization(this, "https://circumgyratory-gove.000webhostapp.com/update_transport.php", TransportID, updateItem, updateContent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    public void updateOrganization(Context context, String url, int id, String updateItem, String updateContent) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        url = url + "?TransportID=" + id + "&UpdateItem=" + updateItem + "&UpdateContent=" + updateContent;

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
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(UpdateTransport.this,TransportActivity.class);
                                    startActivity(intent);
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
                /*@Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    //params.put("TransportType", transport.getTransportType());
                    //params.put("TransportLine", transport.getTransportLine());
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
}
