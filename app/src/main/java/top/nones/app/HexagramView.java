package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class HexagramView extends View {
    private Paint yangPaint;
    private Paint yinPaint;
    private Paint backgroundPaint;
    private Paint borderPaint;
    private String gua = "";
    private String originalGua = "";
    
    private float lineSpacing;
    private float lineHeight;
    private float yinGap;
    private float cornerRadius;
    
    private int visibleLines = 0;
    private boolean isAnimating = false;
    private Handler handler;
    
    private OnLineChangeListener onLineChangeListener;
    
    public interface OnLineChangeListener {
        void onLineChanged(int position, boolean isYang);
        void onAnimationComplete();
    }

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
        lineSpacing = dpToPx(context, 8);
        lineHeight = dpToPx(context, 6);
        yinGap = dpToPx(context, 8);
        cornerRadius = dpToPx(context, 10);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(0xFF1A1A1A);
        backgroundPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(0xFF4A4A4A);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(dpToPx(context, 1));

        yangPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yangPaint.setStyle(Paint.Style.FILL);

        yinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setStyle(Paint.Style.FILL);

        handler = new Handler(Looper.getMainLooper());
        
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

        RectF backgroundRect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint);
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, borderPaint);

        float startX = width / 10f;
        float lineWidth = width * 4f / 5;
        float startY = height - lineHeight - dpToPx(getContext(), 4);

        updatePaints(width, height);

        for (int i = 0; i < 6; i++) {
            if (i >= visibleLines && !isAnimating) continue;
            
            float y = startY - i * (lineHeight + lineSpacing);
            boolean isYang = gua.charAt(5 - i) == '1';
            
            drawLine(canvas, startX, y, lineWidth, isYang);
        }
    }

    private void drawLine(Canvas canvas, float startX, float y, float lineWidth, boolean isYang) {
        if (isYang) {
            RectF rect = new RectF(startX, y - lineHeight / 2,
                    startX + lineWidth, y + lineHeight / 2);
            canvas.drawRoundRect(rect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yangPaint);
        } else {
            float segmentWidth = (lineWidth - yinGap) / 2;
            RectF leftRect = new RectF(startX, y - lineHeight / 2,
                    startX + segmentWidth, y + lineHeight / 2);
            RectF rightRect = new RectF(startX + segmentWidth + yinGap, y - lineHeight / 2,
                    startX + lineWidth, y + lineHeight / 2);
            canvas.drawRoundRect(leftRect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yinPaint);
            canvas.drawRoundRect(rightRect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yinPaint);
        }
    }

    private void updatePaints(int width, int height) {
        LinearGradient yangGradient = new LinearGradient(
                0, 0, width, height,
                0xFFD4AF37, 0xFFFFD700,
                Shader.TileMode.CLAMP);
        yangPaint.setShader(yangGradient);
        yangPaint.setShadowLayer(6, 0, 3, 0x40D4AF37);

        LinearGradient yinGradient = new LinearGradient(
                0, 0, width, height,
                0xFF7B68EE, 0xFF9370DB,
                Shader.TileMode.CLAMP);
        yinPaint.setShader(yinGradient);
        yinPaint.setShadowLayer(6, 0, 3, 0x407B68EE);
    }

    public void setGua(String gua) {
        if (gua != null && !gua.equals(this.gua)) {
            this.gua = gua;
            this.originalGua = gua;
            visibleLines = 0;
            invalidate();
            startRevealAnimation();
        }
    }

    public void setOnLineChangeListener(OnLineChangeListener listener) {
        this.onLineChangeListener = listener;
    }

    private void startRevealAnimation() {
        isAnimating = true;
        visibleLines = 0;
        handler.removeCallbacksAndMessages(null);
        
        handler.postDelayed(new Runnable() {
            int currentLine = 0;
            
            @Override
            public void run() {
                if (currentLine < 6) {
                    visibleLines = currentLine + 1;
                    invalidate();
                    
                    if (onLineChangeListener != null) {
                        boolean isYang = gua.charAt(5 - currentLine) == '1';
                        onLineChangeListener.onLineChanged(currentLine, isYang);
                    }
                    
                    currentLine++;
                    handler.postDelayed(this, 400);
                } else {
                    isAnimating = false;
                    invalidate();
                    if (onLineChangeListener != null) {
                        onLineChangeListener.onAnimationComplete();
                    }
                }
            }
        }, 300);
    }

    private String tempGua = "";
    
    public void toggleLine(int position) {
        if (position < 0 || position >= 6) return;
        
        if (tempGua.isEmpty()) {
            tempGua = gua;
        }
        
        char[] guaChars = tempGua.toCharArray();
        guaChars[5 - position] = guaChars[5 - position] == '1' ? '0' : '1';
        tempGua = new String(guaChars);
        
        if (onLineChangeListener != null) {
            boolean isYang = tempGua.charAt(5 - position) == '1';
            onLineChangeListener.onLineChanged(position, isYang);
        }
    }
    
    public String getCurrentGua() {
        return tempGua.isEmpty() ? gua : tempGua;
    }
    
    public void resetGua() {
        tempGua = "";
        visibleLines = 6;
        isAnimating = false;
        invalidate();
    }

    public String getOriginalGua() {
        return originalGua;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) ((lineHeight + lineSpacing) * 6 + lineSpacing + dpToPx(getContext(), 8));
        setMeasuredDimension(width, height);
    }
}