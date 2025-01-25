package top.nones.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: 开始创建MainActivity");
        try {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "onCreate: 设置布局前");
            setContentView(R.layout.activity_main);
            Log.d(TAG, "onCreate: 设置布局后");

            initViews();
            setupViewPager();
            Log.d(TAG, "onCreate: MainActivity初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: MainActivity创建失败", e);
            finish();
        }
    }

    private void initViews() {
        try {
            Log.d(TAG, "initViews: 开始初始化视图");
            viewPager = findViewById(R.id.viewPager);

            // 添加空值检查
            if (viewPager == null) {
                Log.e(TAG, "initViews: ViewPager未找到");
                throw new IllegalStateException("ViewPager未找到");
            }

            Log.d(TAG, "initViews: 视图初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initViews: 视图初始化失败", e);
            Toast.makeText(this, "应用初始化失败，请检查日志", Toast.LENGTH_LONG).show();
            throw e;
        }
    }

    private void setupViewPager() {
        try {
            Log.d(TAG, "setupViewPager: 开始设置ViewPager");
            viewPager.setAdapter(new FragmentStateAdapter(this) {
                @Override
                public Fragment createFragment(int position) {
                    Log.d(TAG, "createFragment: 创建Fragment, position: " + position);
                    if (position == 0) {
                        return new TaichiFragment();
                    } else {
                        return new HexagramListFragment();
                    }
                }

                @Override
                public int getItemCount() {
                    return 2;
                }
            });
            Log.d(TAG, "setupViewPager: ViewPager设置完成");
        } catch (Exception e) {
            Log.e(TAG, "setupViewPager: ViewPager设置失败", e);
            throw e;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: MainActivity启动");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MainActivity恢复");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: MainActivity暂停");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: MainActivity停止");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: MainActivity销毁");
    }
}