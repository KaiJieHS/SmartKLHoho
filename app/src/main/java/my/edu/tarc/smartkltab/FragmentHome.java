package my.edu.tarc.smartkltab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentHome extends Fragment {
    View view;
    public FragmentHome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);

        Button buttonTransport = (Button) view.findViewById(R.id.button2);
        buttonTransport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), TransportActivity.class);
                startActivity(intent);
            }
        });

        Button buttonOrganisation = (Button) view.findViewById(R.id.button4);
        buttonOrganisation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), OrganizationActivity.class);
                startActivity(intent);
            }
        });

        Button buttonHealthCare = (Button) view.findViewById(R.id.button6);
        buttonHealthCare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), HealthCareActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
