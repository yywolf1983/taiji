package top.nones.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 六十四卦展示页面
 */
public class HexagramFragment extends Fragment {
    private TextView nameTextView;
    private TextView pinyinTextView;
    private TextView symbolTextView;
    private TextView judgmentTextView;
    private TextView overallTextView;
    private HexagramView hexagramView;
    private TextView linesTextView;
    private TextView structureTextView;

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
        
        nameTextView = view.findViewById(R.id.nameText);
        pinyinTextView = view.findViewById(R.id.pinyinText);
        symbolTextView = view.findViewById(R.id.symbolText);
        judgmentTextView = view.findViewById(R.id.judgmentText);
        overallTextView = view.findViewById(R.id.overallText);
        hexagramView = view.findViewById(R.id.hexagramView);
        linesTextView = view.findViewById(R.id.linesText);
        structureTextView = view.findViewById(R.id.structureText);
        
        if (hexagramView == null) return;
        
        try {
            String hexagramJson = getArguments().getString("hexagram");
            if (hexagramJson == null) return;
            
            JSONObject hexagram = new JSONObject(hexagramJson);
            displayHexagramData(hexagram);
        } catch (Exception e) {
            // 静默处理
        }
    }

    private void displayHexagramData(JSONObject hexagram) {
        try {
            int index = hexagram.getInt("index");
            String name = hexagram.getString("name");
            String pinyin = hexagram.getString("pinyin");
            String gua = hexagram.getString("gua");

            // 显示基本信息
            nameTextView.setText(String.format("%d. %s", index + 1, name));
            pinyinTextView.setText(pinyin);
            hexagramView.setGua(gua);

            // 显示上下卦结构
            if (structureTextView != null && gua.length() == 6) {
                String upper = getTrigramName(gua.substring(0, 3));
                String lower = getTrigramName(gua.substring(3, 6));
                structureTextView.setText(String.format("上%s 下%s", upper, lower));
            }

            // 解释信息
            JSONObject interpretation = hexagram.optJSONObject("interpretation");
            if (interpretation != null) {
                if (symbolTextView != null) {
                    symbolTextView.setText("象征：" + interpretation.optString("symbol", ""));
                }
                if (judgmentTextView != null) {
                    judgmentTextView.setText("卦辞：" + interpretation.optString("judgment", ""));
                }
                if (overallTextView != null) {
                    overallTextView.setText("断语：" + interpretation.optString("overall", ""));
                }
            }

            // 爻辞
            JSONArray lines = hexagram.optJSONArray("lines");
            if (lines != null && lines.length() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < lines.length(); i++) {
                    JSONObject line = lines.getJSONObject(i);
                    sb.append(line.optString("text", ""));
                    if (i < lines.length() - 1) sb.append("\n");
                }
                linesTextView.setText(sb.toString());
            }
        } catch (JSONException e) {
            // 静默处理
        }
    }

    /** 根据三爻编码获取八卦名 */
    private String getTrigramName(String trigram) {
        switch (trigram) {
            case "111": return "乾";
            case "000": return "坤";
            case "100": return "震";
            case "010": return "坎";
            case "001": return "艮";
            case "110": return "巽";
            case "101": return "离";
            case "011": return "兑";
            default: return "";
        }
    }
}