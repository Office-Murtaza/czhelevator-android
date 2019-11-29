package com.kingyon.elevator.mvpbase;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.MyStatusBarUtils;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public abstract class MvpBaseActivity<P extends BasePresenter> extends SwipeBackActivity implements BaseView {
    protected P presenter;
    StateLayout stateLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStatusBarUtils.setStatusBar(this,"#ffffff");
        ActivityUtil.addActivity(this);
        setSwipeBackEnable(true);
        presenter = initPresenter();
        getLifecycle().addObserver(presenter);
    }

    //如果是有刷新或者显示空内容的，则需要先初始化
    public void setStateLayout(){
        stateLayout = (StateLayout) findViewById(R.id.stateLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.removeActivity(this);
        if (presenter != null) {
            presenter.detachView();//在presenter中解绑释放view
            presenter = null;
        }
    }

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();


    /**
     * 重写 getResource 方法，防止系统字体影响
     *
     * @return
     */
    @Override
    public Resources getResources() {//禁止app字体大小跟随系统字体大小调节
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String tips) {

    }

    @Override
    public void showShortToast(String tipsContent) {

    }

    @Override
    public void showLongToast(String tipsContent) {

    }

    @Override
    public void showEmptyContentView() {
        if (stateLayout != null) {
            stateLayout.showEmptyView();
        }
    }

    @Override
    public void showEmptyContentView(String content) {
        if (stateLayout != null) {
            stateLayout.showEmptyView(content);
        }
    }


    @Override
    public void showErrorView() {
        if (stateLayout != null) {
            stateLayout.showErrorView();
        }
    }

    @Override
    public void showErrorView(String content) {
        if (stateLayout != null) {
            stateLayout.showErrorView(content);
        }
    }

    @Override
    public void showContentView() {
        if (stateLayout != null) {
            stateLayout.showContentView();
        }
    }

    @Override
    public void showProgressView() {
        if (stateLayout != null) {
            stateLayout.showProgressView();
        }
    }

    @Override
    public void showProgressView(String content) {
        if (stateLayout != null) {
            stateLayout.showProgressView(content);
        }
    }


    @Override
    public void showProgressDialog(String message, Boolean isCancel) {

    }

    @Override
    public void hideProgressDailog() {

    }
}
