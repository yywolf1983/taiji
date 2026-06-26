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

public class YarrowDivinationActivity extends AppCompatActivity {
    private TextView guideTitle, guideText, stepInfo;
    private Button btnStart, btnRestart;
    private Button btnRemainder1, btnRemainder2, btnRemainder3, btnRemainder4;
    private LinearLayout guideCard, selectionContainer, linesContainer, hexagramContainer, hexagramClickArea;
    private LinearLayout linesList;
    private HexagramView resultHexagram;
    private TextView resultName;
    
    private int currentLineIndex = 0;
    private StringBuilder guaBuilder = new StringBuilder();
    
    private int currentChange = 0;
    private int totalSticks = 49;
    private int leftRemainder = 0;
    private int rightRemainder = 0;
    private int currentStep = 0;
    
    private String[] positions = {"初", "二", "三", "四", "五", "上"};
    
    private String[] trigramNames = {"坤", "艮", "坎", "巽", "震", "离", "兑", "乾"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarrow_divination);
        
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
        
        btnRemainder1 = findViewById(R.id.btnRemainder1);
        btnRemainder2 = findViewById(R.id.btnRemainder2);
        btnRemainder3 = findViewById(R.id.btnRemainder3);
        btnRemainder4 = findViewById(R.id.btnRemainder4);
        
        linesList = findViewById(R.id.linesList);
        resultHexagram = findViewById(R.id.resultHexagram);
        resultName = findViewById(R.id.resultName);
        hexagramClickArea = findViewById(R.id.hexagramClickArea);
        
        btnStart.setOnClickListener(v -> startDivination());
        btnRestart.setOnClickListener(v -> resetDivination());
        
        btnRemainder1.setOnClickListener(v -> selectRemainder(1));
        btnRemainder2.setOnClickListener(v -> selectRemainder(2));
        btnRemainder3.setOnClickListener(v -> selectRemainder(3));
        btnRemainder4.setOnClickListener(v -> selectRemainder(4));
        
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
        totalSticks = 49;
        currentChange = 1;
        
        guideTitle.setText("第1爻 · 初爻");
        guideText.setText("第一变：\n\n1. 取49根蓍草，随机分成两堆\n2. 从右堆取1根夹在小指间\n3. 左堆每4根一数，记录余数");
        
        guideCard.setVisibility(View.VISIBLE);
        selectionContainer.setVisibility(View.VISIBLE);
        
        stepInfo.setText("第" + (currentLineIndex + 1) + "爻 · 第" + currentChange + "变\n请选择左堆余数");
    }
    
    private void selectRemainder(int remainder) {
        if (currentStep == 0) {
            leftRemainder = remainder;
            
            guideTitle.setText("第" + (currentLineIndex + 1) + "爻 · 初爻");
            guideText.setText("第一变：\n\n4. 右堆每4根一数，记录余数");
            
            stepInfo.setText("请选择右堆余数");
            currentStep = 1;
            
        } else if (currentStep == 1) {
            rightRemainder = remainder;
            
            int removed = leftRemainder + rightRemainder + 1;
            totalSticks -= removed;
            
            currentChange++;
            
            if (currentChange <= 3) {
                currentStep = 0;
                
                guideTitle.setText("第" + (currentLineIndex + 1) + "爻 · " + positions[currentLineIndex] + "爻");
                guideText.setText("第" + currentChange + "变：\n\n1. 将剩余的" + totalSticks + "根蓍草混合\n2. 再次随机分成两堆\n3. 从右堆取1根夹在小指间\n4. 左堆每4根一数，记录余数");
                
                stepInfo.setText("第" + currentChange + "变\n请选择左堆余数");
                
            } else {
                showLineResult();
            }
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

    private void showLineResult() {
        currentStep = 0;
        
        int lineValue = totalSticks / 4;
        
        boolean isYang = (lineValue == 7 || lineValue == 9);
        String lineChar = isYang ? "1" : "0";
        guaBuilder.append(lineChar);
        
        String yangYin = isYang ? "九" : "六";
        String positionName = positions[currentLineIndex];
        
        addLineToDisplay(yangYin + positionName, isYang);
        
        currentLineIndex++;
        
        if (currentLineIndex < 6) {
            handlerTransition(() -> {
                totalSticks = 49;
                currentChange = 1;
                
                guideTitle.setText("第" + (currentLineIndex + 1) + "爻 · " + positions[currentLineIndex] + "爻");
                guideText.setText("第一变：\n\n1. 取49根蓍草，随机分成两堆\n2. 从右堆取1根夹在小指间\n3. 左堆每4根一数，记录余数");
                
                stepInfo.setText("第" + (currentLineIndex + 1) + "爻 · 第" + currentChange + "变\n请选择左堆余数");
            });
            
        } else {
            guideCard.setVisibility(View.GONE);
            selectionContainer.setVisibility(View.GONE);
            
            showResult();
        }
    }
    
    private void handlerTransition(Runnable onComplete) {
        Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            
            @Override
            public void onAnimationEnd(Animation animation) {
                if (destroyed || isFinishing() || isDestroyed()) return;
                onComplete.run();
                
                Animation fadeIn = AnimationUtils.loadAnimation(YarrowDivinationActivity.this, R.anim.fade_in);
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
                resultName.setText(name);
                
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
        totalSticks = 49;
        currentChange = 0;
        currentStep = 0;
        
        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
        hexagramContainer.setVisibility(View.GONE);
        guideCard.setVisibility(View.GONE);
        selectionContainer.setVisibility(View.GONE);
        
        guideTitle.setText("准备工作");
        guideText.setText("请准备49根蓍草或牙签，心中默念您想询问的问题。");
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