package my.edu.tarc.smartkltab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HealthCareAdapterTest extends RecyclerView.Adapter<HealthCareAdapterTest.ViewHolder> {
    private Context context;
    private List<HealthCare> healthCareList;

    public HealthCareAdapterTest(Context context, List<HealthCare> healthCareList) {
        this.context = context;
        this.healthCareList = healthCareList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View healthcareView = LayoutInflater.from(context)
                .inflate(R.layout.healthcare_record, viewGroup, false);

        return new ViewHolder(healthcareView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        HealthCare healthCare = healthCareList.get(i);
        viewHolder.textViewHcBranchName.setText(String.format("%s" ,healthCare.getHcBranchName()));
        viewHolder.textViewHcBranchLocation.setText(String.format("%s" ,healthCare.getHcBranchLocation()));
        viewHolder.textViewHcContactNumber.setText(String.format("%s" ,healthCare.getHcContactNumber()));

    }

    @Override
    public int getItemCount() {
        return healthCareList.size();
    }

    public void removeItem(int position) {
        healthCareList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(HealthCare item, int position) {
        healthCareList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }



 /*   private Context context;
    private List<HealthCare> HealthCareArrayList;

    public HealthCareAdapterTest(Context context, List<HealthCare> HealthCareArrayList) {
        this.context = context;
        this.HealthCareArrayList = HealthCareArrayList;
    }

    public void remove(int position) {
        HealthCareArrayList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getCount() {
        return HealthCareArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return HealthCareArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.healthcare_record, null, true);

            holder.textViewHcBranchName = (TextView) convertView.findViewById(R.id.textViewHcBranchName);
            holder.textViewHcBranchLocation = (TextView) convertView.findViewById(R.id.textViewHcBranchLocation);
            holder.textViewHcContactNumber = (TextView) convertView.findViewById(R.id.textViewHcContactNumber);
            holder.containerHealthCare = (CardView) convertView.findViewById(R.id.containerHealthCare);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textViewHcBranchName = (TextView) convertView.findViewById(R.id.textViewHcBranchName);
        holder.textViewHcBranchLocation = (TextView) convertView.findViewById(R.id.textViewHcBranchLocation);
        holder.textViewHcContactNumber = (TextView) convertView.findViewById(R.id.textViewHcContactNumber);

        holder.textViewHcBranchName.setText(String.format("%s" ,HealthCareArrayList.get(position).getHcBranchName()));
        holder.textViewHcBranchLocation.setText(String.format("%s" ,HealthCareArrayList.get(position).getHcBranchLocation()));
        holder.textViewHcContactNumber.setText(String.format("%s" ,HealthCareArrayList.get(position).getHcContactNumber()));
        return convertView;
    }*/


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHcBranchName, textViewHcBranchLocation, textViewHcContactNumber;
        public CardView containerHealthCare;

        public ViewHolder(View view) {
            super(view);
            textViewHcBranchName = view.findViewById(R.id.textViewHcBranchName);
            textViewHcBranchLocation = view.findViewById(R.id.textViewHcBranchLocation);
            textViewHcContactNumber = view.findViewById(R.id.textViewHcContactNumber);
        }
    }

}

