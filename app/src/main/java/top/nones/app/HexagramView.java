package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class HexagramView extends View {
    private Paint yangPaint;
    private Paint yinPaint;
    private String gua = "";

    // 使用 dp 转换后的像素值
    private float lineSpacing;
    private float lineHeight;
    private float yinGap;

    public HexagramView(Context context) {
        super(context);
        init(context);
    }

    public HexagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HexagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // dp 转 px
        lineSpacing = dpToPx(context, 4);
        lineHeight = dpToPx(context, 3);
        yinGap = dpToPx(context, 5);

        setBackgroundColor(0xFFFFFFFF);

        yangPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yangPaint.setColor(0xFF2196F3);
        yangPaint.setStrokeWidth(dpToPx(context, 3.5f));
        yangPaint.setStyle(Paint.Style.FILL);

        yinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setColor(0xFFFF5722);
        yinPaint.setStrokeWidth(dpToPx(context, 3.5f));
        yinPaint.setStyle(Paint.Style.FILL);

        // 启用软件渲染以支持阴影
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        yangPaint.setShadowLayer(4, 0, 2, 0x40000000);
        yinPaint.setShadowLayer(4, 0, 2, 0x40000000);
    }

    private float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gua == null || gua.length() != 6) return;

        int width = getWidth();
        int height = getHeight();
        float startX = width / 12f;
        float lineWidth = width * 5f / 6;
        float startY = height - lineHeight;

        for (int i = 0; i < 6; i++) {
            float y = startY - i * (lineHeight + lineSpacing);
            if (gua.charAt(5 - i) == '1') {
                // 阳爻：实线
                canvas.drawRect(startX, y - lineHeight / 2,
                        startX + lineWidth, y + lineHeight / 2, yangPaint);
            } else {
                // 阴爻：两段
                canvas.drawRect(startX, y - lineHeight / 2,
                        startX + (lineWidth - yinGap) / 2, y + lineHeight / 2, yinPaint);
                canvas.drawRect(startX + (lineWidth + yinGap) / 2, y - lineHeight / 2,
                        startX + lineWidth, y + lineHeight / 2, yinPaint);
            }
        }
    }

    public void setGua(String gua) {
        if (gua != null && !gua.equals(this.gua)) {
            this.gua = gua;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) ((lineHeight + lineSpacing) * 6 + lineSpacing);
        setMeasuredDimension(width, height);
    }
}
