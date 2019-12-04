package com.kingyon.elevator.interfaces;

import android.view.View;

/**
 * Created By SongPeng  on 2019/12/2
 * Email : 1531603384@qq.com
 * 防止快速点击
 */
public abstract class NoQuickClickListener  implements View.OnClickListener {
    private static final long DELAYED_TIME = 1000;
    private long lastTime = 0;

    public abstract void forbidClick(View view);

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis()-lastTime > DELAYED_TIME){
            lastTime = System.currentTimeMillis();
            forbidClick(v);
        }
    }
}
