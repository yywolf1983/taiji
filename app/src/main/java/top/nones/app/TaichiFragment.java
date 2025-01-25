package top.nones.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class TaichiFragment extends Fragment {
    private static final String TAG = "TaichiFragment";
    private TaichiView taichiView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建TaichiFragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_taichi, container, false);
            initViews(view);
            return view;
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: TaichiFragment视图创建失败", e);
            throw e;
        }
    }

    private void initViews(View view) {
        try {
            Log.d(TAG, "initViews: 开始初始化视图组件");
            taichiView = view.findViewById(R.id.taichiView);
            if (taichiView == null) {
                Log.e(TAG, "initViews: taichiView未找到");
                throw new IllegalStateException("TaichiView not found");
            }
            Log.d(TAG, "initViews: 视图组件初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initViews: 视图组件初始化失败", e);
            throw e;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: TaichiFragment恢复");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: TaichiFragment暂停");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: TaichiFragment销毁");
    }
}