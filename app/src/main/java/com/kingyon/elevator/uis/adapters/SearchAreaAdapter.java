package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.entities.NormalParamEntity;

import java.util.List;

/**
 * Created by GongLi on 2019/1/28.
 * Email：lc824767150@163.com
 */

public class SearchAreaAdapter extends SearchCellTypeAdapter {
    public SearchAreaAdapter(Context context, int resId) {
        super(context, resId);
    }

    @Override
    public void onItemClick(View view, int position, NormalParamEntity entity, BaseAdapterWithHF<NormalParamEntity> baseAdaper) {
        if (entity != null) {
            if (TextUtils.isEmpty(entity.getType())) {
                clearSelected();
            } else {
                List<NormalParamEntity> datas = getDatas();

                for (NormalParamEntity item : datas) {
                    if (!TextUtils.isEmpty(item.getType())) {
                        if (item == entity) {
                            item.setChoosed(!entity.isChoosed());
                        } else {
                            item.setChoosed(false);
                        }
                    }
                }

                boolean hasChoose = false;
                for (NormalParamEntity item : datas) {
                    if (!TextUtils.isEmpty(item.getType()) && item.isChoosed()) {
                        hasChoose = true;
                        break;
                    }
                }
                setNoLimitChoose(!hasChoose);
            }
            notifyDataSetChanged();
        }
    }
}
