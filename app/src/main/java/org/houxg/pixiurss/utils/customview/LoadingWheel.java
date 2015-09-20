package org.houxg.pixiurss.utils.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import org.houxg.pixiurss.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Loading Widget
 */
public class LoadingWheel extends View {

    private int strokeColor = Color.GRAY;
    private int strokeWidth = 20;
    private int startDuration = 1000;
    private int endDuration = 600;
    private float rad = 150;    //半径，包含线宽
    private float DIVIATION = 30;


    private float startPosition = -45;    //起始绘制点
    private float sweepAngle = 0;   //扫过的角度

    long UPDATE_DURATION = 10;  //动画更新间隔
    float OFFSET = 1;

    Paint paint;

    public LoadingWheel(Context context) {
        super(context);
        init();
    }

    public LoadingWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingWheel, 0, 0);

        strokeColor = a.getColor(R.styleable.LoadingWheel_strokeColor, Color.GRAY);
        strokeWidth = (int) a.getDimension(R.styleable.LoadingWheel_strokeWidth, 20);
        startDuration = a.getInt(R.styleable.LoadingWheel_startDuration, 1000);
        endDuration = a.getInt(R.styleable.LoadingWheel_endDuration, 600);
        rad = a.getDimension(R.styleable.LoadingWheel_wheelRad, 150);

        a.recycle();
        init();
    }

    public LoadingWheel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingWheel, defStyleAttr, 0);

        strokeColor = a.getColor(R.styleable.LoadingWheel_strokeColor, Color.GRAY);
        strokeWidth = (int) a.getDimension(R.styleable.LoadingWheel_strokeWidth, 20);
        startDuration = a.getInt(R.styleable.LoadingWheel_startDuration, 1000);
        endDuration = a.getInt(R.styleable.LoadingWheel_endDuration, 600);
        rad = a.getDimension(R.styleable.LoadingWheel_wheelRad, 150);

        a.recycle();

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        path = new Path();
        rec = new RectF();
        rec.set(strokeWidth, strokeWidth, rad * 2 - strokeWidth, rad * 2 - strokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthSize;
        int height = heightSize;

        int desireWid = (int) (getPaddingLeft() + rad * 2 + getPaddingRight());
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSize, desireWid);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = desireWid;
                break;
        }

        int desireHei = (int) (getPaddingTop() + rad * 2 + getPaddingBottom());
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, desireHei);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = desireHei;
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rec, startPosition, sweepAngle, false, paint);
    }

    Path path;
    RectF rec;

    public void start() {
        LittleAnimator animatorStart = new LittleAnimator(startDuration, new AccelerateDecelerateInterpolator(), 0, 360 - DIVIATION)
                .setListener(new LittleAnimator.OnValueUpdateListener() {
                    @Override
                    public void onUpdate(float output) {
                        sweepAngle = output;
                        startPosition += OFFSET;
                    }

                    @Override
                    public void onEnd() {
                        startPosition -= DIVIATION;
                    }
                });
        LittleAnimator animatorEnd = new LittleAnimator(endDuration, new AccelerateDecelerateInterpolator(), -(360 - DIVIATION), 0)
                .setListener(new LittleAnimator.OnValueUpdateListener() {
                    @Override
                    public void onUpdate(float output) {
                        sweepAngle = output;
                        startPosition += OFFSET;
                    }

                    @Override
                    public void onEnd() {
                    }
                });

        List<LittleAnimator> animators = new ArrayList<>();
        animators.add(animatorStart);
        animators.add(animatorEnd);

        if (executor != null) {
            executor.stop();
        }
        startPosition = -45;  //初始化位置
        executor = new AnimatorExecutor();
        executor.animators = animators;

        new Thread(executor).start();
    }

    public void stop() {
        if (executor != null) {
            executor.stop();
        }
    }

    public boolean isRunning() {
        return !(executor == null || !executor.running);
    }

    AnimatorExecutor executor;

    static class LittleAnimator {
        int duration;
        Interpolator interpolator;
        float start;
        float end;
        OnValueUpdateListener listener;

        public interface OnValueUpdateListener {
            void onUpdate(float output);

            void onEnd();
        }

        public LittleAnimator(int duration, Interpolator interpolator, float start, float end) {
            this.duration = duration;
            this.interpolator = interpolator;
            this.start = start;
            this.end = end;
        }

        public void postUpdate(float output) {
            if (listener != null) {
                listener.onUpdate(output);
            }
        }

        public void postEnd() {
            if (listener != null) {
                listener.onEnd();
            }
        }

        public LittleAnimator setListener(OnValueUpdateListener listener) {
            this.listener = listener;
            return this;
        }
    }

    class AnimatorExecutor implements Runnable {

        List<LittleAnimator> animators;
        boolean running = true;

        public void stop() {
            running = false;
        }

        @Override
        public void run() {
            if (animators == null || animators.size() == 0) {
                return;
            }
            while (true) {
                int size = animators.size();
                for (int i = 0; i < size; i++) {
                    LittleAnimator animator = animators.get(i);
                    float time = 0;
                    float multiple;

                    while (time <= animator.duration) {
                        if (!running) {
                            return;
                        }
                        if (animator.duration < 0) {
                            multiple = 0;
                        } else {
                            multiple = time / animator.duration;
                            multiple = animator.interpolator.getInterpolation(multiple);
                        }

                        float out = animator.start + (animator.end - animator.start) * multiple;
                        animator.postUpdate(out);
                        postInvalidate();
                        try {
                            Thread.sleep(UPDATE_DURATION);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time += UPDATE_DURATION;
                    }
                    animator.postEnd();

                }
            }
        }
    }
}