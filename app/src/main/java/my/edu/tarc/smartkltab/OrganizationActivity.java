package my.edu.tarc.smartkltab;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

import static android.view.View.VISIBLE;

public class OrganizationActivity extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.testsmartkl";
    ListView listViewOrganization;
    List<Organization> ogList;
    private ProgressDialog pDialog;
    //TODO: Please update the URL to point to your own server
    private static String GET_URL = "https://circumgyratory-gove.000webhostapp.com/select_organization.php";
    RequestQueue queue;
    private List<Organization> UserSelection = new ArrayList<>();
    MenuItem updateButton;
    private OrganizationAdapter organizationAdapter;
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";
    private SharedPreferences sharedPreferences;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        setTitle("Organization List");

        listViewOrganization = (ListView) findViewById(R.id.listViewOrganization);
        pDialog = new ProgressDialog(this);
        ogList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);


        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }


        organizationAdapter = new OrganizationAdapter(this,0,ogList);
        listViewOrganization.setAdapter(organizationAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizationActivity.this, AddOrganizationActivity.class);
                startActivity(intent);
            }
        });
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        downloadOrganization(getApplicationContext(), GET_URL);
        if(sharedPreferences.getString("usertype","").equals("admin")){
            listViewOrganization.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            listViewOrganization.setMultiChoiceModeListener(modeListener);
            fab.setVisibility(VISIBLE);
        }


        listViewOrganization.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String uri = ogList.get(position).getOrgBranchLocation();
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
            if(UserSelection.contains(ogList.get(position))){
                UserSelection.remove(ogList.get(position));
            }else{
                UserSelection.add(ogList.get(position));
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
                    organizationAdapter.removeItems(UserSelection);
                    mode.finish();
                    return true;
                case R.id.action_update:
                    organizationAdapter.updateItems(UserSelection);
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

    private void downloadOrganization(Context context, String url) {
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
                            ogList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject organizationResponse = (JSONObject) response.get(i);
                                int organizationID = Integer.parseInt(organizationResponse.getString("OrganizationID"));
                                String orgName = organizationResponse.getString("OrgName");
                                String orgBranchLocation = organizationResponse.getString("OrgBranchLocation");
                                String orgContactNum = organizationResponse.getString("OrgContactNum");
                                Organization organization = new Organization(organizationID,orgName,orgBranchLocation,orgContactNum);
                                ogList.add(organization);
                            }
                            loadOrganization();
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

    private void loadOrganization() {
        final OrganizationAdapter adapter = new OrganizationAdapter(this, R.layout.activity_organization, ogList);
        listViewOrganization.setAdapter(adapter);
        if(ogList != null){
            int size = ogList.size();
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
