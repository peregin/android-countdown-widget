package peregin.android.countdown;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import peregin.android.util.AppInfo;
import peregin.android.util.Io;

import java.io.IOException;
import java.io.InputStream;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.info);

        final WebView web = (WebView) findViewById(R.id.wv_info);
        final String version = AppInfo.getVersionName(this);
        Log.i(getClass().getSimpleName(), "application version is "+version);

        // replace the version metainfo in the html asset with the actual version extracted
        final String assetName = getString(R.string.info_page_key);
        String content;
        try {
            InputStream in = getAssets().open(assetName);
            content = Io.readString(in).toString();
            content = String.format(content, version);
            Io.close(in);
        } catch (IOException any) {
            Log.e(getClass().getSimpleName(), "unable to read asset "+assetName, any);
            content = version;
        }
        web.loadData(content, "text/html", null);
        // web.loadUrl("file:///android_asset/" + getString(R.string.info_page_key));

        final Button btnDismiss = (Button) findViewById(R.id.buttonDismiss);
        btnDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
