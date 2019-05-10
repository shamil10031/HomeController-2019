package shomazzapp.com.homecontorl.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import shomazzapp.com.homecontorl.R;

public class HoleView extends View {

    private Paint mTransparentPaint;
    private Paint mSemiBlackPaint;
    private Path mPath = new Path();
    private int color;

    private static float RADIUS;

    public HoleView(Context context) {
        super(context);
        initPaints();
    }

    public HoleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public HoleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        mTransparentPaint = new Paint();
        mTransparentPaint.setColor(Color.TRANSPARENT);
        mTransparentPaint.setStrokeWidth(10);

        mSemiBlackPaint = new Paint();
        mSemiBlackPaint.setColor(Color.TRANSPARENT);
        mSemiBlackPaint.setStrokeWidth(10);

        float dip = getResources().getInteger(R.integer.reg_camera_diameter)/2;
        Resources r = getResources();
        RADIUS = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip,
                r.getDisplayMetrics());

        color  = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        color = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        mPath.addCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, RADIUS, Path.Direction.CW);
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, RADIUS, mTransparentPaint);

        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(color);
    }

}
