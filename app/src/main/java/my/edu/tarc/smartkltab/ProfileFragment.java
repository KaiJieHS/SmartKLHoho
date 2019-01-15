package my.edu.tarc.smartkltab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    View view;
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";
    private SharedPreferences sharedPreferences;
    private TextView textViewName,textViewShowPhone,textViewShowEmail;
    private Button buttonLogout;
    private ProgressDialog progressDialog;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = getActivity().getSharedPreferences(FILE_NAME, getActivity().MODE_PRIVATE);


        textViewName = (TextView)view.findViewById(R.id.textViewName);
        textViewShowPhone = (TextView)view.findViewById(R.id.textViewShowPhone);
        textViewShowEmail = (TextView)view.findViewById(R.id.textViewShowEmail);
        progressDialog = new ProgressDialog(this.getActivity());
        buttonLogout = (Button)view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogout();
            }
        });

//        textViewName.setText(String.format("%s",SharedPrefManager.getInstance(this).getName()));
//        textViewShowPhone.setText(String.format("%s",SharedPrefManager.getInstance(this).getPhoneNo()));
//        textViewShowEmail.setText(String.format("%s",SharedPrefManager.getInstance(this).getEmail()));

        String name = sharedPreferences.getString("name", null);
        String email =sharedPreferences.getString("email", null);
        String phoneno = sharedPreferences.getString("phoneno", null);
        textViewName.setText(name);
        textViewShowEmail.setText(email);
        textViewShowPhone.setText(phoneno);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("phoneno", phoneno);
        editor.putString("email", email);
        editor.apply();
        return view;
    }

    public void userLogout(){
        progressDialog.setMessage("Logout...");
        progressDialog.show();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "");
        editor.putString("phoneno", "");
        editor.putString("email", "");
        editor.apply();
        SharedPrefManager.getInstance(this.getActivity()).logout();

        getActivity().finish();
        startActivity(new Intent(this.getActivity(), MainActivity.class));
    }

}




