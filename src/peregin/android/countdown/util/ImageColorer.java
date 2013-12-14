package peregin.android.countdown.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ImageColorer {

    private final Bitmap bitmap;
    private final int width;
    private final int height;

    /**
     * HSB colorer
     * 
     * @param src the source or reference image
     * @param hue between [-180, 180]
     * @param alpha between [0, 1] 0 is completely transparent, 1 is opaque
     * @return the modified image
     */
    static public Bitmap adjust(Bitmap src, int hue, float alpha) {
        ImageColorer ic = new ImageColorer(src);
        ic.adjustHSB(hue, 0, 0, alpha);
        return ic.getBitmap();
    }

    public ImageColorer(Bitmap src) {
        bitmap = src.copy(Bitmap.Config.ARGB_8888, true);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Adjusts the HSB values of the image.
     * 
     * @param hue [-180, 180]
     * @param saturation [-100, 100]
     * @param brightness [-100, 100]
     * @param alphaFactor [0, 1]
     */
    public void adjustHSB(int hue, int saturation, int brightness, float alphaFactor) {
        if (hue > 180) {
            Log.d(getClass().getSimpleName(), "hue too big " + hue);
            hue = 180;
        } else if (hue < -180) {
            Log.d(getClass().getSimpleName(), "hue too low " + hue);
            hue = -180;
        }
        if (saturation > 100) {
            Log.d(getClass().getSimpleName(), "saturation too big " + saturation);
            saturation = 100;
        } else if (saturation < -100) {
            Log.d(getClass().getSimpleName(), "saturation too low " + saturation);
            saturation = -100;
        }
        if (brightness > 100) {
            Log.d(getClass().getSimpleName(), "brightness too big " + brightness);
            brightness = 100;
        } else if (brightness < -100) {
            Log.d(getClass().getSimpleName(), "brightness too low " + brightness);
            brightness = -100;
        }

        float[] hsb = new float[3];
        float[] adjustedHsv = new float[3];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = bitmap.getPixel(x, y);
                if (pixel == 0) {
                    continue;
                }

                Color.colorToHSV(pixel, hsb);

                // adjust hue
                float dh = hsb[0];// * 360;
                dh += hue;
                dh %= 360;

                // adjust brightness
                float db = hsb[2];
                float f = 0;
                if (brightness > 0) {
                    f = (1 - db) * brightness / 100;
                } else if (brightness < 0) {
                    f = db * brightness / 100;
                }
                db += f;

                // adjust saturation
                float ds = hsb[1];
                f = 0;
                if (saturation > 0) {
                    f = (1 - ds) * saturation / 100;
                } else if (saturation < 0) {
                    f = ds * saturation / 100;
                }
                ds += f;

                if (alphaFactor > 1) {
                    alphaFactor = 1;
                } else if (alphaFactor < 0) {
                    alphaFactor = 0;
                }
                int a = Color.alpha(pixel);

                // convert it back to ARGB
                adjustedHsv[0] = dh;
                adjustedHsv[1] = ds;
                adjustedHsv[2] = db;
                int argb = Color.HSVToColor((int)(a * alphaFactor), adjustedHsv);

                bitmap.setPixel(x, y, argb);
            }
        }
    }
}
