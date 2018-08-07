package com.baidu_map.lhq.permission_demo.ui.activity.base;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.baidu_map.lhq.permission_demo.ui.activity.ano.QueryPermission;
import java.util.ArrayList;
import java.util.List;

public abstract class PermissionActivity extends BaseActivity{


    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    protected void requestPermission(){
        Class aClass = this.getClass();
        QueryPermission annotation = (QueryPermission) aClass.getAnnotation(QueryPermission.class);
        if (annotation == null){
            return;
        }
        String[] permissions = annotation.permissions();
        List<String> permissionArray = new ArrayList<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this,permission);
            if (result!= PackageManager.PERMISSION_GRANTED){
                permissionArray.add(permission);
            }
        }

        if (permissionArray.size() <= 0){
            return;
        }

        permissions = new String[permissionArray.size()];
        permissionArray.toArray(permissions);
        ActivityCompat.requestPermissions(this,permissions,1);
    }
}
