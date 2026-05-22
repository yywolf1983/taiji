package top.nones.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;

public class HexagramDetailActivity extends AppCompatActivity {
    private static final String EXTRA_HEXAGRAM_NUMBER = "hexagram_number";

    public static void start(Context context, int hexagramNumber) {
        Intent intent = new Intent(context, HexagramDetailActivity.class);
        intent.putExtra(EXTRA_HEXAGRAM_NUMBER, hexagramNumber);
        context.startActivity(intent);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(
                    R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagram_detail);

        int hexagramNumber = getIntent().getIntExtra(EXTRA_HEXAGRAM_NUMBER, -1);
        if (hexagramNumber < 0) {
            showError("未接收到卦象数据");
            return;
        }

        // 从缓存获取数据（毫秒级）
        HexagramDataStore.getInstance().load(this);
        JSONObject hexagram = HexagramDataStore.getInstance().get(hexagramNumber);

        if (hexagram == null) {
            showError("未找到对应的卦象数据");
            return;
        }

        displayHexagram(hexagram);
    }

    private void displayHexagram(JSONObject hexagram) {
        try {
            TextView nameTextView = findViewById(R.id.nameTextView);
            TextView guaTextView = findViewById(R.id.guaTextView);
            TextView pinyinTextView = findViewById(R.id.pinyinTextView);
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            TextView linesTextView = findViewById(R.id.linesTextView);
            LinearLayout contentLayout = findViewById(R.id.contentLayout);
            HexagramView guaView = findViewById(R.id.guaView);

            String name = HexagramUtils.getHexagramName(hexagram);
            String gua = HexagramUtils.getGua(hexagram);
            String pinyin = HexagramUtils.getPinyin(hexagram);

            nameTextView.setText(name);
            guaTextView.setText(gua);
            pinyinTextView.setText(pinyin);

            if (guaView != null && gua.length() == 6) {
                guaView.setGua(gua);
            }

            // 解释信息
            JSONObject interpretation = HexagramUtils.getInterpretation(hexagram);
            if (interpretation != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("象征: ").append(interpretation.optString("symbol", "")).append("\n\n");
                sb.append("卦辞: ").append(interpretation.optString("judgment", "")).append("\n\n");
                sb.append("评价: ").append(interpretation.optString("overall", ""));
                descriptionTextView.setText(sb.toString());
            } else {
                descriptionTextView.setText("暂无解释信息");
            }

            // 爻辞
            JSONArray lines = HexagramUtils.getLines(hexagram);
            if (lines != null && lines.length() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < lines.length(); i++) {
                    JSONObject line = lines.getJSONObject(i);
                    sb.append(line.optString("text", "")).append("\n");
                }
                linesTextView.setText(sb.toString().trim());
            } else {
                linesTextView.setText("暂无爻辞信息");
            }

            if (contentLayout != null) {
                contentLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            showError("数据解析失败");
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        LinearLayout contentLayout = findViewById(R.id.contentLayout);
        if (contentLayout != null) {
            contentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
