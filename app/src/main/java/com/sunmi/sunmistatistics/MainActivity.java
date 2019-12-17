package com.sunmi.sunmistatistics;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sunmi.sunmistatisticslib.SMStaticsMaster;
import com.sunmi.sunmistatisticslib.bean.SMStaticsInfo;


/**
 * 1.clean整个项目
 * 2.build sunmmilib release 库
 * 3. publishing 里面生成 releasepublicat 和 tomavenlocal
 * 4。 执行./gradlew bintrayUpload -PbintrayUser=SunmiUi -PbintrayKey=ef38856d2dd40dffff4a1eb9872f3b4cceae301a -PdryRun=false
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();

        int a =5;
        if(a>1){
            Log.d(TAG, "onCreate: ------>1111111111");
        }else if(a>2){
            Log.d(TAG, "onCreate: ------>2222222222");
        }else{
            Log.d(TAG, "onCreate: ------>33333333");
        }


    }

    private void checkPermissions(){
        boolean hasPermission = checkSelfPermission(Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            requestPermissions(new String[]{Manifest.permission_group.STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private int num=0;
    public void testEventReport(View view){
        Log.d(TAG, "event report test");
        SMStaticsMaster.getInstance().onEvent("event_test",String.valueOf(num++));
    }
}
