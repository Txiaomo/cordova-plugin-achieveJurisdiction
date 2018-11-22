package cordova.plugin.achievejurisdiction;


import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import cordova.plugin.achievejurisdiction.FileProvider;
import android.widget.Toast;
import android.app.ActivityManager;
import android.util.Log;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import yzt.jzt.BuildConfig;

public class AchieveJurisdiction extends CordovaPlugin {
    private int storageCode = 1;
    private int IUSourceCode = 1;
    private String appName="";
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 1;
    private final static int INSTALL_PACKAGES_REQUESTCODE = 0x12;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("storage")) {
            if (Build.VERSION.SDK_INT >= 24) {
                if (ContextCompat.checkSelfPermission(this.cordova.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.cordova.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
                    storageCode--;
                    if (storageCode < 0) {
                        storageCode = 1;
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
        }else if(action.equals("installApp")) {
            appName=args.getString(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if (isHasInstallPermissionWith(cordova.getActivity())){
                    appInstall();
                }else{
                    startInstallPermissionSettingActivity();
                }
            }else{
                appInstall();
            }
            return true;
        }else if(action.equals("isPermission")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if (!isHasInstallPermissionWith(cordova.getActivity())){
                    callbackContext.error("noPermission");
                }else{
                    callbackContext.error("havePermission");
                }
            }else{
                callbackContext.error("havePermission");
            }
            return true;
        }
        return false;
    }

    public void appInstall(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(""),appName);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                //7.0
                Uri contentUri = FileProvider.getUriForFile(cordova.getActivity(), BuildConfig.APPLICATION_ID+".achievejurisdiction.provider", apkFile);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            }else{
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (cordova.getActivity().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                cordova.getActivity().startActivity(intent);
            }else{
                Log.d("TAG","安装intent不可跳转！");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWith(Context context){
        return context.getPackageManager().canRequestPackageInstalls();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        String result=data.getExtras().toString();
        if (resultCode==CordovaActivity.RESULT_OK&&requestCode==1){
            appInstall();
        }
        Log.i("TAG",result);
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI=Uri.parse("package:"+cordova.getActivity().getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
        cordova.getActivity().startActivityForResult(intent,1);
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
