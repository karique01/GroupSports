package pe.edu.karique.groupsports.widget;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by karique on 12/07/2018.
 */

public class PercentageCropImageView extends android.support.v7.widget.AppCompatImageView{

    private Float mCropYCenterOffsetPct;
    private Float mCropXCenterOffsetPct;

    public PercentageCropImageView(Context context) {
        super(context);
    }

    public PercentageCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentageCropImageView(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
    }

    public float getCropYCenterOffsetPct() {
        return mCropYCenterOffsetPct;
    }

    public void setCropYCenterOffsetPct(float cropYCenterOffsetPct) {
        if (cropYCenterOffsetPct > 1.0) {
            throw new IllegalArgumentException("Value too large: Must be <= 1.0");
        }
        this.mCropYCenterOffsetPct = cropYCenterOffsetPct;
    }

    public float getCropXCenterOffsetPct() {
        return mCropXCenterOffsetPct;
    }

    public void setCropXCenterOffsetPct(float cropXCenterOffsetPct) {
        if (cropXCenterOffsetPct > 1.0) {
            throw new IllegalArgumentException("Value too large: Must be <= 1.0");
        }
        this.mCropXCenterOffsetPct = cropXCenterOffsetPct;
    }

    private void myConfigureBounds() {
        if (this.getScaleType() == ScaleType.MATRIX) {
            Drawable d = this.getDrawable();
            if (d != null) {
                int dwidth = d.getIntrinsicWidth();
                int dheight = d.getIntrinsicHeight();

                Matrix m = new Matrix();

                int vwidth = getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                int vheight = getHeight() - this.getPaddingTop() - this.getPaddingBottom();

                float scale;
                float dx = 0, dy = 0;

                if (dwidth * vheight > vwidth * dheight) {
                    float cropXCenterOffsetPct = mCropXCenterOffsetPct != null ?
                            mCropXCenterOffsetPct.floatValue() : 0.5f;
                    scale = (float) vheight / (float) dheight;
                    dx = (vwidth - dwidth * scale) * cropXCenterOffsetPct;
                } else {
                    float cropYCenterOffsetPct = mCropYCenterOffsetPct != null ?
                            mCropYCenterOffsetPct.floatValue() : 0f;

                    scale = (float) vwidth / (float) dwidth;
                    dy = (vheight - dheight * scale) * cropYCenterOffsetPct;
                }

                m.setScale(scale, scale);
                m.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

                this.setImageMatrix(m);
            }
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        this.myConfigureBounds();
        return changed;
    }
    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
        this.myConfigureBounds();
    }
    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        this.myConfigureBounds();
    }

    public void redraw() {
        Drawable d = this.getDrawable();

        if (d != null) {
            // Force toggle to recalculate our bounds
            this.setImageDrawable(null);
            this.setImageDrawable(d);
        }
    }
}