package my.edu.tarc.smartkltab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.view.View.VISIBLE;

public class TransportActivity extends AppCompatActivity {


    ListView listView;
    String items[] = new String [] {"KTM","LRT","Bus"};
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";
    private SharedPreferences sharedPreferences;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        listView = findViewById(R.id.transportListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,android.R.id.text1,items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String TempListView = items[position];
                    Intent intent = new Intent(TransportActivity.this,TransportLine.class);
                    intent.putExtra("TransportType", TempListView);
                    startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fabT);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransportActivity.this, AddTransportLineActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(sharedPreferences.getString("usertype","").equals("admin")){
            fab.setVisibility(VISIBLE);
        }

    }



}
