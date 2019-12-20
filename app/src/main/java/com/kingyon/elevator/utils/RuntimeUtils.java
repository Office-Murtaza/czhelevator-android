package com.kingyon.elevator.utils;

import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.entities.ChartSelectParameterEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.GoPlaceAnOrderEntity;
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
     * 去下单时存放的临时数据
     */
    public static GoPlaceAnOrderEntity goPlaceAnOrderEntity;

}
