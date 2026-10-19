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
    private Button btnLaoYang, btnShaoYang, btnShaoYin, btnLaoYin;
    private LinearLayout guideCard, selectionContainer, linesContainer, hexagramContainer, hexagramClickArea;
    private LinearLayout linesList;
    private HexagramView resultHexagram;
    private TextView resultName;
    
    private int currentLineIndex = 0;
    private StringBuilder guaBuilder = new StringBuilder();
    
    private String[] positions = {"初", "二", "三", "四", "五", "上"};
    
    /** 三枚铜钱/蓍草标准映射：0-7 坤, 1 震, 2 坎, 3 兑, 4 艮, 5 离, 6 巽, 7 乾 */
    private String[] trigramNames = {"坤", "震", "坎", "兑", "艮", "离", "巽", "乾"};
    
    /** 蓍草三变后四种结果：老阳9/少阳7 → 阳；少阴8/老阴6 → 阴 */
    private static final int LAO_YANG = 9;
    private static final int SHAO_YANG = 7;
    private static final int SHAO_YIN = 8;
    private static final int LAO_YIN = 6;
    
    private volatile boolean destroyed = false;
    
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
        
        // 四个按钮对应：老阳(9)、少阳(7)、少阴(8)、老阴(6)
        btnLaoYang = findViewById(R.id.btnRemainder1);
        btnShaoYang = findViewById(R.id.btnRemainder2);
        btnShaoYin = findViewById(R.id.btnRemainder3);
        btnLaoYin = findViewById(R.id.btnRemainder4);
        
        linesList = findViewById(R.id.linesList);
        resultHexagram = findViewById(R.id.resultHexagram);
        resultName = findViewById(R.id.resultName);
        hexagramClickArea = findViewById(R.id.hexagramClickArea);
        
        btnStart.setOnClickListener(v -> startDivination());
        btnRestart.setOnClickListener(v -> resetDivination());
        
        btnLaoYang.setOnClickListener(v -> selectLineValue(LAO_YANG, "九(老阳)", true));
        btnShaoYang.setOnClickListener(v -> selectLineValue(SHAO_YANG, "七(少阳)", true));
        btnShaoYin.setOnClickListener(v -> selectLineValue(SHAO_YIN, "八(少阴)", false));
        btnLaoYin.setOnClickListener(v -> selectLineValue(LAO_YIN, "六(老阴)", false));
        
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
        
        updateGuide();
    }
    
    private void updateGuide() {
        int lineNum = currentLineIndex + 1;
        String posName = positions[currentLineIndex];
        
        guideTitle.setText("第" + lineNum + "爻 · " + posName + "爻");
        guideText.setText("蓍草三变之后，余数除以4的结果只有四种可能：\n\n" +
                "老阳(九/9)：三爻皆阳，动爻\n" +
                "少阳(七/7)：两阴一阳，静爻\n" +
                "少阴(八/8)：两阳一阴，静爻\n" +
                "老阴(六/6)：三爻皆阴，动爻\n\n" +
                "请选择本次蓍草占卜所得的结果。");
        
        guideCard.setVisibility(View.VISIBLE);
        selectionContainer.setVisibility(View.VISIBLE);
        
        stepInfo.setText("请选择第" + lineNum + "爻结果");
    }
    
    private void selectLineValue(int value, String resultText, boolean isYang) {
        String lineChar = isYang ? "1" : "0";
        guaBuilder.append(lineChar);
        
        String yangYin = isYang ? "九" : "六";
        String positionName = positions[currentLineIndex];
        
        addLineToDisplay(yangYin + positionName + " · " + resultText, isYang);
        
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
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            
            @Override
            public void onAnimationEnd(Animation animation) {
                if (destroyed || isFinishing() || isDestroyed()) return;
                
                updateGuide();
                
                Animation fadeIn = AnimationUtils.loadAnimation(YarrowDivinationActivity.this, R.anim.fade_in);
                guideCard.startAnimation(fadeIn);
                selectionContainer.startAnimation(fadeIn);
            }
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
                resultName.setText(name);
                
                hexagramContainer.setVisibility(View.VISIBLE);
                btnRestart.setVisibility(View.VISIBLE);
                
                hexagramContainer.post(() -> resultHexagram.setGua(reversedGua));
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String getUpperTrigram(String gua) {
        if (gua.length() != 6) return "";
        int idx = Integer.parseInt(gua.substring(0, 3), 2);
        return trigramNames[idx];
    }
    
    private String getLowerTrigram(String gua) {
        if (gua.length() != 6) return "";
        int idx = Integer.parseInt(gua.substring(3, 6), 2);
        return trigramNames[idx];
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
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(20, 0, 0, 0);
        lineView.setLayoutParams(lp);
        
        lineItem.addView(nameView);
        lineItem.addView(lineView);
        
        linesList.addView(lineItem);
    }
}
