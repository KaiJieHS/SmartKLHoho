package my.edu.tarc.smartkltab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddHealthCareActivity extends AppCompatActivity {

    EditText editTextName, editTextAddress, editTextContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_care);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextName = findViewById(R.id.editTextName);
        editTextAddress =  findViewById(R.id.editTextAddress);
        editTextContact = findViewById(R.id.editTextContact);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveRecordHc(View v) {
        HealthCare healthcare = new HealthCare();

        if(editTextName.getText().toString().matches("")){
            Toast.makeText(this,"Please enter the Hospital Name",Toast.LENGTH_SHORT).show();
            return;
        }else if(editTextAddress.getText().toString().matches("")){
            Toast.makeText(this,"Please enter the Address",Toast.LENGTH_SHORT).show();
            return;
        }else if(editTextContact.getText().toString().matches("")){
            Toast.makeText(this,"Please enter the Contact Number",Toast.LENGTH_SHORT).show();
            return;
        }else {

            healthcare.setHcBranchName(editTextName.getText().toString());
            healthcare.setHcBranchLocation(editTextAddress.getText().toString());
            healthcare.setHcContactNumber(editTextContact.getText().toString());


            try {
                //TODO: Please update the URL to point to your own server
                addHealthCare(this, "https://circumgyratory-gove.000webhostapp.com/insert_healthcare.php", healthcare);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addHealthCare(Context context, String url, final HealthCare healthcare) {
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
                                    Intent intent = new Intent(AddHealthCareActivity.this,HealthCareActivity.class);
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
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("HcBranchName", healthcare.getHcBranchName());
                    params.put("HcBranchLocation", healthcare.getHcBranchLocation());
                    params.put("HcContactNum", healthcare.getHcContactNumber());
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
