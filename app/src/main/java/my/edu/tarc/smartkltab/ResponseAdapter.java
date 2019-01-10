package my.edu.tarc.smartkltab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ResponseAdapter extends ArrayAdapter<FeedbackResponses> {
    public ResponseAdapter(Activity context, int resource, List<FeedbackResponses> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FeedbackResponses feedbackres = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.response_record, parent, false);

        TextView textViewFeedbackId, textViewResponseDesc, textViewResponseDate, textViewOfficerId, textViewResponseId;

        //textViewFeedbackId = rowView.findViewById(R.id.textViewFeedbackId);
        textViewResponseDesc = rowView.findViewById(R.id.textViewResponseDesc);
        textViewResponseDate = rowView.findViewById(R.id.textViewResponseDate);
        textViewOfficerId = rowView.findViewById(R.id.textViewOfficerId);
        textViewResponseId = rowView.findViewById(R.id.textViewResponseId);

       // textViewFeedbackId.setText(String.format("%s : %s", "Feedback ID",feedbackres.getFeedbackID()));
        textViewResponseDesc.setText(String.format("%s : %s", "Description",feedbackres.getResponseDesc()));
        textViewResponseDate.setText(String.format("%s : %s", "Date/Time",feedbackres.getResponseDate()));
        textViewOfficerId.setText(String.format("%s : %d", "From Officer ID",feedbackres.getOfficerID()));
        textViewResponseId.setText(String.format("%s : %s", "Response ID",feedbackres.getResponseID()));

        if(feedbackres.getOfficerID()>0){
            //textViewFeedbackId.setTextColor(Color.WHITE);
            textViewResponseDesc.setTextColor(Color.WHITE);
            textViewResponseDate.setTextColor(Color.WHITE);
            textViewOfficerId.setTextColor(Color.WHITE);
            textViewResponseId.setTextColor(Color.WHITE);
            rowView.setBackgroundColor(Color.DKGRAY);
       }else{
            //textViewFeedbackId.setTextColor(Color.BLACK);
            textViewResponseDesc.setTextColor(Color.BLACK);
            textViewResponseDate.setTextColor(Color.BLACK);
            textViewOfficerId.setAlpha(0.0f);
            textViewResponseId.setTextColor(Color.BLACK);
        }
        return rowView;
    }
}
