package peregin.android.countdown;

import java.util.ArrayList;
import java.util.List;

import peregin.android.countdown.util.ImageColorer;
import peregin.android.countdown.util.UnitConverter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class BackgroundImageAdapter extends BaseAdapter {

    static public final int DEFAULT_BACKGROUND_INDEX = 5;
    static final int HUE_FACTOR_STEP = 30;

    private final Context context;
    private final List<Bitmap> items = new ArrayList<Bitmap>();

    public BackgroundImageAdapter(Context context) {
        this.context = context;

        // create a set of colored backgrounds programmatically
        Bitmap back = BitmapFactory.decodeResource(context.getResources(), R.drawable.back1x1);

        // add 10 backgrounds
        for (int i = -150; i < 150; i += HUE_FACTOR_STEP) {
            Bitmap b = ImageColorer.adjust(back, i, 1f);
            items.add(b);
        }
    }

    static public Bitmap createColoredBackground(Context context, int backgroundIndex, float alpha, int resId) {
        Bitmap back = BitmapFactory.decodeResource(context.getResources(), resId);
        int hueFactor = -150 + backgroundIndex * HUE_FACTOR_STEP;
        return ImageColorer.adjust(back, hueFactor, alpha);
    }

    public int dip2pixel(int dip) {
        return UnitConverter.dip2pixel(context, dip);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);

        int sizePx = dip2pixel(60);
        imageView.setImageBitmap(items.get(position));
        imageView.setLayoutParams(new Gallery.LayoutParams(sizePx, sizePx));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return imageView;
    }
}
