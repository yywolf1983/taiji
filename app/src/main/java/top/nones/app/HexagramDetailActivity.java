package top.nones.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;

public class HexagramDetailActivity extends AppCompatActivity implements HexagramView.OnLineChangeListener {
    private static final String EXTRA_HEXAGRAM_NUMBER = "hexagram_number";
    private HexagramView guaView;
    private LinearLayout linesContainer;
    private TextView changeHint;
    private TextView changeRuleExplain;
    private boolean isChanged = false;
    
    private LinearLayout zhiguaContainer;
    private HexagramView zhiguaView;
    private TextView zhiguaName;
    private TextView zhiguaPinyin;
    private TextView zhiguaJudgment;
    
    private Button[] lineButtons = new Button[6];

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
            linesContainer = findViewById(R.id.linesContainer);
            guaView = findViewById(R.id.guaView);
            changeHint = findViewById(R.id.changeHint);
            changeRuleExplain = findViewById(R.id.changeRuleExplain);
            
            zhiguaContainer = findViewById(R.id.zhiguaContainer);
            zhiguaView = findViewById(R.id.zhiguaView);
            zhiguaName = findViewById(R.id.zhiguaName);
            zhiguaPinyin = findViewById(R.id.zhiguaPinyin);
            zhiguaJudgment = findViewById(R.id.zhiguaJudgment);

            lineButtons[0] = findViewById(R.id.btnLine1);
            lineButtons[1] = findViewById(R.id.btnLine2);
            lineButtons[2] = findViewById(R.id.btnLine3);
            lineButtons[3] = findViewById(R.id.btnLine4);
            lineButtons[4] = findViewById(R.id.btnLine5);
            lineButtons[5] = findViewById(R.id.btnLine6);

            for (int i = 0; i < 6; i++) {
                final int position = i;
                lineButtons[i].setOnClickListener(v -> toggleLineByButton(position));
            }

            TextView natureTag = findViewById(R.id.natureTag);
            TextView wuxingTag = findViewById(R.id.wuxingTag);
            TextView familyTag = findViewById(R.id.familyTag);

            String name = HexagramUtils.getHexagramName(hexagram);
            String gua = HexagramUtils.getGua(hexagram);
            String pinyin = HexagramUtils.getPinyin(hexagram);
            
            updateButtonLabels(gua);

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
                guaView.setOnLineChangeListener(this);
            }

            if (changeHint != null) {
                changeHint.setOnClickListener(v -> {
                    if (isChanged && guaView != null) {
                        guaView.resetGua();
                        isChanged = false;
                        highlightCurrentLines();
                        changeHint.setText("点击按钮变爻");
                        if (changeRuleExplain != null) {
                            changeRuleExplain.setVisibility(View.GONE);
                        }
                        if (zhiguaContainer != null) {
                            zhiguaContainer.setVisibility(View.GONE);
                        }
                        resetButtonColors();
                    }
                });
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
                        String lineTextStr = line.optString("text", "");
                        
                        boolean isYang = lineTextStr.contains("九");
                        
                        androidx.cardview.widget.CardView lineCard = new androidx.cardview.widget.CardView(this);
                        lineCard.setRadius(12f);
                        lineCard.setCardElevation(1f);
                        lineCard.setCardBackgroundColor(getResources().getColor(R.color.cardBackground));
                        lineCard.setTag(i);
                        
                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        cardParams.setMargins(0, i == 0 ? 0 : 8, 0, 0);
                        lineCard.setLayoutParams(cardParams);

                        LinearLayout lineItem = new LinearLayout(this);
                        lineItem.setOrientation(LinearLayout.VERTICAL);
                        lineItem.setPadding(12, 12, 12, 12);

                        TextView lineHeader = new TextView(this);
                        lineHeader.setText(lineTextStr);
                        lineHeader.setTextSize(14);
                        lineHeader.setTypeface(null, android.graphics.Typeface.BOLD);
                        lineHeader.setLineSpacing(6f, 1f);
                        if (isYang) {
                            lineHeader.setTextColor(0xFFB8943E);
                        } else {
                            lineHeader.setTextColor(0xFF5B6A8A);
                        }

                        lineItem.addView(lineHeader);

                        String lineBaihua = line.optString("baihua", "");
                        if (!lineBaihua.isEmpty()) {
                            TextView lineBaihuaText = new TextView(this);
                            lineBaihuaText.setText(lineBaihua);
                            lineBaihuaText.setTextSize(13);
                            lineBaihuaText.setTextColor(getResources().getColor(R.color.textSecondary));
                            lineBaihuaText.setLineSpacing(4f, 1f);
                            lineBaihuaText.setPadding(0, 8, 0, 0);
                            lineItem.addView(lineBaihuaText);
                        }
                        lineCard.addView(lineItem);
                        linesContainer.addView(lineCard);
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
                }
            }
        } catch (Exception e) {
            showError("数据解析失败");
        }
    }

    private void toggleLineByButton(int position) {
        if (guaView == null) return;
        
        guaView.toggleLine(position);
        updateButtonColor(position);
        updateButtonLabels(guaView.getCurrentGua());
    }
    
    private void updateButtonLabels(String guaStr) {
        if (guaStr == null || guaStr.length() != 6) return;
        
        String[] positions = {"初", "二", "三", "四", "五", "上"};
        
        for (int i = 0; i < 6; i++) {
            if (lineButtons[i] != null) {
                char c = guaStr.charAt(5 - i);
                String yangYin = (c == '1') ? "九" : "六";
                lineButtons[i].setText(yangYin + positions[i]);
            }
        }
    }

    private void updateButtonColor(int position) {
        if (lineButtons[position] == null || guaView == null) return;
        
        String currentGua = guaView.getCurrentGua();
        String originalGua = guaView.getOriginalGua();
        
        if (currentGua.charAt(5 - position) != originalGua.charAt(5 - position)) {
            lineButtons[position].setBackgroundResource(R.drawable.btn_line_changed);
            lineButtons[position].setTextColor(0xFF9B9689);
        } else {
            lineButtons[position].setBackgroundResource(R.drawable.btn_line_normal);
            lineButtons[position].setTextColor(getResources().getColor(R.color.textSecondary));
        }
    }

    private void resetButtonColors() {
        for (int i = 0; i < 6; i++) {
            if (lineButtons[i] != null) {
                lineButtons[i].setBackgroundResource(R.drawable.btn_line_normal);
                lineButtons[i].setTextColor(getResources().getColor(R.color.textSecondary));
            }
        }
        if (guaView != null) {
            updateButtonLabels(guaView.getOriginalGua());
        }
    }

    private void highlightCurrentLines() {
    }

    @Override
    public void onLineChanged(int position, boolean isYang) {
        highlightCurrentLines();
        
        String currentGua = guaView.getCurrentGua();
        String originalGua = guaView.getOriginalGua();
        
        if (currentGua.equals(originalGua)) {
            isChanged = false;
            if (changeHint != null) {
                changeHint.setText("点击按钮变爻");
            }
            if (changeRuleExplain != null) {
                changeRuleExplain.setVisibility(View.GONE);
            }
            if (zhiguaContainer != null) {
                zhiguaContainer.setVisibility(View.GONE);
            }
        } else {
            isChanged = true;
            
            int changedLineCount = 0;
            for (int i = 0; i < 6; i++) {
                if (currentGua.charAt(i) != originalGua.charAt(i)) {
                    changedLineCount++;
                }
            }
            
            String prompt = "";
            String ruleExplain = "";
            
            if (changedLineCount == 1) {
                prompt = "点击提示恢复原卦";
                ruleExplain = "一爻变：以本卦变爻之辞为主占断。";
            } else if (changedLineCount == 2) {
                prompt = "点击提示恢复原卦";
                ruleExplain = "二爻变：以本卦两个变爻之辞为主占断，以上爻之辞为重。";
            } else if (changedLineCount == 3) {
                prompt = "点击提示恢复原卦";
                ruleExplain = "三爻变：本卦与之卦并重，以本卦卦辞与变爻之辞为主。";
            } else if (changedLineCount == 4) {
                prompt = "点击提示恢复原卦";
                ruleExplain = "四爻变：以之卦两个不变爻之辞为主占断，以下爻之辞为重。";
            } else if (changedLineCount == 5) {
                prompt = "点击提示恢复原卦";
                ruleExplain = "五爻变：以之卦不变爻之辞为主占断。";
            } else {
                prompt = "点击提示恢复原卦";
                ruleExplain = "六爻全变：乾坤二卦以用九、用六之辞占断，其他卦以之卦卦辞占断。";
            }
            
            if (changeHint != null) {
                changeHint.setText(prompt);
            }
            
            if (changeRuleExplain != null) {
                changeRuleExplain.setText(ruleExplain);
                changeRuleExplain.setVisibility(View.VISIBLE);
            }
            
            showZhigua(currentGua);
        }
    }
    
    private void showZhigua(String guaStr) {
        if (zhiguaContainer == null || zhiguaView == null) return;
        
        int zhiguaIndex = HexagramUtils.findHexagramIndexByGua(guaStr);
        if (zhiguaIndex >= 0) {
            JSONObject zhigua = HexagramDataStore.getInstance().get(zhiguaIndex);
            if (zhigua != null) {
                zhiguaView.setGua(HexagramUtils.getGua(zhigua));
                
                zhiguaName.setText(HexagramUtils.getHexagramName(zhigua));
                zhiguaPinyin.setText(HexagramUtils.getPinyin(zhigua));
                
                JSONObject interpretation = HexagramUtils.getInterpretation(zhigua);
                if (interpretation != null) {
                    String judgment = interpretation.optString("judgment", "");
                    zhiguaJudgment.setText(judgment.isEmpty() ? interpretation.optString("overall", "") : judgment);
                }
                
                zhiguaContainer.setVisibility(View.VISIBLE);
                return;
            }
        }
        
        zhiguaContainer.setVisibility(View.GONE);
    }

    private String getLineName(int position) {
        String[] names = {"初九", "九二", "九三", "九四", "九五", "上九"};
        return names[position];
    }

    @Override
    public void onAnimationComplete() {
        if (changeHint != null) {
            changeHint.setText("点击按钮变爻");
        }
        if (changeRuleExplain != null) {
            changeRuleExplain.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
        try {
            LinearLayout contentLayout = findViewById(R.id.contentLayout);
            if (contentLayout != null) {
                contentLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
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