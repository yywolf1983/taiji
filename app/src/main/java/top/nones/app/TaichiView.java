package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TaichiView extends View implements View.OnClickListener {
    private Paint whitePaint;
    private Paint blackPaint;
    private RectF rectF;

    public TaichiView(Context context) {
        super(context);
        init(context);
    }

    public TaichiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(0xFFF5F5F5); // 使用与背景渐变起始色相同的颜色
        whitePaint.setStyle(Paint.Style.FILL);

        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setColor(0xFF333333); // 使用较柔和的深色
        blackPaint.setStyle(Paint.Style.FILL);

        rectF = new RectF();
        
        // 设置可点击并注册点击监听器
        setClickable(true);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 生成1-64的随机数
        int hexagramNumber = (int) (Math.random() * 64) + 1;
        // 启动卦象详情页面
        HexagramDetailActivity.start(getContext(), hexagramNumber);
        invalidate(); // 重绘太极图
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        float radius = size * 0.45f;  // 增大太极图显示比例
        float centerX = width / 2f;
        float centerY = height / 2f;

        // 使用统一的颜色配置
        int backgroundColor = 0xFF333333;  // 深灰色背景
        int foregroundColor = 0xFFFFFFFF;  // 浅灰色前景
        whitePaint.setColor(foregroundColor);
        blackPaint.setColor(backgroundColor);

        // 绘制背景圆
        canvas.drawCircle(centerX, centerY, radius, blackPaint);

        // 绘制前景半圆
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, 270, 180, true, whitePaint);

        // 绘制大圆
        float bigRadius = radius / 2;
        canvas.drawCircle(centerX, centerY - bigRadius, bigRadius, whitePaint);
        canvas.drawCircle(centerX, centerY + bigRadius, bigRadius, blackPaint);

        // 绘制小圆
        float smallRadius = radius / 6;
        canvas.drawCircle(centerX, centerY - bigRadius, smallRadius, blackPaint);
        canvas.drawCircle(centerX, centerY + bigRadius, smallRadius, whitePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }
}