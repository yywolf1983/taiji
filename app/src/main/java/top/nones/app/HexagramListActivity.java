package top.nones.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class HexagramListActivity extends AppCompatActivity {
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagram_list);
        initializeViewPager();
    }

    private void initializeViewPager() {
        viewPager = findViewById(R.id.viewPager);
        if (viewPager == null) return;
        viewPager.setAdapter(new ViewPagerAdapter(this));
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewPager != null) {
            viewPager.setAdapter(null);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
