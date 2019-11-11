package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.entities.NormalParamEntity;

import java.util.List;

/**
 * Created by GongLi on 2018/12/28.
 * Email：lc824767150@163.com
 */

public class SearchDistanceAdapter extends SearchCellTypeAdapter {
    public SearchDistanceAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    @Override
    public void onItemClick(View view, int position, NormalParamEntity entity, BaseAdapterWithHF<NormalParamEntity> baseAdaper) {
        if (entity != null) {
            if (TextUtils.isEmpty(entity.getType())) {
                clearSelected();
            } else {
                List<NormalParamEntity> datas = baseAdaper.getDatas();

                for (NormalParamEntity item : datas) {
                    if (item == entity) {
                        item.setChoosed(!item.isChoosed());
                    } else {
                        item.setChoosed(false);
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

    public void chooseDiatance(String distance) {
        if (TextUtils.isEmpty(distance)) {
            clearSelected();
        } else {
            List<NormalParamEntity> datas = getDatas();

            for (NormalParamEntity item : datas) {
                item.setChoosed(TextUtils.equals(distance, item.getType()));
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
    }
}
