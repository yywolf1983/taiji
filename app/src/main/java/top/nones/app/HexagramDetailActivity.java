package top.nones.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import java.io.InputStream;
import java.io.IOException;

public class HexagramDetailActivity extends AppCompatActivity {
    private static final String TAG = "HexagramDetailActivity";
    private static final String EXTRA_HEXAGRAM_NUMBER = "hexagram_number";

    public static void start(Context context, int hexagramNumber) {
        Log.d(TAG, "start: 启动卦象详情页面，卦象编号: " + hexagramNumber);
        Intent intent = new Intent(context, HexagramDetailActivity.class);
        intent.putExtra(EXTRA_HEXAGRAM_NUMBER, hexagramNumber);
        context.startActivity(intent);
    }

    private TextView nameTextView;
    private TextView guaTextView;
    private TextView pinyinTextView;
    private TextView descriptionTextView;
    private TextView linesTextView;

    private ProgressBar loadingProgressBar;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: 开始创建HexagramDetailActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagram_detail);

        try {
            // 添加加载进度条和内容布局
            Log.d(TAG, "onCreate: 初始化视图组件");
            loadingProgressBar = findViewById(R.id.loadingProgressBar);
            contentLayout = findViewById(R.id.contentLayout);

            // 初始化视图
            nameTextView = findViewById(R.id.nameTextView);
            guaTextView = findViewById(R.id.guaTextView);
            pinyinTextView = findViewById(R.id.pinyinTextView);
            descriptionTextView = findViewById(R.id.descriptionTextView);
            linesTextView = findViewById(R.id.linesTextView);

            // 获取传递的卦象编号
            Intent intent = getIntent();
            if (intent != null) {
                int hexagramNumber = intent.getIntExtra(EXTRA_HEXAGRAM_NUMBER, -1);
                Log.d(TAG, "onCreate: 接收到的卦象编号: " + hexagramNumber);
                
                if (hexagramNumber >= 0) {
                    // 显示加载状态
                    showLoading(true);
                    
                    try {
                        // 从assets加载卦象数据
                        String jsonString = loadJSONFromAsset("hexagrams.json");
                        if (jsonString != null) {
                            JSONObject root = new JSONObject(jsonString);
                            JSONArray hexagrams = root.getJSONArray("hexagrams");
                            
                            // 查找对应编号的卦象
                            JSONObject hexagram = null;
                            for (int i = 0; i < hexagrams.length(); i++) {
                                JSONObject current = hexagrams.getJSONObject(i);
                                if (current.getInt("index") == hexagramNumber) {
                                    hexagram = current;
                                    break;
                                }
                            }
                            
                            if (hexagram != null) {
                                Log.d(TAG, "onCreate: 成功找到卦象数据");
                                try {
                                    // 解析 JSON 数据
                                    Log.d(TAG, "onCreate: 成功解析卦象JSON数据");
                                    
                                    // 设置视图数据
                                    String name = HexagramUtils.getHexagramName(hexagram);
                                    Log.d(TAG, "onCreate: 设置卦象名称: " + name);
                                    nameTextView.setText(name);

                                    // 处理卦象数据
                                    String guaStr = HexagramUtils.getGua(hexagram);
                                    Log.d(TAG, "onCreate: 获取到卦象数据: " + guaStr);
                                    int[] gua = new int[6];
                                    for (int i = 0; i < 6 && i < guaStr.length(); i++) {
                                        gua[i] = Character.getNumericValue(guaStr.charAt(i));
                                    }

                                    if (gua != null && gua.length == 6) {
                                        HexagramView guaView = findViewById(R.id.guaView);
                                        if (guaView != null) {
                                            StringBuilder guaBuilder = new StringBuilder();
                                            for (int value : gua) {
                                                guaBuilder.append(value);
                                            }
                                            Log.d(TAG, "onCreate: 设置卦象图形: " + guaBuilder.toString());
                                            guaView.setGua(guaBuilder.toString());
                                        } else {
                                            Log.e(TAG, "onCreate: guaView未找到");
                                            Toast.makeText(this, "无法显示卦象", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.e(TAG, "onCreate: 卦象数据无效");
                                        Toast.makeText(this, "无法显示卦象", Toast.LENGTH_SHORT).show();
                                    }

                                    guaTextView.setText(HexagramUtils.getGua(hexagram));
                                    pinyinTextView.setText(HexagramUtils.getPinyin(hexagram));

                                    // 获取并解析解释信息
                                    Log.d(TAG, "onCreate: 开始解析解释信息");
                                    JSONObject interpretationJson = HexagramUtils.getInterpretation(hexagram);
                                    if (interpretationJson != null) {
                                        String symbol = interpretationJson.optString("symbol", "");
                                        String judgment = interpretationJson.optString("judgment", "");
                                        String overall = interpretationJson.optString("overall", "");

                                        StringBuilder interpretationBuilder = new StringBuilder();
                                        interpretationBuilder.append("象征: ").append(symbol).append("\n");
                                        interpretationBuilder.append("卦辞: ").append(judgment).append("\n");
                                        interpretationBuilder.append("评价: ").append(overall).append("\n");

                                        Log.d(TAG, "onCreate: 设置解释信息");
                                        descriptionTextView.setText(interpretationBuilder.toString());
                                    } else {
                                        Log.w(TAG, "onCreate: 解释信息为空");
                                        descriptionTextView.setText("暂无解释信息");
                                    }

                                    // 获取并解析爻辞信息
                                    Log.d(TAG, "onCreate: 开始解析爻辞信息");
                                    JSONArray linesJson = HexagramUtils.getLines(hexagram);
                                    if (linesJson != null && linesJson.length() > 0) {
                                        StringBuilder linesBuilder = new StringBuilder();
                                        linesBuilder.append("爻辞:\n");
                                        for (int i = 0; i < linesJson.length(); i++) {
                                            JSONObject line = linesJson.getJSONObject(i);
                                            int position = line.optInt("position");
                                            String text = line.optString("text");
                                            linesBuilder.append("第").append(position).append("爻：").append(text).append("\n");
                                        }
                                        Log.d(TAG, "onCreate: 设置爻辞信息");
                                        linesTextView.setText(linesBuilder.toString());
                                    } else {
                                        Log.w(TAG, "onCreate: 爻辞信息为空");
                                        linesTextView.setText("暂无爻辞信息");
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "onCreate: JSON解析失败", e);
                                    showError("数据解析失败，请稍后重试");
                                } finally {
                                    showLoading(false);
                                }
                            } else {
                                Log.e(TAG, "onCreate: 未找到对应编号的卦象");
                                showError("未找到对应的卦象数据");
                            }
                        } else {
                            Log.e(TAG, "onCreate: 未接收到卦象数据");
                            showError("未接收到卦象数据");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: JSON解析失败", e);
                        showError("数据解析失败，请稍后重试");
                    }
                } else {
                    Log.e(TAG, "onCreate: 未接收到卦象数据");
                    showError("未接收到卦象数据");
                }
            } else {
                Log.e(TAG, "onCreate: Intent为空");
                showError("页面启动异常");
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: 页面创建失败", e);
            showError("页面创建失败");
        }
    }

    private void showLoading(boolean show) {
        Log.d(TAG, "showLoading: " + (show ? "显示" : "隐藏") + "加载进度");
        if (loadingProgressBar != null && contentLayout != null) {
            loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            contentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showError(String message) {
        Log.e(TAG, "showError: " + message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (contentLayout != null) {
            contentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 卦象详情页面启动");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 卦象详情页面恢复");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 卦象详情页面暂停");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 卦象详情页面停止");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 卦象详情页面销毁");
    }
    private String loadJSONFromAsset(String fileName) {
        Log.d(TAG, "loadJSONFromAsset: 开始从assets加载文件: " + fileName);
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d(TAG, "loadJSONFromAsset: 文件加载成功");
        } catch (IOException e) {
            Log.e(TAG, "loadJSONFromAsset: 文件加载失败", e);
            json = null;
        }
        return json;
    }
}
