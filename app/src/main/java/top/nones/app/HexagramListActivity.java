package top.nones.app;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class HexagramListActivity extends AppCompatActivity {
    private static final String TAG = "HexagramListActivity";
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: 开始创建HexagramListActivity");
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_hexagram_list);
            initializeViewPager();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing activity", e);
            finish();
        }
    }

    private void initializeViewPager() {
        try {
            Log.d(TAG, "initializeViewPager: 开始初始化ViewPager");
            viewPager = findViewById(R.id.viewPager);
            if (viewPager == null) {
                Log.e(TAG, "initializeViewPager: ViewPager未找到");
                throw new IllegalStateException("ViewPager not found");
            }
            viewPager.setAdapter(new ViewPagerAdapter(this));
            viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            Log.d(TAG, "initializeViewPager: ViewPager初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initializeViewPager: ViewPager初始化失败", e);
            throw e;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: 开始销毁HexagramListActivity");
        super.onDestroy();
        if (viewPager != null) {
            viewPager.setAdapter(null);
            Log.d(TAG, "onDestroy: ViewPager适配器已清除");
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Log.d(TAG, "onBackPressed: 处理返回按钮事件");
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception e) {
            Log.e(TAG, "onBackPressed: 处理返回事件失败", e);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: HexagramListActivity启动");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: HexagramListActivity恢复");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: HexagramListActivity暂停");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: HexagramListActivity停止");
    }
}