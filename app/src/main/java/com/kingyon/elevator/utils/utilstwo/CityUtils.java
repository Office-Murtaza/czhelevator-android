package com.kingyon.elevator.utils.utilstwo;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Created By Admin  on 2020/5/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CityUtils {

    public static  void getCityCode(Context context,String city,CityCode cityCode){
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open("gd_district.json"),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
            //直接传入JSONObject来构造一个实例
            JSONArray jsonArray = new JSONArray(builder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject.optJSONArray("districts");
                for (int c= 0;c<jsonArray1.length();c++){
                    JSONObject object1 = jsonArray1.optJSONObject(c);
                    if (object1.optString("name").equals(city)){
                        cityCode.cityCode(object1.getInt("adcode"));
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
            e.printStackTrace();
        }
    }

   public interface CityCode{
        void cityCode(int cityCode);
    }
}
