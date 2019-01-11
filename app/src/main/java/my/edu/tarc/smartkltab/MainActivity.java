package my.edu.tarc.smartkltab;

import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;
    public static final String FILE_NAME = "my.edu.tarc.smartkltab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout)findViewById(R.id.tablayoutid);
        appBarLayout = (AppBarLayout)findViewById(R.id.appbarid);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentHome(),"Home");

        String name = sharedPreferences.getString("name","");
        if(name==""){
            viewPagerAdapter.addFragment(new NoFeedbackFragment(),"Feedback");
            viewPagerAdapter.addFragment(new FragmentMe(),"Me");
        }else{
            viewPagerAdapter.addFragment(new FragmentFeedback(),"Feedback");
            viewPagerAdapter.addFragment(new ProfileFragment(),"Me");
        }
        //viewPagerAdapter.addFragment(new FragmentMe(),"Me");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
