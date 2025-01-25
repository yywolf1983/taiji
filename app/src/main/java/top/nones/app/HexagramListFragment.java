package top.nones.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import android.view.Gravity;
import android.text.TextUtils;
import android.graphics.Typeface;

public class HexagramListFragment extends Fragment {
    private static final String TAG = "HexagramListFragment";
    private GridLayout gridLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建Fragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_hexagram_list, container, false);
            gridLayout = view.findViewById(R.id.hexagramGrid);
            if (gridLayout == null) {
                Log.e(TAG, "onCreateView: gridLayout未找到");
                throw new IllegalStateException("GridLayout not found");
            }
            Log.d(TAG, "onCreateView: 视图创建成功，开始加载卦象数据");
            loadHexagrams();
            return view;
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Fragment视图创建失败", e);
            throw e;
        }
    }

    private void loadHexagrams() {
        Log.d(TAG, "loadHexagrams: 开始加载卦象数据");
        try {
            InputStream is = getActivity().getAssets().open("hexagrams.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            Log.d(TAG, "loadHexagrams: JSON数据读取成功");
            
            JSONObject root = new JSONObject(json);
            JSONArray hexagrams = root.getJSONArray("hexagrams");
            Log.d(TAG, "loadHexagrams: 解析到" + hexagrams.length() + "个卦象");

            for (int i = 0; i < hexagrams.length(); i++) {
                JSONObject hexagram = hexagrams.getJSONObject(i);
                addHexagramView(hexagram);
            }
            Log.d(TAG, "loadHexagrams: 所有卦象加载完成");
        } catch (IOException e) {
            Log.e(TAG, "loadHexagrams: 读取卦象数据文件失败", e);
            Toast.makeText(getContext(), "加载卦象数据失败，请稍后重试", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Log.e(TAG, "loadHexagrams: JSON数据解析失败", e);
            Toast.makeText(getContext(), "卦象数据格式错误，请检查数据文件", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "loadHexagrams: 加载卦象数据失败", e);
            Toast.makeText(getContext(), "加载卦象时发生错误，请稍后重试", Toast.LENGTH_LONG).show();
        }
    }

    private void addHexagramView(JSONObject hexagram) {
        Log.d(TAG, "addHexagramView: 开始添加卦象视图");
        try {
            String name = HexagramUtils.getHexagramName(hexagram);
            String gua = HexagramUtils.getGua(hexagram);
            String pinyin = HexagramUtils.getPinyin(hexagram);
            Log.d(TAG, "addHexagramView: 处理卦象 - " + name);

            MaterialCardView cardView = new MaterialCardView(getContext());
            cardView.setCardElevation(8);
            cardView.setRadius(16);
            cardView.setUseCompatPadding(true);
            cardView.setClickable(true);
            cardView.setFocusable(true);
            
            cardView.setOnClickListener(v -> {
                Log.d(TAG, "onClick: 点击卦象 - " + name);
                showHexagramDetail(hexagram);
            });
            
            LinearLayout container = new LinearLayout(getContext());
            container.setOrientation(LinearLayout.VERTICAL);
            container.setGravity(Gravity.CENTER);
            container.setPadding(8, 8, 8, 8);
            container.setBackgroundColor(0xFFFFFFFF);  // 设置白色背景

            HexagramView hexagramView = new HexagramView(getContext());
            hexagramView.setGua(gua);
            LinearLayout.LayoutParams hexagramParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    80  // 减小卦象视图高度
            );
            hexagramParams.setMargins(1, 1, 1, 1);  // 减小边距
            hexagramView.setLayoutParams(hexagramParams);
            container.addView(hexagramView);

            TextView nameText = new TextView(getContext());
            nameText.setText(name);
            nameText.setTextSize(12);  // 减小文字大小
            nameText.setTextColor(0xFF333333);
            nameText.setGravity(Gravity.CENTER);
            container.addView(nameText);

            TextView pinyinText = new TextView(getContext());
            pinyinText.setText(pinyin);
            pinyinText.setTextSize(8);  // 减小拼音文字大小
            pinyinText.setTextColor(0xFF666666);
            pinyinText.setGravity(Gravity.CENTER);
            container.addView(pinyinText);

            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int margins = getResources().getDimensionPixelSize(R.dimen.grid_margin);
            int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
            int itemWidth = (screenWidth - (margins * 2) - (spacing * 3)) / 4;  // 每行4个卦象
            
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = itemWidth;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(2, 2, 2, 2);
            container.setLayoutParams(params);
            
            cardView.addView(container);
            gridLayout.addView(cardView);
            gridLayout.setColumnCount(4);  // 设置每行显示4个卦象
            gridLayout.setUseDefaultMargins(false);  // 禁用默认边距
            gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);  // 设置对齐模式
            gridLayout.setOrientation(GridLayout.HORIZONTAL);  // 设置水平方向排列
            Log.d(TAG, "addHexagramView: 卦象视图添加成功 - " + name);
        } catch (Exception e) {
            Log.e(TAG, "addHexagramView: 添加卦象视图失败", e);
            throw e;
        }
    }

    private void showHexagramDetail(JSONObject hexagram) {
        try {
            String name = HexagramUtils.getHexagramName(hexagram);
            Log.d(TAG, "showHexagramDetail: 显示卦象详情 - " + name);
            int hexagramNumber = hexagram.getInt("index");
            HexagramDetailActivity.start(getContext(), hexagramNumber);
        } catch (JSONException e) {
            Log.e(TAG, "showHexagramDetail: JSON数据解析失败", e);
            Toast.makeText(getContext(), "数据解析失败，请稍后重试", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "showHexagramDetail: 显示卦象详情失败", e);
            Toast.makeText(getContext(), "显示详情失败，请稍后重试", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Fragment恢复");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Fragment暂停");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Fragment销毁");
    }
}