package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class HexagramView extends View {
    private Paint yangPaint;
    private Paint yinPaint;
    private Paint backgroundPaint;
    private Paint borderPaint;
    private String gua = "";

    // 使用 dp 转换后的像素值
    private float lineSpacing;
    private float lineHeight;
    private float yinGap;
    private float cornerRadius;

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
        lineSpacing = dpToPx(context, 5);
        lineHeight = dpToPx(context, 4);
        yinGap = dpToPx(context, 6);
        cornerRadius = dpToPx(context, 8);

        // 背景画笔
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(0xFFF8F8F8);
        backgroundPaint.setStyle(Paint.Style.FILL);

        // 边框画笔
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(0xFFE0E0E0);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(dpToPx(context, 1));

        // 阳爻画笔 - 使用渐变效果
        yangPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yangPaint.setStyle(Paint.Style.FILL);

        // 阴爻画笔 - 使用渐变效果
        yinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setStyle(Paint.Style.FILL);

        // 启用软件渲染以支持阴影和渐变
        setLayerType(LAYER_TYPE_SOFTWARE, null);
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

        // 绘制背景
        RectF backgroundRect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint);
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, borderPaint);

        float startX = width / 10f;
        float lineWidth = width * 4f / 5;
        float startY = height - lineHeight - dpToPx(getContext(), 4);

        // 更新画笔颜色（需要根据视图大小调整渐变）
        updatePaints(width, height);

        for (int i = 0; i < 6; i++) {
            float y = startY - i * (lineHeight + lineSpacing);
            if (gua.charAt(5 - i) == '1') {
                // 阳爻：实线
                RectF rect = new RectF(startX, y - lineHeight / 2,
                        startX + lineWidth, y + lineHeight / 2);
                canvas.drawRoundRect(rect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yangPaint);
            } else {
                // 阴爻：两段
                float segmentWidth = (lineWidth - yinGap) / 2;
                RectF leftRect = new RectF(startX, y - lineHeight / 2,
                        startX + segmentWidth, y + lineHeight / 2);
                RectF rightRect = new RectF(startX + segmentWidth + yinGap, y - lineHeight / 2,
                        startX + lineWidth, y + lineHeight / 2);
                canvas.drawRoundRect(leftRect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yinPaint);
                canvas.drawRoundRect(rightRect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yinPaint);
            }
        }
    }

    private void updatePaints(int width, int height) {
        // 阳爻渐变：蓝色系
        LinearGradient yangGradient = new LinearGradient(
                0, 0, width, height,
                0xFF1976D2, 0xFF42A5F5,
                Shader.TileMode.CLAMP);
        yangPaint.setShader(yangGradient);
        yangPaint.setShadowLayer(6, 0, 3, 0x30000000);

        // 阴爻渐变：橙色系
        LinearGradient yinGradient = new LinearGradient(
                0, 0, width, height,
                0xFFE65100, 0xFFFF9800,
                Shader.TileMode.CLAMP);
        yinPaint.setShader(yinGradient);
        yinPaint.setShadowLayer(6, 0, 3, 0x30000000);
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
        int height = (int) ((lineHeight + lineSpacing) * 6 + lineSpacing + dpToPx(getContext(), 8));
        setMeasuredDimension(width, height);
    }
}
