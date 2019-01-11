package my.edu.tarc.smartkltab;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.VISIBLE;

public class HealthCareActivity extends AppCompatActivity{

    public static final String TAG = "my.edu.tarc.testsmartkl";
    ListView listViewHealthCare;
    private HealthCareAdapter healthcareAdapter;
    List<HealthCare> hcList;
    private ProgressDialog pDialog;
    //TODO: Please update the URL to point to your own server
    private static String GET_URL = "https://circumgyratory-gove.000webhostapp.com/search_healthcare.php";
    private static String SEARCH_URL;
    private List<HealthCare> UserSelection = new ArrayList<>();
    MenuItem updateButton;

    RequestQueue queue;
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";
    private SharedPreferences sharedPreferences;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_care);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        listViewHealthCare = (ListView) findViewById(R.id.listViewHealthCare);
        pDialog = new ProgressDialog(this);
        hcList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }
        downloadHealthCare(getApplicationContext(), GET_URL);

       healthcareAdapter = new HealthCareAdapter(this,0,hcList);
       listViewHealthCare.setAdapter(healthcareAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthCareActivity.this, AddHealthCareActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(sharedPreferences.getString("usertype","").equals("admin")){
            listViewHealthCare.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            listViewHealthCare.setMultiChoiceModeListener(modeListener);
            fab.setVisibility(VISIBLE);
        }

        listViewHealthCare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String uri = hcList.get(position).getHcBranchLocation();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(Uri.parse("geo:0,0?q="+uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }


            }
        });

    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if(UserSelection.contains(hcList.get(position))){
                 UserSelection.remove(hcList.get(position));
            }else{
                UserSelection.add(hcList.get(position));
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
                    healthcareAdapter.removeItems(UserSelection);
                    mode.finish();
                    return true;
                case R.id.action_update:
                    healthcareAdapter.updateItems(UserSelection);
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_healthcare, menu);

        return true;
    }*/


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //here to add action for the apps bar
        if (id == R.id.action_setting) {
            Intent intent = new Intent(this, UpdateHealthCare.class);
            startActivity(intent);

            //Toast.makeText(MainActivity.this, "Search Button Is Clicked.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void searchHealthCare(Context context, int HcID) {
        queue = Volley.newRequestQueue(context);
        String url = SEARCH_URL + "?HcID=" + HcID;

        if (!pDialog.isShowing())
            pDialog.setMessage("Searching...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            hcList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject healthCareResponse = (JSONObject) response.get(i);
                                int hcID = Integer.parseInt(healthCareResponse.getString("HcID"));
                                String hcBranchName = healthCareResponse.getString("HcBranchName");
                                String hcBranchLocation = healthCareResponse.getString("HcBranchLocation");
                                String hcContactNumber = healthCareResponse.getString("HcContactNum");
                                HealthCare healthcare = new HealthCare(hcID,hcBranchName,hcBranchLocation,hcContactNumber);
                                hcList.add(healthcare);
                            }
                            loadHealthCare();
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
    private void downloadHealthCare(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog.isShowing())
            pDialog.setMessage("Sync with server...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            hcList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject healthCareResponse = (JSONObject) response.get(i);
                                int hcID = Integer.parseInt(healthCareResponse.getString("HcID"));
                                String hcBranchName = healthCareResponse.getString("HcBranchName");
                                String hcBranchLocation = healthCareResponse.getString("HcBranchLocation");
                                String hcContactNumber = healthCareResponse.getString("HcContactNum");
                                HealthCare healthcare = new HealthCare(hcID,hcBranchName,hcBranchLocation,hcContactNumber);
                                hcList.add(healthcare);
                            }
                            loadHealthCare();
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

        private void loadHealthCare() {
        final HealthCareAdapter adapter = new HealthCareAdapter(this, R.layout.activity_health_care, hcList);
        //listViewHealthCare.setAdapter(adapter);
        listViewHealthCare.setAdapter(adapter);
        if(hcList != null){
            int size = hcList.size();
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
