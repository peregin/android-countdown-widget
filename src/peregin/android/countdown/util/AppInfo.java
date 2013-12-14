package peregin.android.countdown.util;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * Convenience class to extract application related information from the package
 * or meta information.
 * @author levi
 */
public class AppInfo {
  
  static final String NO_VERSION_NAME = "none";

  static public String getVersionName(Activity app) {    
    PackageManager pm = app.getPackageManager();
    String pname = app.getPackageName();

    try {
      PackageInfo pinfo = pm.getPackageInfo(pname, 0);
      String vname = pinfo.versionName;
      if (vname == null) {
        return NO_VERSION_NAME;
      } 
      return vname;
    } catch (NameNotFoundException any) {
      Log.w(AppInfo.class.getName(), any);
      return NO_VERSION_NAME;
    }
  }
}
