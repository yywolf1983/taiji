package top.nones.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 六十四卦展示页面
 * 使用 GridLayout 展示所有卦象
 */
public class HexagramFragment extends Fragment {
    private static final String TAG = "HexagramFragment";
    private TextView nameTextView;
    private TextView pinyinTextView;
    private TextView symbolTextView;    // 象征意义
    private TextView judgmentTextView;  // 卦辞
    private TextView overallTextView;   // 总体评价
    private HexagramView hexagramView;
    private TextView linesTextView;
    private TextView structureTextView;
    private TextView imageTextView;
    private TextView judgmentDetailTextView;
    private TextView greatSymbolismTextView;
    private TextView applicationsTextView;
    private TextView historyTextView;
    private TextView changesTextView;

    public static HexagramFragment newInstance(String hexagramJson) {
        HexagramFragment fragment = new HexagramFragment();
        Bundle args = new Bundle();
        args.putString("hexagram", hexagramJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hexagram_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Log.d(TAG, "onViewCreated");
        
        nameTextView = view.findViewById(R.id.nameText);
        pinyinTextView = view.findViewById(R.id.pinyinText);
        symbolTextView = view.findViewById(R.id.symbolText);
        judgmentTextView = view.findViewById(R.id.judgmentText);
        overallTextView = view.findViewById(R.id.overallText);
        hexagramView = view.findViewById(R.id.hexagramView);
        linesTextView = view.findViewById(R.id.linesText);
        structureTextView = view.findViewById(R.id.structureText);
        imageTextView = view.findViewById(R.id.imageText);
        judgmentDetailTextView = view.findViewById(R.id.judgmentDetailText);
        greatSymbolismTextView = view.findViewById(R.id.greatSymbolismText);
        applicationsTextView = view.findViewById(R.id.applicationsText);
        historyTextView = view.findViewById(R.id.historyText);
        changesTextView = view.findViewById(R.id.changesText);
        
        if (hexagramView == null) {
            Log.e(TAG, "hexagramView is null!");
            return;
        }
        
        try {
            String hexagramJson = getArguments().getString("hexagram");
            if (hexagramJson == null) {
                Log.e(TAG, "hexagram data is null!");
                return;
            }
            
            JSONObject hexagram = new JSONObject(hexagramJson);
            displayHexagramData(hexagram);
        } catch (Exception e) {
            Log.e(TAG, "Error loading hexagram data", e);
        }

        // 绑定数据
        bindHexagramData();
    }

    private void bindHexagramData() {
        // 实现数据绑定逻辑
        // 确保数据正确传递到视图组件
    }

    private void displayHexagramData(JSONObject hexagram) {
        try {
            // 基本信息
            String name = hexagram.getString("name");
            String pinyin = hexagram.getString("pinyin");
            String gua = hexagram.getString("gua");
            int number = hexagram.getInt("number");
            
            // 卦象结构
            JSONObject upperTrigram = hexagram.getJSONObject("upperTrigram");
            JSONObject lowerTrigram = hexagram.getJSONObject("lowerTrigram");
            String upperName = upperTrigram.getString("name");
            String lowerName = lowerTrigram.getString("name");
            
            // 解释信息
            JSONObject interpretation = hexagram.getJSONObject("interpretation");
            String symbol = interpretation.getString("symbol");
            String judgment = interpretation.getString("judgment");
            String overall = interpretation.getString("overall");
            String image = interpretation.getString("image");
            String judgmentDetail = interpretation.getString("judgmentDetail");
            String greatSymbolism = interpretation.getString("greatSymbolism");
            
            // 应用信息
            JSONObject applications = hexagram.getJSONObject("applications");
            String general = applications.getString("general");
            String love = applications.getString("love");
            String career = applications.getString("career");
            String health = applications.getString("health");
            
            // 历史典故
            String history = hexagram.getString("history");
            
            // 变卦信息
            JSONArray changes = hexagram.getJSONArray("changes");
            StringBuilder changeText = new StringBuilder();
            for (int i = 0; i < changes.length(); i++) {
                JSONObject change = changes.getJSONObject(i);
                String position = change.getString("position");
                String targetHexagram = change.getString("targetHexagram");
                changeText.append(position).append("爻变：").append(targetHexagram).append("\n");
            }
            
            // 爻辞信息
            JSONArray lines = hexagram.getJSONArray("lines");
            StringBuilder lineTexts = new StringBuilder();
            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);
                String position = line.getString("position");
                String text = line.getString("text");
                lineTexts.append(position).append("：").append(text).append("\n");
            }
            
            // 显示基本信息
            nameTextView.setText(String.format("%d. %s", number, name));
            pinyinTextView.setText(pinyin);
            hexagramView.setGua(gua);
            
            // 显示卦象结构
            structureTextView.setText(String.format("上卦：%s  下卦：%s", upperName, lowerName));
            
            // 显示解释信息
            symbolTextView.setText("象征：" + symbol);
            judgmentTextView.setText("卦辞：" + judgment);
            overallTextView.setText("断语：" + overall);
            imageTextView.setText("卦象：" + image);
            judgmentDetailTextView.setText("卦辞详解：" + judgmentDetail);
            greatSymbolismTextView.setText("大象传：" + greatSymbolism);
            
            // 显示应用信息
            applicationsTextView.setText(String.format(
                "应用参考：\n总体：%s\n恋爱：%s\n事业：%s\n健康：%s",
                general, love, career, health
            ));
            
            // 显示历史典故
            historyTextView.setText("历史典故：" + history);
            
            // 显示变卦信息
            changesTextView.setText("变卦：\n" + changeText.toString());
            
            // 显示爻辞
            linesTextView.setText(lineTexts.toString().trim());
            
        } catch (JSONException e) {
            Log.e(TAG, "Error displaying hexagram data", e);
        }
    }
}