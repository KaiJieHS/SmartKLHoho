package my.edu.tarc.smartkltab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

public class FragmentFeedback extends Fragment {
    View view;
    public static final String TAG = "my.edu.tarc.testsmartkl";
    ListView listViewFeedback;
    List<Feedback> fbList;
    private static String SEARCH_URL = "https://circumgyratory-gove.000webhostapp.com/search_feedback.php";
    private static String SEARCHWAITINGLIST_URL = "https://circumgyratory-gove.000webhostapp.com/search_feedbackwaitinglist.php";
    RequestQueue queue;
    private final String currentUserType = "user";

    public FragmentFeedback() {
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.feedback_fragment,container,false);
        listViewFeedback = (ListView) view.findViewById(R.id.listViewFeedback);
        fbList = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentid =String.valueOf(2);
                Intent intent = new Intent(getActivity(), AddFeedbackActivity.class);
                intent.putExtra("currentUserType", currentUserType);
                intent.putExtra("currentid2", currentid);
                startActivity(intent);
            }
        });

        if (!isConnected()) {
            Toast.makeText(getActivity().getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        if (currentUserType.equals("admin")){
            searchWaitingListFeedback(getActivity().getApplicationContext());

        }else {
            fab.setVisibility(view.VISIBLE);
            searchFeedback(getActivity().getApplicationContext(), 1);
        }

        listViewFeedback.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fbID = String.valueOf(fbList.get(position).getFeedbackID());
                String subject = fbList.get(position).getSubject();
                String desc = fbList.get(position).getDescription();
                String currentid =String.valueOf(1);
                Intent intent = new Intent(getActivity(), ViewResponseActivity.class);
                intent.putExtra("currentFeedbackID", fbID);
                intent.putExtra("currentUserType", currentUserType);
                intent.putExtra("currentid", currentid);
                intent.putExtra("desc", desc);
                intent.putExtra("subject", subject);
                startActivity(intent);

            }
        });
        return view;
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void searchFeedback(Context context, int id) {
        queue = Volley.newRequestQueue(context);
        String url = SEARCH_URL + "?CitizenID=" + id;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            fbList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject feedbackResponse = (JSONObject) response.get(i);

                                int fbid = feedbackResponse.getInt("FeedbackID");
                                String type = feedbackResponse.getString("FeedbackType");
                                String subj = feedbackResponse.getString("FeedbackTitle");
                                String desc = feedbackResponse.getString("FeedbackDesc");
                                String date = feedbackResponse.getString("FeedbackDate");
                                int citizenid = feedbackResponse.getInt("CitizenID");
                                String status = feedbackResponse.getString("Status");
                                Feedback feedback = new Feedback(fbid,type,subj,desc,date,citizenid,status);
                                fbList.add(feedback);
                            }
                            loadFeedback();

                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void searchWaitingListFeedback(Context context) {
        queue = Volley.newRequestQueue(context);
        String status = "waiting";
        String url = SEARCHWAITINGLIST_URL +"?Status=" +status;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            fbList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject feedbackResponse = (JSONObject) response.get(i);

                                int fbid = feedbackResponse.getInt("FeedbackID");
                                String type = feedbackResponse.getString("FeedbackType");
                                String subj = feedbackResponse.getString("FeedbackTitle");
                                String desc = feedbackResponse.getString("FeedbackDesc");
                                String date = feedbackResponse.getString("FeedbackDate");
                                int citizenid = feedbackResponse.getInt("CitizenID");
                                String status = feedbackResponse.getString("Status");
                                Feedback feedback = new Feedback(fbid,type,subj,desc,date,citizenid,status);
                                fbList.add(feedback);
                            }
                            loadFeedback();

                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    private void loadFeedback() {
        final FeedbackAdapter adapter = new FeedbackAdapter(getContext(), R.layout.feedback_fragment, fbList);
        listViewFeedback.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentUserType.equals("admin")){
            searchWaitingListFeedback(getActivity().getApplicationContext());
        }else {
            searchFeedback(getActivity().getApplicationContext(), 1);
        }

    }
}