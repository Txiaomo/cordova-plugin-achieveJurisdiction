package cordova.plugin.AchieveJurisdiction;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import android.app.ActivityManager;

/**
 * This class echoes a string called from JavaScript.
 */
public class AchieveJurisdiction extends CordovaPlugin {
    private int code = 1;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 1;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("storage")) {
            if (Build.VERSION.SDK_INT >= 24) {
                if (ContextCompat.checkSelfPermission(this.cordova.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.cordova.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
                    code--;
                    if (code < 0) {
                        code = 1;
                        Toast toast = Toast.makeText(cordova.getActivity(), "请在系统设置中开启存储权限", Toast.LENGTH_LONG);
                        showtoast(toast, 4000);
                        callbackContext.success("error");
                    }
                } else {
                    Toast toast = Toast.makeText(cordova.getActivity(), "已有权限", Toast.LENGTH_LONG);
                    callbackContext.success("success");
                }
            } else {
                Toast toast = Toast.makeText(cordova.getActivity(), "不需要申请权限", Toast.LENGTH_LONG);
                callbackContext.success("success");
            }
            return true;
        }else if(action.equals("exitApp")){
            System.exit(0);
            ActivityManager activityManager= (ActivityManager) cordova.getActivity().getSystemService(cordova.getActivity().ACTIVITY_SERVICE);
            activityManager.restartPackage(cordova.getActivity().getPackageName());
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    //设置吐司时长
    public void showtoast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 4000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }
}
