package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransportLine extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.testsmartkl";
    ListView listViewTransportLine;
    List<Transport> tpList;
    TextView textViewMessage;
    private ProgressDialog pDialog;
    private ProgressDialog pDialog1;
    //TODO: Please update the URL to point to your own server
    private static String SEARCH_URL = "https://circumgyratory-gove.000webhostapp.com/search_transportLine.php";
    private static String SEARCH_URL1 = "https://circumgyratory-gove.000webhostapp.com/search_Schedule.php";
    RequestQueue queue;
    RequestQueue queue1;
    List<Schedule> tsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_line);

        String transportType = getIntent().getStringExtra("TransportType");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listViewTransportLine = (ListView) findViewById(R.id.listViewTransportLine);
        pDialog = new ProgressDialog(this);
        pDialog1 = new ProgressDialog(this);
        tpList = new ArrayList<>();
        tsList = new ArrayList<>();

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }
        searchTransport(getApplicationContext(),transportType);

        listViewTransportLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String uri = tpList.get(position).getTransportSchedule();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                //find an activity to hand the intent and start that activity.
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }else{
                    Log.d("ImplicitIntents", "Can't handle this intent!");
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void searchTransport(Context context, String transportType) {
        queue = Volley.newRequestQueue(context);
        String url = SEARCH_URL + "?TransportType=" + transportType;

        if (!pDialog.isShowing())
            pDialog.setMessage("Searching...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            tpList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject transportResponse = (JSONObject) response.get(i);
                                int transportId = Integer.parseInt(transportResponse.getString("TransportID"));
                                String transportLine = transportResponse.getString("TransportLine");
                                String transportSchedule = transportResponse.getString("TransportSchedule");
                                Transport transport = new Transport(transportId,transportLine,transportSchedule);
                                tpList.add(transport);
                            }
                            loadTransport();
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

    private void loadTransport() {
        final TransportAdapter adapter = new TransportAdapter(this, R.layout.activity_transport_line, tpList);
        listViewTransportLine.setAdapter(adapter);
        if(tpList != null){
            int size = tpList.size();
            if(size > 0)
                Toast.makeText(getApplicationContext(), "No. of record : " + size + ".", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "No record found.", Toast.LENGTH_SHORT).show();
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
