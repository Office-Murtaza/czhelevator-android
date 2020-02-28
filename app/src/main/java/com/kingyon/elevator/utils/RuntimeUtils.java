package com.kingyon.elevator.utils;

import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.entities.ChartSelectParameterEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.GoPlaceAnOrderEntity;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.photopicker.MediaDirectory;

/**
 * Created By SongPeng  on 2019/12/2
 * Email : 1531603384@qq.com
 */
public class RuntimeUtils {


    /**
     * 图表marker点击查看详情
     */
    public static ChartSelectParameterEntity chartSelectParameterEntity;

    public static CooperationInfoNewEntity cooperationInfoNewEntity;

    public static BindAccountEntity selectBindAccountEntity;//选择的提现账号

    public static String payVerCode = "";

    public static String videoPath = "";

    public static String selectVideoPath = "";

    public static String cropVideoPath = "";

    public static MediaDirectory currentMediaDirectory;//当前选择的文件夹

    public static int[] clickPositionAnimation;
    public static String animationImagePath;

    /**
     * 地图或者小区列表界面点击添加时，图片的动画
     */
    public static int[] mapOrListAddPositionAnimation;

    /**
     * 去下单时存放的临时数据
     */
    public static GoPlaceAnOrderEntity goPlaceAnOrderEntity;

    /**
     * 从我的广告选择的广告 跳转到订单界面
     */
    public static ADEntity adEntity;

    public static int businessPlanCount = 0;
    public static int diyPlanCount = 0;
    public static int infomationPlanCount = 0;
    public static NewsEntity newsEntity;//当前点击的新闻

}
