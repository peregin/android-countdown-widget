package peregin.android.countdown;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageReel extends Gallery {

    private Camera camera = new Camera();

    /**
     * Tells the center of the reel
     */
    private int coverflowCenter;

    public ImageReel(Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
    }

    public ImageReel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
    }

    public ImageReel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
    }

    private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see #setStaticTransformationsEnabled(boolean)
     */
    protected boolean getChildStaticTransformation(View child, Transformation t) {

        final int childCenter = getCenterOfView(child);
        int halfRange = 20;
        if (childCenter > coverflowCenter - halfRange && childCenter < coverflowCenter + halfRange) {
            t.clear();
            t.setTransformationType(Transformation.TYPE_MATRIX);
            int diff = Math.abs(coverflowCenter - childCenter);
            if (diff > halfRange) {
                diff = halfRange;
            }
            float zoomFactor = 50.0f * (halfRange - diff) / halfRange;
            zoomImageBitmap((ImageView) child, t, -zoomFactor);
        }

        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        coverflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void zoomImageBitmap(ImageView child, Transformation t, float zoomFactor) {
        camera.save();
        final Matrix imageMatrix = t.getMatrix();
        final int imageHeight = child.getLayoutParams().height;
        final int imageWidth = child.getLayoutParams().width;

        camera.translate(0.0f, 0.0f, zoomFactor);

        camera.getMatrix(imageMatrix);
        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));

        camera.restore();
    }
}
