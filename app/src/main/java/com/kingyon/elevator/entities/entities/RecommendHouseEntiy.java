package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/5/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class RecommendHouseEntiy {
    
        /**
         * id : 100084
         * name : 伊思特
         * address : 贵州省贵阳市白云区沙文镇科创南路
         * numberElevator : 2
         * numberUnit : 1
         * longitude : 106.672424
         * latitude : 26.725725
         * stayIn : 2019-10-20
         * type : OFFICE_BUILD
         * numberTraffic : 1000
         * priceBusiness : 10
         * priceDiy : 200
         * priceText : 0
         * urlCover : http://cdn.tlwgz.com/Fs7OAid31Yt7WGULaXNsDae78fy-
         * distanceM : 37291
         * urlHousing : http://cdn.tlwgz.com/Fi6_CTKTFxLo4rUxUq6O6AbehYVc,http://cdn.tlwgz.com/Fn-CZ1kfZ5AlEG0Hkufo791CR9h8,http://cdn.tlwgz.com/FohRD3DE0VPdUILit0z31pBrXg3B
         * urlElevator : 
         */
        public int id;
        public String name;
        public String address;
        public int numberElevator;
        public int numberUnit;
        public double longitude;
        public double latitude;
        public String stayIn;
        public String type;
        public int numberTraffic;
        public double priceBusiness;
        public double priceDiy;
        public double priceText;
        public String urlCover;
        public int distanceM;
        public String urlHousing;
        public String urlElevator;

        @Override
        public String toString() {
                return "RecommendHouseEntiy{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", address='" + address + '\'' +
                        ", numberElevator=" + numberElevator +
                        ", numberUnit=" + numberUnit +
                        ", longitude=" + longitude +
                        ", latitude=" + latitude +
                        ", stayIn='" + stayIn + '\'' +
                        ", type='" + type + '\'' +
                        ", numberTraffic=" + numberTraffic +
                        ", priceBusiness=" + priceBusiness +
                        ", priceDiy=" + priceDiy +
                        ", priceText=" + priceText +
                        ", urlCover='" + urlCover + '\'' +
                        ", distanceM=" + distanceM +
                        ", urlHousing='" + urlHousing + '\'' +
                        ", urlElevator='" + urlElevator + '\'' +
                        '}';
        }
}
