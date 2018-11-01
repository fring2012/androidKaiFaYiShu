package com.baidu_map.lhq.permission_demo.ui.gesturedetector;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.GestureDetector;
import android.view.MotionEvent;

import static android.view.GestureDetector.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ContextClickListener implements OnContextClickListener {
    /**
     * 当鼠标/触摸板，右键点击时候的回调
     * @param e
     * @return
     */
    @Override
    public boolean onContextClick(MotionEvent e) {
        return false;
    }
}
