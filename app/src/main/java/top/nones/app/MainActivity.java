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

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
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

        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
            private static final float MIN_ALPHA = 0.5f;

            @Override
            public void transformPage(View page, float position) {
                int pageWidth = page.getWidth();

                if (position < -1) {
                    page.setAlpha(0f);
                } else if (position <= 0) {
                    page.setAlpha(1f);
                    page.setTranslationX(0f);
                    page.setScaleX(1f);
                    page.setScaleY(1f);
                } else if (position <= 1) {
                    page.setAlpha(1 - position);
                    page.setTranslationX(pageWidth * -position);
                    float scaleFactor = MIN_SCALE
                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                } else {
                    page.setAlpha(0f);
                }
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicator(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateIndicatorAnimation(position, positionOffset);
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

    private void updateIndicatorAnimation(int position, float positionOffset) {
        if (dot0 == null || dot1 == null) return;

        float scale0 = position == 0 ? 1.0f + (0.2f * (1 - positionOffset)) : 1.0f;
        float scale1 = position == 1 ? 1.0f + (0.2f * positionOffset) : 1.0f;

        dot0.setScaleX(scale0);
        dot0.setScaleY(scale0);
        dot1.setScaleX(scale1);
        dot1.setScaleY(scale1);
    }
}