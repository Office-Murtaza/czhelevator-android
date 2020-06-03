package com.leo.afbaselibrary.nets.converters;

import com.google.gson.Gson;
import com.leo.afbaselibrary.nets.entities.ResultEntity;
import com.leo.afbaselibrary.nets.entities.WxPayEntity;
import com.leo.afbaselibrary.nets.exceptions.PayResultException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * created by arvin on 16/10/24 17:24
 * email：1035407623@qq.com
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type adapter;

    public GsonResponseBodyConverter(Gson gson, Type adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            Logger.i(response);
            Logger.json(response);
            ResultEntity resultModel = gson.fromJson(response, ResultEntity.class);
            if (resultModel.getStatus()==200) {
                if (resultModel.getContent() != null) {
                    return gson.fromJson(resultModel.getContent(), adapter);
                }
                return null;
            } else if (resultModel.getStatus() == 8001 || resultModel.getStatus() == 8002) {
                throw new PayResultException(resultModel.getStatus(), resultModel.getMessage(), gson.fromJson(resultModel.getContent(), WxPayEntity.class));
            }else {
                throw new ResultException(resultModel.getStatus(), resultModel.getMessage());
            }
        } finally {
            value.close();
        }
    }
}
