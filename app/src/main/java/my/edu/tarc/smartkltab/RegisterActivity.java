package my.edu.tarc.smartkltab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername, editTextPassword , editTextConfirmPassword, editTextName,editTextPhoneNo,editTextEmail;
    private Button buttonRegisterCitizen,buttonRegisterOfficer;
    private ProgressDialog progressDialog;
    private TextView textViewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //if(SharedPrefManager.getInstance(this).isLoggedIn()){
        // finish();
        //startActivity(new Intent(this,ProfileActivity.class));
        //}

        editTextUsername = (EditText)findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextPhoneNo = (EditText)findViewById(R.id.editTextPhoneNo);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);

        //textViewLogin = (TextView)findViewById(R.id.textViewLogin);
        buttonRegisterCitizen = (Button)findViewById(R.id.btnRegisterCitizen);
        buttonRegisterOfficer = (Button)findViewById(R.id.btnRegisterOfficer);

        progressDialog = new ProgressDialog(this);

        buttonRegisterCitizen.setOnClickListener(this);
        buttonRegisterOfficer.setOnClickListener(this);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void onClick(View view){
        if(view == buttonRegisterCitizen) {
            if(editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                registerCitizen();

            }else{
                Toast.makeText(getApplicationContext(),"Sorry 2 password must be same",Toast.LENGTH_LONG).show();
                editTextPassword.setText("");
                editTextConfirmPassword.setText("");
            }
        }else if(view == buttonRegisterOfficer){
            if(editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                registerOfficer();
                finish();
                //startActivity(new Intent(getApplicationContext(),CitizenLoginActivity.class));
            }else{
                Toast.makeText(getApplicationContext(),"Sorry 2 password must be same",Toast.LENGTH_LONG).show();
                editTextPassword.setText("");
                editTextConfirmPassword.setText("");
            }
        }
    }

    public void registerCitizen() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String phoneNo = editTextPhoneNo.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        progressDialog.setMessage("Registering Citizen...");
        progressDialog.show();

        if(!validate()){
            progressDialog.dismiss();

        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://circumgyratory-gove.000webhostapp.com/registerCitizen.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        //System.out.println(jsonObject.getString("message"));
                        progressDialog.dismiss();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    System.out.println(error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("CUserName", username);
                    params.put("CPassword", password);
                    params.put("CName", name);
                    params.put("CPhoneNo", phoneNo);
                    params.put("CEmail", email);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    public void registerOfficer() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String phoneNo = editTextPhoneNo.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        progressDialog.setMessage("Registering Officer...");
        progressDialog.show();

        if(!validate()){
            progressDialog.dismiss();

        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://circumgyratory-gove.000webhostapp.com/registerOfficer.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    System.out.println(error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("OUserName", username);
                    params.put("OPassword", password);
                    params.put("OName", name);
                    params.put("OPhoneNo", phoneNo);
                    params.put("OEmail", email);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    private boolean validate(){
        boolean valid = true;

        if(editTextUsername.getText().toString().trim().isEmpty()){
            editTextUsername.setError("Please enter your username");
            valid = false;
        }
        if(editTextPassword.getText().toString().trim().isEmpty()){
            editTextPassword.setError("Please enter your password");
            valid = false;
        }
        if(editTextConfirmPassword.getText().toString().trim().isEmpty()){
            editTextConfirmPassword.setError("Please enter your password again");
            valid = false;
        }
        if(editTextName.getText().toString().trim().isEmpty()){
            editTextName.setError("Please enter your name");
            valid = false;
        }
        if(editTextPhoneNo.getText().toString().trim().isEmpty()){
            editTextPhoneNo.setError("Please enter your phone number");
            valid = false;
        }
        if(editTextEmail.getText().toString().trim().isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()){
            editTextEmail.setError("Please enter your email / Invalid email format");
            valid = false;
        }
        return valid;
    }
}
