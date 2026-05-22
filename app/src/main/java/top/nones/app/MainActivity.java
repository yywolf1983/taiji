package top.nones.app;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private View dot0, dot1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupViewPager();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        dot0 = findViewById(R.id.dot0);
        dot1 = findViewById(R.id.dot1);
    }

    private void setupViewPager() {
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? new TaichiFragment() : new HexagramListFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicator(position);
            }
        });
    }

    private void updateIndicator(int position) {
        if (dot0 != null && dot1 != null) {
            dot0.setBackgroundResource(position == 0
                    ? R.drawable.indicator_active : R.drawable.indicator_inactive);
            dot1.setBackgroundResource(position == 1
                    ? R.drawable.indicator_active : R.drawable.indicator_inactive);
        }
    }
}
