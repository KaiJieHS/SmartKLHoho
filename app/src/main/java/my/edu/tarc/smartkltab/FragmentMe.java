package my.edu.tarc.smartkltab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentMe extends Fragment {
    View view;
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";
    private EditText editTextUserName,editTextPassword;
    private TextView textViewRegister;
    private Button btnLoginCitizen,btnLoginOfficer;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    public FragmentMe() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me_fragment,container,false);
        editTextUserName = (EditText)view.findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)view.findViewById(R.id.editTextPassword);
        textViewRegister =(TextView)view.findViewById(R.id.textViewRegister);
        sharedPreferences = getActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        //if(SharedPrefManager.getInstance(getActivity()).isLoggedIn()){
        //   getActivity().finish();
        //    startActivity(new Intent(getActivity(),ProfileActivity.class));
        // }

        progressDialog = new ProgressDialog(getActivity());
        String text = "Not a user? Register now";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE );
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan,12,20,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewRegister.setText(ss);
        textViewRegister.setMovementMethod(LinkMovementMethod.getInstance());

        btnLoginCitizen = (Button) view.findViewById(R.id.btnLoginCitizen);
        btnLoginCitizen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                citizenLogin();

            }
        });

        btnLoginOfficer = (Button) view.findViewById(R.id.btnLoginOfficer);
        btnLoginOfficer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                officerLogin();
            }
        });

        return view;
        //sharedPreferences = getContext().getSharedPreferences(FILE_NAME, context().MODE_PRIVATE);

    }

    public void citizenLogin(){
        final String username = editTextUserName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        progressDialog.setMessage("Login...");
        progressDialog.show();

        String URL = "https://circumgyratory-gove.000webhostapp.com/citizen_login.php?CUserName="+ username  + "&CPassword="+ password  ;

        if(!validate()){
            Toast.makeText(getActivity().getApplicationContext(),"",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    final JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response);
                        final int success = jsonObject.getInt("success");
                        final String message = jsonObject.getString("message");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (success == 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin("name","phoneNo","email");

                                    try {
                                        JSONObject citizenJson = jsonObject.getJSONObject("citizen");

                                        user citizen = new user(
                                                citizenJson.getInt("CitizenID"),
                                                citizenJson.getString("CName"),
                                                citizenJson.getString("CPhoneNo"),
                                                citizenJson.getString("CEmail")
                                        );

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("usertype", "user");
                                        editor.putInt("id", citizen.getId());
                                        editor.putString("name", citizen.getName());
                                        editor.putString("phoneno", citizen.getPhoneno());
                                        editor.putString("email", citizen.getEmail());
                                        editor.apply();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                                    progressDialog.dismiss();

                                }
                            }
                        }, 1000);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

            };
            RequestHandler.getInstance(this.getActivity()).addToRequestQueue(stringRequest);
        }
    }

    private void officerLogin(){
        final String username = editTextUserName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        progressDialog.setMessage("Login...");
        progressDialog.show();

        String URL2 = "https://circumgyratory-gove.000webhostapp.com/officer_login.php?OUserName="+ username  + "&OPassword="+ password  ;
        if(!validate()){
            Toast.makeText(getActivity().getApplicationContext(),"",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    final JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response);
                        final int success = jsonObject.getInt("success");
                        final String message = jsonObject.getString("message");
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (success == 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                    try {
                                        JSONObject officerJson = jsonObject.getJSONObject("officer");

                                        user officer = new user(
                                                officerJson.getInt("OfficerID"),
                                                officerJson.getString("OName"),
                                                officerJson.getString("OPhoneNo"),
                                                officerJson.getString("OEmail")
                                        );

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("usertype", "admin");
                                        editor.putInt("id", officer.getId());
                                        editor.putString("name", officer.getName());
                                        editor.putString("phoneno", officer.getPhoneno());
                                        editor.putString("email", officer.getEmail());
                                        editor.apply();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                                    progressDialog.dismiss();


                                }
                            }
                        }, 1000);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
            };
            RequestHandler.getInstance(this.getActivity()).addToRequestQueue(stringRequest);
        }
    }

    private boolean validate(){
        boolean valid = true;

        if(editTextUserName.getText().toString().trim().isEmpty()){
            editTextUserName.setError("Please enter your username");
            valid = false;
        }
        if(editTextPassword.getText().toString().trim().isEmpty()){
            editTextPassword.setError("Please enter your password");
            valid = false;
        }
        return valid;
    }
}
