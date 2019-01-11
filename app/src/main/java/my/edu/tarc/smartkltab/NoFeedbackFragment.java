package my.edu.tarc.smartkltab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NoFeedbackFragment extends Fragment {
    View view;
    private Button btnGoto;
    private ViewPager viewPager;

    public NoFeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_no_feedback, container, false);
        btnGoto = view.findViewById(R.id.btnGotoLogin);
        btnGoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPager = (ViewPager) getActivity().findViewById(
                        R.id.viewpager);
                viewPager.setCurrentItem(2);
            }
        });
        return view;


    }


}
