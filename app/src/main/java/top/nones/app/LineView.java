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

public class LineView extends View {
    private Paint yangPaint;
    private Paint yinPaint;
    private boolean isYang = true;
    
    private float lineHeight;
    private float yinGap;
    
    public LineView(Context context) {
        super(context);
        init(context);
    }
    
    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context) {
        lineHeight = dpToPx(context, 6);
        yinGap = dpToPx(context, 8);
        
        yangPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yangPaint.setStyle(Paint.Style.FILL);
        
        yinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setStyle(Paint.Style.FILL);
        
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }
    
    private float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
    
    public void setYang(boolean yang) {
        this.isYang = yang;
        invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        int width = getWidth();
        int height = getHeight();
        
        float centerY = height / 2f;
        
        updatePaints(width, height);
        
        if (isYang) {
            RectF rect = new RectF(0, centerY - lineHeight / 2,
                    width, centerY + lineHeight / 2);
            canvas.drawRoundRect(rect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yangPaint);
        } else {
            float segmentWidth = (width - yinGap) / 2;
            RectF leftRect = new RectF(0, centerY - lineHeight / 2,
                    segmentWidth, centerY + lineHeight / 2);
            RectF rightRect = new RectF(segmentWidth + yinGap, centerY - lineHeight / 2,
                    width, centerY + lineHeight / 2);
            canvas.drawRoundRect(leftRect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yinPaint);
            canvas.drawRoundRect(rightRect, dpToPx(getContext(), 2), dpToPx(getContext(), 2), yinPaint);
        }
    }
    
    private void updatePaints(int width, int height) {
        // 阳爻：古铜金，沉稳温润
        LinearGradient yangGradient = new LinearGradient(
                0, 0, width, 0,
                0xFFB8943E, 0xFFC9A45C,
                Shader.TileMode.CLAMP);
        yangPaint.setShader(yangGradient);
        yangPaint.setShadowLayer(8, 0, 2, 0x40C9A45C);
        
        // 阴爻：墨蓝，沉静如夜
        LinearGradient yinGradient = new LinearGradient(
                0, 0, width, 0,
                0xFF4A5B7A, 0xFF5B6A8A,
                Shader.TileMode.CLAMP);
        yinPaint.setShader(yinGradient);
        yinPaint.setShadowLayer(6, 0, 2, 0x305B6A8A);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (lineHeight * 2);
        setMeasuredDimension(width, height);
    }
}