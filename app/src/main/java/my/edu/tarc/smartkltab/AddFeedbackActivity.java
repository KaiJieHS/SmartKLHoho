package my.edu.tarc.smartkltab;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddFeedbackActivity extends AppCompatActivity {
    EditText editTextSubject, editTextDesc;
    RadioGroup radioGroupType;
    RadioButton buttonSug, buttonPro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextSubject = findViewById(R.id.editTextSubject);
        editTextDesc =  findViewById(R.id.editTextDesc);
        buttonSug = findViewById(R.id.radioButton3);
        buttonPro = findViewById(R.id.radioButton4);
        radioGroupType = findViewById(R.id.radioGroupType);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveRecord(View v) {
        Feedback feedback = new Feedback();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String timeDate = df.format(currentTime);
        String type="";
        if(radioGroupType.getCheckedRadioButtonId() == R.id.radioButton3){
            type="Suggestion";
        }else if(radioGroupType.getCheckedRadioButtonId() == R.id.radioButton4){
            type="Problems";
        }
        int citizenid= Integer.parseInt(getIntent().getStringExtra("currentid2"));
        feedback.setFeedbackType(type);
        feedback.setSubject(editTextSubject.getText().toString());
        feedback.setDescription(editTextDesc.getText().toString());
        feedback.setDate(timeDate);
        feedback.setCitizenID(1);
        feedback.setStatus("waiting");

        try {
            //TODO: Please update the URL to point to your own server
            addFeedback(this, "https://circumgyratory-gove.000webhostapp.com/insert_feedback.php", feedback);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addFeedback(Context context, String url, final Feedback feedback) {
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
                    params.put("FeedbackTitle", feedback.getSubject());
                    params.put("FeedbackDesc", feedback.getDescription());
                    params.put("FeedbackType", feedback.getFeedbackType());
                    params.put("FeedbackDate", feedback.getDate());
                    params.put("CitizenID", String.valueOf(feedback.getCitizenID()));
                    params.put("Status", feedback.getStatus());
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
