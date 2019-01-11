package my.edu.tarc.smartkltab;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewResponseActivity extends AppCompatActivity {
    public static final String TAG = "my.edu.tarc.testsmartkl";
    ListView listViewResponse;
    List<FeedbackResponses> resList;
    private ProgressDialog pDialog;
    private EditText editTextRepDesc;
    private TextView textViewS, textViewD;
    private SharedPreferences sharedPreferences;
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";

    //TODO: Please update the URL to point to your own server
    private static String SEARCH_URL = "https://circumgyratory-gove.000webhostapp.com/search_feedbackresponse.php";
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_response);
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listViewResponse = (ListView) findViewById(R.id.listViewResponse);
        pDialog = new ProgressDialog(this);
        resList = new ArrayList<>();
        textViewS = findViewById(R.id.textViewS);
        textViewD = findViewById(R.id.textViewD);
        textViewS.setText("Subject: " + getIntent().getStringExtra("subject"));
        textViewD.setText("Description: " +getIntent().getStringExtra("desc"));

        int searchfeedbackid= Integer.parseInt(getIntent().getStringExtra("currentFeedbackID"));
        searchResponse(getApplicationContext(), searchfeedbackid);

        }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("ResourceAsColor")
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_addreply, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.R.color.white));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editTextRepDesc=(EditText)popupView.findViewById(R.id.editTextDesc);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }

    public void saveRecord(View v) {
        int currentid = sharedPreferences.getInt("id", 0);
        int searchfeedbackid= Integer.parseInt(getIntent().getStringExtra("currentFeedbackID"));
        FeedbackResponses feedbackres = new FeedbackResponses();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String timeDate = df.format(currentTime);

        if (editTextRepDesc.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter a description", Toast.LENGTH_SHORT).show();
            return;
        }else{
        feedbackres.setFeedbackID(searchfeedbackid);
        feedbackres.setResponseDesc(editTextRepDesc.getText().toString());
        feedbackres.setResponseDate(timeDate);

        if(sharedPreferences.getString("usertype", "").equals("admin")) {
            feedbackres.setOfficerID(currentid);
        }

        try {
            //TODO: Please update the URL to point to your own server
            if(sharedPreferences.getString("usertype", "").equals("admin")) {
                addResponse(this, "https://circumgyratory-gove.000webhostapp.com/insert_response.php", feedbackres);
            }else{
                addResponse(this, "https://circumgyratory-gove.000webhostapp.com/insert_response1.php", feedbackres);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    }

    public void addResponse(Context context, String url, final FeedbackResponses feedbackres) {
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
                    params.put("FeedbackID", String.valueOf(feedbackres.getFeedbackID()));
                    params.put("RespDesc", feedbackres.getResponseDesc());
                    if(sharedPreferences.getString("usertype", "").equals("admin")){
                        params.put("OfficerID", String.valueOf(feedbackres.getOfficerID()));
                    }
                    params.put("RespDate", feedbackres.getResponseDate());
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


    private void searchResponse(Context context, int id) {
        queue = Volley.newRequestQueue(context);
        String url = SEARCH_URL + "?FeedbackID=" + id;

        if (!pDialog.isShowing())
            pDialog.setMessage("Searching...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            resList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject respResponse = (JSONObject) response.get(i);
                                int fbid = respResponse.getInt("FeedbackID");
                                int resid = respResponse.getInt("RespID");
                                String desc = respResponse.getString("RespDesc");
                                String date = respResponse.getString("RespDate");
                                int ofid = respResponse.optInt("OfficerID");
                                FeedbackResponses resp = new FeedbackResponses(resid, desc, date, ofid, fbid);
                                resList.add(resp);
                            }
                            loadResponse();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void loadResponse() {
        final ResponseAdapter adapter = new ResponseAdapter(this, R.layout.activity_view_response, resList);
        listViewResponse.setAdapter(adapter);
        if(resList != null){
            int size = resList.size();
            if(size > 0)
                Toast.makeText(getApplicationContext(), "No. of record : " + size + ".", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "No response found.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

}
