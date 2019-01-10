package my.edu.tarc.smartkltab;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UpdateHealthCare extends AppCompatActivity {

    String items[] = new String [] {"HcBranchName","HcBranchLocation","HcContactNum"};
    EditText editTextUpdateContent;
    Spinner spinnerUpdateItem;
    String updateItem,updateContent,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_health_care);

        spinnerUpdateItem = findViewById(R.id.spinnerUpdateItem);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,items);
        spinnerUpdateItem.setAdapter(adapter);

        editTextUpdateContent = findViewById(R.id.editTextUpdateContent);
    }

    public void updateRecordHc(View v) {

        updateItem = spinnerUpdateItem.getSelectedItem().toString();
        updateContent = editTextUpdateContent.getText().toString();

        try {
            //TODO: Please update the URL to point to your own server
            updateHealthCare(this, "https://circumgyratory-gove.000webhostapp.com/update_healthcare.php",id, updateItem, updateContent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateHealthCare(Context context, String url,String id, String updateItem, String updateContent) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        url = url + "?HcID=" + id + "&UpdateItem=" + updateItem + "&UpdateContent=" + updateContent;

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
