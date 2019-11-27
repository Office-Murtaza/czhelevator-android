package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.mvpbase.BaseView;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public interface FragmentContainerView extends BaseView {

    void loadingComplete(int stateCode);


    void  goPartnerDetailsInfo(boolean authed, CooperationIdentityEntity identity, CooperationInfoEntity info);
}
