package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HexagramView extends View {
    private Paint yangPaint;  // 阳爻画笔
    private Paint yinPaint;   // 阴爻画笔
    private String gua = "";
    private static final int LINE_SPACING = 16;   // 减小爻线间距
    private static final int LINE_HEIGHT = 12;    // 减小爻线高度
    private static final int YIN_GAP = 20;        // 调整阴爻间隔

    public HexagramView(Context context) {
        super(context);
        init();
    }

    public HexagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HexagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(0xFFFFFFFF);  // 设置纯白色背景，确保在深色主题下也保持一致

        yangPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yangPaint.setColor(0xFF2196F3);  // 蓝色，代表阳
        yangPaint.setStrokeWidth(14);    // 增加线宽
        yangPaint.setStyle(Paint.Style.FILL);
        yangPaint.setShadowLayer(4, 0, 2, 0x40000000);  // 添加阴影效果

        yinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setColor(0xFFFF5722);   // 橙色，代表阴
        yinPaint.setStrokeWidth(14);     // 增加线宽
        yinPaint.setStyle(Paint.Style.FILL);
        yinPaint.setShadowLayer(4, 0, 2, 0x40000000);  // 添加阴影效果
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gua == null || gua.length() != 6) return;

        int width = getWidth();
        int height = getHeight();
        int startX = width / 12;             // 更靠左
        int lineWidth = width * 5 / 6;       // 更长的线条
        int startY = height - LINE_HEIGHT;    // 调整起始位置

        // 从下到上绘制六爻
        for (int i = 0; i < 6; i++) {
            int y = startY - i * (LINE_HEIGHT + LINE_SPACING);
            Paint paint = gua.charAt(5-i) == '1' ? yangPaint : yinPaint;
            
            if (gua.charAt(5-i) == '1') {
                // 阳爻：实线
                canvas.drawRect(startX, y - LINE_HEIGHT/2, 
                              startX + lineWidth, y + LINE_HEIGHT/2, paint);
            } else {
                // 阴爻：两段线
                float gap = YIN_GAP;
                canvas.drawRect(startX, y - LINE_HEIGHT/2,
                              startX + (lineWidth - gap)/2, y + LINE_HEIGHT/2, paint);
                canvas.drawRect(startX + (lineWidth + gap)/2, y - LINE_HEIGHT/2,
                              startX + lineWidth, y + LINE_HEIGHT/2, paint);
            }
        }
    }

    public void setGua(String gua) {
        this.gua = gua;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (LINE_HEIGHT + LINE_SPACING) * 6;  // 减小整体高度
        setMeasuredDimension(width, height);
    }
}