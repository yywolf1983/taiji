package top.nones.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class CoinDivinationActivity extends AppCompatActivity {
    private TextView guideTitle, guideText, stepInfo;
    private Button btnStart, btnRestart;
    private Button btn3Yang, btn2Yang1Yin, btn1Yang2Yin, btn3Yin;
    private LinearLayout guideCard, selectionContainer, linesContainer, hexagramContainer, hexagramClickArea;
    private LinearLayout linesList;
    private HexagramView resultHexagram;
    private TextView resultName, resultPinyin, resultGuaType, resultGuaNumber;
    
    private int currentLineIndex = 0;
    private StringBuilder guaBuilder = new StringBuilder();
    
    private String[] positions = {"初", "二", "三", "四", "五", "上"};
    
    private String[] trigramNames = {"坤", "艮", "坎", "巽", "震", "离", "兑", "乾"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_divination);
        
        HexagramDataStore.getInstance().load(this);
        
        guideTitle = findViewById(R.id.guideTitle);
        guideText = findViewById(R.id.guideText);
        stepInfo = findViewById(R.id.stepInfo);
        
        guideCard = findViewById(R.id.guideCard);
        selectionContainer = findViewById(R.id.selectionContainer);
        linesContainer = findViewById(R.id.linesContainer);
        hexagramContainer = findViewById(R.id.hexagramContainer);
        
        btnStart = findViewById(R.id.btnStart);
        btnRestart = findViewById(R.id.btnRestart);
        
        btn3Yang = findViewById(R.id.btn3Yang);
        btn2Yang1Yin = findViewById(R.id.btn2Yang1Yin);
        btn1Yang2Yin = findViewById(R.id.btn1Yang2Yin);
        btn3Yin = findViewById(R.id.btn3Yin);
        
        linesList = findViewById(R.id.linesList);
        resultHexagram = findViewById(R.id.resultHexagram);
        resultName = findViewById(R.id.resultName);
        resultPinyin = findViewById(R.id.resultPinyin);
        resultGuaType = findViewById(R.id.resultGuaType);
        resultGuaNumber = findViewById(R.id.resultGuaNumber);
        hexagramClickArea = findViewById(R.id.hexagramClickArea);
        
        btnStart.setOnClickListener(v -> startDivination());
        btnRestart.setOnClickListener(v -> resetDivination());
        
        btn3Yang.setOnClickListener(v -> selectResult(3, 9, "九(老阳)", true));
        btn2Yang1Yin.setOnClickListener(v -> selectResult(2, 7, "七(少阳)", true));
        btn1Yang2Yin.setOnClickListener(v -> selectResult(1, 8, "八(少阴)", false));
        btn3Yin.setOnClickListener(v -> selectResult(0, 6, "六(老阴)", false));
        
        hexagramClickArea.setOnClickListener(v -> navigateToHexagram());
    }
    
    private String getReversedGua() {
        String gua = guaBuilder.toString();
        if (gua.length() != 6) return "";
        return new StringBuilder(gua).reverse().toString();
    }
    
    private void navigateToHexagram() {
        String reversedGua = getReversedGua();
        if (reversedGua.isEmpty()) return;
        int hexagramIndex = HexagramUtils.findHexagramIndexByGua(reversedGua);
        if (hexagramIndex >= 0) {
            Intent intent = new Intent(this, HexagramDetailActivity.class);
            intent.putExtra("hexagram_number", hexagramIndex);
            startActivity(intent);
        }
    }

    private volatile boolean destroyed = false;

    @Override
    protected void onDestroy() {
        destroyed = true;
        super.onDestroy();
        if (guideCard != null) guideCard.clearAnimation();
        if (selectionContainer != null) selectionContainer.clearAnimation();
        if (resultHexagram != null) {
            resultHexagram.setGua("");
        }
    }

    private void startDivination() {
        currentLineIndex = 0;
        guaBuilder.setLength(0);
        linesList.removeAllViews();
        
        btnStart.setVisibility(View.GONE);
        btnRestart.setVisibility(View.GONE);
        hexagramContainer.setVisibility(View.GONE);
        
        startLine1();
    }
    
    private void startLine1() {
        guideTitle.setText("第1爻 · 初爻");
        guideText.setText("请将三枚铜钱合在手心，默念问题后轻轻摇动，然后抛在桌面上。\n\n阳面（字）：○\n阴面（背）：●");
        
        guideCard.setVisibility(View.VISIBLE);
        selectionContainer.setVisibility(View.VISIBLE);
        
        stepInfo.setText("请选择第" + (currentLineIndex + 1) + "次投掷结果");
    }
    
    private void selectResult(int yangCount, int value, String resultText, boolean isYang) {
        String lineChar = isYang ? "1" : "0";
        guaBuilder.append(lineChar);
        
        String yangYin = isYang ? "九" : "六";
        String positionName = positions[currentLineIndex];
        
        addLineToDisplay(yangYin + positionName, isYang);
        
        currentLineIndex++;
        
        if (currentLineIndex < 6) {
            startNextLine();
        } else {
            showResult();
        }
    }
    
    private void startNextLine() {
        Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            
            @Override
            public void onAnimationEnd(Animation animation) {
                if (destroyed || isFinishing() || isDestroyed()) return;
                guideTitle.setText("第" + (currentLineIndex + 1) + "爻 · " + positions[currentLineIndex] + "爻");
                guideText.setText("请再次将三枚铜钱合在手心，默念问题后轻轻摇动，然后抛在桌面上。");
                stepInfo.setText("请选择第" + (currentLineIndex + 1) + "次投掷结果");
                
                Animation fadeIn = AnimationUtils.loadAnimation(CoinDivinationActivity.this, R.anim.fade_in);
                guideCard.startAnimation(fadeIn);
                selectionContainer.startAnimation(fadeIn);
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        
        guideCard.startAnimation(fadeOut);
        selectionContainer.startAnimation(fadeOut);
    }
    
    private void showResult() {
        guideCard.setVisibility(View.GONE);
        selectionContainer.setVisibility(View.GONE);
        
        String reversedGua = getReversedGua();
        
        int hexagramIndex = HexagramUtils.findHexagramIndexByGua(reversedGua);
        
        if (hexagramIndex >= 0) {
            try {
                JSONObject hexagram = HexagramUtils.getHexagram(hexagramIndex);
                String name = HexagramUtils.getHexagramName(hexagram);
                String pinyin = HexagramUtils.getPinyin(hexagram);
                
                String upperTrigram = getUpperTrigram(reversedGua);
                String lowerTrigram = getLowerTrigram(reversedGua);
                String guaType = upperTrigram + "上" + lowerTrigram + "下";
                String guaNumber = "第" + (hexagramIndex + 1) + "卦";
                
                resultName.setText(name);
                resultPinyin.setText(pinyin);
                resultGuaType.setText(guaType);
                resultGuaNumber.setText(guaNumber);
                
                hexagramContainer.setVisibility(View.VISIBLE);
                btnRestart.setVisibility(View.VISIBLE);
                
                hexagramContainer.post(() -> {
                    resultHexagram.setGua(reversedGua);
                });
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String getUpperTrigram(String gua) {
        if (gua.length() != 6) return "";
        String upperGua = gua.substring(0, 3);
        int upperIndex = Integer.parseInt(upperGua, 2);
        if (upperIndex >= 0 && upperIndex < trigramNames.length) {
            return trigramNames[upperIndex];
        }
        return "";
    }
    
    private String getLowerTrigram(String gua) {
        if (gua.length() != 6) return "";
        String lowerGua = gua.substring(3, 6);
        int lowerIndex = Integer.parseInt(lowerGua, 2);
        if (lowerIndex >= 0 && lowerIndex < trigramNames.length) {
            return trigramNames[lowerIndex];
        }
        return "";
    }
    
    private void resetDivination() {
        currentLineIndex = 0;
        guaBuilder.setLength(0);
        linesList.removeAllViews();
        
        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
        hexagramContainer.setVisibility(View.GONE);
        guideCard.setVisibility(View.GONE);
        selectionContainer.setVisibility(View.GONE);
        
        guideTitle.setText("准备工作");
        guideText.setText("请准备三枚铜钱，心中默念您想询问的问题。");
    }
    
    private void addLineToDisplay(String lineName, boolean isYang) {
        LinearLayout lineItem = new LinearLayout(this);
        lineItem.setOrientation(LinearLayout.HORIZONTAL);
        lineItem.setGravity(android.view.Gravity.CENTER);
        lineItem.setPadding(0, 4, 0, 4);
        
        TextView nameView = new TextView(this);
        nameView.setText(lineName);
        nameView.setTextSize(14);
        nameView.setTextColor(0xFF6A6560);
        
        LineView lineView = new LineView(this);
        lineView.setYang(isYang);
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            150,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(20, 0, 0, 0);
        lineView.setLayoutParams(lp);
        
        lineItem.addView(nameView);
        lineItem.addView(lineView);
        
        linesList.addView(lineItem);
    }
}