package top.nones.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
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
        
        try {
            setContentView(R.layout.activity_hexagram_detail);
        } catch (Exception e) {
            Toast.makeText(this, "布局加载失败", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        } catch (Exception e) {
            // 忽略状态栏颜色设置失败
        }

        int hexagramNumber = getIntent().getIntExtra(EXTRA_HEXAGRAM_NUMBER, -1);
        if (hexagramNumber < 0) {
            showError("未接收到卦象数据");
            return;
        }

        try {
            HexagramDataStore.getInstance().load(this);
            JSONObject hexagram = HexagramDataStore.getInstance().get(hexagramNumber);

            if (hexagram == null) {
                showError("未找到对应的卦象数据");
                return;
            }

            displayHexagram(hexagram, hexagramNumber);
        } catch (Exception e) {
            showError("数据加载失败");
        }
    }

    private void displayHexagram(JSONObject hexagram, int hexagramNumber) {
        try {
            TextView nameTextView = findViewById(R.id.nameTextView);
            TextView guaTextView = findViewById(R.id.guaTextView);
            TextView pinyinTextView = findViewById(R.id.pinyinTextView);
            TextView judgmentTextView = findViewById(R.id.judgmentTextView);
            TextView baihuaTextView = findViewById(R.id.baihuaTextView);
            TextView modernTextView = findViewById(R.id.modernTextView);
            TextView seasonTextView = findViewById(R.id.seasonTextView);
            TextView directionTextView = findViewById(R.id.directionTextView);
            TextView wuxingDisplay = findViewById(R.id.wuxingDisplay);
            TextView natureDisplay = findViewById(R.id.natureDisplay);
            TextView familyDisplay = findViewById(R.id.familyDisplay);
            TextView numberDisplay = findViewById(R.id.numberDisplay);
            TextView summaryTextView = findViewById(R.id.summaryTextView);
            TextView adviceTextView = findViewById(R.id.adviceTextView);
            LinearLayout contentLayout = findViewById(R.id.contentLayout);
            LinearLayout linesContainer = findViewById(R.id.linesContainer);
            HexagramView guaView = findViewById(R.id.guaView);

            TextView natureTag = findViewById(R.id.natureTag);
            TextView wuxingTag = findViewById(R.id.wuxingTag);
            TextView familyTag = findViewById(R.id.familyTag);

            String name = HexagramUtils.getHexagramName(hexagram);
            String gua = HexagramUtils.getGua(hexagram);
            String pinyin = HexagramUtils.getPinyin(hexagram);

            if (nameTextView != null) nameTextView.setText(name);
            if (guaTextView != null) guaTextView.setText(gua);
            if (pinyinTextView != null) pinyinTextView.setText(pinyin);

            String nature = hexagram.optString("nature", "");
            String wuxing = hexagram.optString("wuxing", "");
            String family = hexagram.optString("family", "");

            if (natureTag != null) {
                if (!nature.isEmpty()) {
                    natureTag.setText(nature);
                    natureTag.setVisibility(View.VISIBLE);
                } else {
                    natureTag.setVisibility(View.GONE);
                }
            }

            if (wuxingTag != null) {
                if (!wuxing.isEmpty()) {
                    wuxingTag.setText(wuxing);
                    wuxingTag.setVisibility(View.VISIBLE);
                } else {
                    wuxingTag.setVisibility(View.GONE);
                }
            }

            if (familyTag != null) {
                if (!family.isEmpty()) {
                    familyTag.setText(family);
                    familyTag.setVisibility(View.VISIBLE);
                } else {
                    familyTag.setVisibility(View.GONE);
                }
            }

            String season = hexagram.optString("season", "");
            String direction = hexagram.optString("direction", "");

            if (seasonTextView != null) {
                seasonTextView.setText(season.isEmpty() ? "未知" : season);
            }
            if (directionTextView != null) {
                directionTextView.setText(direction.isEmpty() ? "未知" : direction);
            }
            if (wuxingDisplay != null) {
                wuxingDisplay.setText(wuxing.isEmpty() ? "未知" : wuxing);
            }
            if (natureDisplay != null) {
                natureDisplay.setText(nature.isEmpty() ? "未知" : nature);
            }
            if (familyDisplay != null) {
                familyDisplay.setText(family.isEmpty() ? "未知" : family);
            }
            if (numberDisplay != null) {
                numberDisplay.setText(String.valueOf(hexagramNumber));
            }

            String summary = hexagram.optString("summary", "");
            if (summaryTextView != null) {
                summaryTextView.setText(summary.isEmpty() ? "" : summary);
                summaryTextView.setVisibility(summary.isEmpty() ? View.GONE : View.VISIBLE);
            }

            String advice = hexagram.optString("advice", "");
            if (adviceTextView != null) {
                adviceTextView.setText(advice.isEmpty() ? "暂无建议" : advice);
            }

            if (guaView != null && gua.length() == 6) {
                guaView.setGua(gua);
            }

            JSONObject interpretation = HexagramUtils.getInterpretation(hexagram);
            if (judgmentTextView != null) {
                if (interpretation != null) {
                    String judgment = interpretation.optString("judgment", "");
                    judgmentTextView.setText(judgment.isEmpty() ? interpretation.optString("overall", "") : judgment);
                } else {
                    judgmentTextView.setText("暂无卦辞信息");
                }
            }

            String baihua = hexagram.optString("baihua", "");
            if (baihuaTextView != null) {
                baihuaTextView.setText(baihua.isEmpty() ? "暂无白话翻译" : baihua);
            }

            String modern = hexagram.optString("modern", "");
            if (modernTextView != null) {
                modernTextView.setText(modern.isEmpty() ? "暂无现代解读" : modern);
            }

            JSONArray lines = HexagramUtils.getLines(hexagram);
            if (linesContainer != null) {
                linesContainer.removeAllViews();
                if (lines != null && lines.length() > 0) {
                    for (int i = 0; i < lines.length(); i++) {
                        JSONObject line = lines.getJSONObject(i);
                        LinearLayout lineItem = new LinearLayout(this);
                        lineItem.setOrientation(LinearLayout.VERTICAL);
                        lineItem.setPadding(0, i == 0 ? 0 : 16, 0, 0);

                        TextView lineText = new TextView(this);
                        lineText.setText(line.optString("text", ""));
                        lineText.setTextSize(15);
                        lineText.setTextColor(getResources().getColor(R.color.textPrimary));
                        lineText.setLineSpacing(6f, 1f);
                        lineText.setTypeface(null, android.graphics.Typeface.BOLD);

                        String lineBaihua = line.optString("baihua", "");
                        if (!lineBaihua.isEmpty()) {
                            TextView lineBaihuaText = new TextView(this);
                            lineBaihuaText.setText(lineBaihua);
                            lineBaihuaText.setTextSize(14);
                            lineBaihuaText.setTextColor(getResources().getColor(R.color.textSecondary));
                            lineBaihuaText.setLineSpacing(4f, 1f);
                            lineBaihuaText.setPadding(0, 4, 0, 0);
                            lineItem.addView(lineBaihuaText);
                        }

                        if (i < lines.length() - 1) {
                            View divider = new View(this);
                            divider.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    1));
                            divider.setBackgroundColor(getResources().getColor(R.color.dividerColorLight));
                            divider.setPadding(0, 12, 0, 0);
                            lineItem.addView(divider);
                        }

                        lineItem.addView(lineText);
                        linesContainer.addView(lineItem);
                    }
                } else {
                    TextView emptyText = new TextView(this);
                    emptyText.setText("暂无爻辞信息");
                    emptyText.setTextColor(getResources().getColor(R.color.textHint));
                    linesContainer.addView(emptyText);
                }
            }

            if (contentLayout != null) {
                contentLayout.setVisibility(View.VISIBLE);
                try {
                    contentLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_enter));
                } catch (Exception e) {
                    // 忽略动画加载失败
                }
            }
        } catch (Exception e) {
            showError("数据解析失败");
        }
    }

    private void showError(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // 忽略Toast显示失败
        }
        try {
            LinearLayout contentLayout = findViewById(R.id.contentLayout);
            if (contentLayout != null) {
                contentLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // 忽略
        }
    }

    @Override
    public void finish() {
        try {
            super.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } catch (Exception e) {
            super.finish();
        }
    }
}