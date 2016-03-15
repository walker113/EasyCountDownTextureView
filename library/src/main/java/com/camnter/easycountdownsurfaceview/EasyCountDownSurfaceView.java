package com.camnter.easycountdownsurfaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Date;

/**
 * Description：EasyCountDownSurfaceView
 * Created by：CaMnter
 * Time：2016-03-15 19:49
 */
public class EasyCountDownSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {

    private static final int DEFAULT_COLOR_BACKGROUND = 0xff000000;
    private static final int DEFAULT_COLOR_TIME = 0xffffffff;
    private DisplayMetrics mMetrics;

    private static final int COUNT_DOWN_INTERVAL = 1000;

    private long mMillisInFuture = 99000L;

    private static final float DEFAULT_BACKGROUND_PAINT_WIDTH = 0.66f;
    private static final float DEFAULT_TIME_PAINT_WIDTH = 0.66f;

    private double increaseTime = 0.0d;
    private long lastTimeRefresh = 0L;

    private SurfaceHolder mHolder;
    private EasyThread mThread;

    private Paint timePaint;
    private Paint backgroundPaint;

    public EasyCountDownSurfaceView(Context context) {
        super(context);
        this.init();
    }

    public EasyCountDownSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public EasyCountDownSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EasyCountDownSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    private void init() {
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.mHolder = this.getHolder();
        this.mHolder.addCallback(this);

        this.setZOrderOnTop(true);
        this.mHolder.setFormat(PixelFormat.TRANSPARENT);

        this.initPaints();
        this.mThread = new EasyThread();

    }

    private void initPaints() {
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setAntiAlias(true);
        this.backgroundPaint.setColor(DEFAULT_COLOR_BACKGROUND);
        this.backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.backgroundPaint.setStrokeWidth(this.dp2px(DEFAULT_BACKGROUND_PAINT_WIDTH));

        this.timePaint = new Paint();
        this.timePaint.setAntiAlias(true);
        this.timePaint.setColor(DEFAULT_COLOR_TIME);
        this.timePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.timePaint.setStrokeWidth(this.dp2px(DEFAULT_TIME_PAINT_WIDTH));
        this.timePaint.setTextSize(30);
    }


    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == mThread) {
            mThread = new EasyThread();
            mThread.start();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != mThread) {
            mThread.stopThread();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Start count down by date
     *
     * @param date date
     */
    public void start(Date date) {
        this.mThread.start();
    }

    /**
     * Start count down by timeMillis
     *
     * @param timeMillis timeMillis
     */
    public void start(long timeMillis) {

    }

    /**
     * Stop count down
     */
    public void stop() {

    }

    public void start() {
        if (this.mThread.isAlive()) {
            this.mThread.start();
        } else {
            this.mThread.run();
        }
    }

    private class EasyThread extends Thread {

        public boolean isRunning = false;

        public EasyThread() {
            this.isRunning = true;
        }

        public synchronized final void stopThread() {
            isRunning = false;
            boolean workIsNotFinish = true;
            while (workIsNotFinish) {
                try {
                    this.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                workIsNotFinish = false;
            }
        }

        @Override
        public void run() {
            System.out.println("Run");
            long deltaTime = 0;
            long tickTime = 0;
            while (isRunning) {
                Canvas canvas = null;
                try {
                    synchronized (this) {
                        canvas = mHolder.lockCanvas();
                        this.drawBackground(canvas);
                        this.drawTime(canvas, (int) ((mMillisInFuture -= COUNT_DOWN_INTERVAL) / 1000));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mHolder != null) {
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }
                deltaTime = System.currentTimeMillis() - tickTime;
                if (deltaTime < COUNT_DOWN_INTERVAL) {
                    try {
                        Thread.sleep(COUNT_DOWN_INTERVAL - deltaTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tickTime = System.currentTimeMillis();
            }

        }

        private void drawBackground(Canvas canvas) {
            canvas.drawRoundRect(new RectF(0, 0, 100, 100), dp2px(4), dp2px(4), backgroundPaint);
            canvas.drawRoundRect(new RectF(150, 0, 250, 100), dp2px(4), dp2px(4), backgroundPaint);
            canvas.drawRoundRect(new RectF(300, 0, 400, 100), dp2px(4), dp2px(4), backgroundPaint);
        }

        private void drawTime(Canvas canvas, int time) {
            canvas.drawText(time + "", 330, 70, timePaint);
        }
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.mMetrics);
    }

}