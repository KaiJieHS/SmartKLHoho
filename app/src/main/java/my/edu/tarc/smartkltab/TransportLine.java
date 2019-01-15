package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
import java.util.Objects;

import static android.view.View.VISIBLE;

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
    private List<Transport> UserSelection = new ArrayList<>();
    MenuItem updateButton;
    private TransportAdapter transportAdapter;
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";
    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_line);

        String transportType = getIntent().getStringExtra("TransportType");

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        setTitle("Transport Line");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listViewTransportLine = (ListView) findViewById(R.id.listViewTransportLine);
        pDialog = new ProgressDialog(this);
        pDialog1 = new ProgressDialog(this);
        tpList = new ArrayList<>();
        tsList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }


        transportAdapter = new TransportAdapter(this,0,tpList);
        listViewTransportLine.setAdapter(transportAdapter);

        if(sharedPreferences.getString("usertype","").equals("admin")){
            listViewTransportLine.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            listViewTransportLine.setMultiChoiceModeListener(modeListener);
        }

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

        searchTransport(getApplicationContext(),transportType);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if(UserSelection.contains(tpList.get(position))){
                UserSelection.remove(tpList.get(position));
            }else{
                UserSelection.add(tpList.get(position));
            }
            if(UserSelection.size()!=1){
                updateButton.setVisible(false);
            }else{
                updateButton.setVisible(true);
            }

            mode.setTitle(UserSelection.size() + " items selected...");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            updateButton = menu.findItem(R.id.action_update);
            if(UserSelection.size()!=1){
                updateButton.setVisible(false);
            }else{
                updateButton.setVisible(true);
            }
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:
                    transportAdapter.removeItems(UserSelection);
                    mode.finish();
                    return true;
                case R.id.action_update:
                    transportAdapter.updateItems(UserSelection);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            UserSelection.clear();
        }
    };

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
