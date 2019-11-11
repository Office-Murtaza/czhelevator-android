package com.kingyon.elevator.utils;

import android.text.TextUtils;

import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;

/**
 * Created by GongLi on 2019/2/13.
 * Email：lc824767150@163.com
 */

public class RoleUtils {
    private static RoleUtils roleUtils;

    private RoleUtils() {
    }

    public static RoleUtils getInstance() {
        if (roleUtils == null) {
            roleUtils = new RoleUtils();
        }
        return roleUtils;
    }

//    public boolean roleBePartner(String userRole) {
//        boolean bePartner = false;
//        if (userRole != null) {
//            String[] roles = userRole.split(",");
//            for (String role : roles) {
//                if (TextUtils.equals(Constants.RoleType.PARTNER, role)) {
//                    bePartner = true;
//                    break;
//                }
//            }
//        }
//        return bePartner;
//    }

    public boolean roleBeTarget(String targetRole, String userRole) {
        boolean beTarget = false;
        if (userRole != null && targetRole != null) {
            String[] roles = userRole.split(",");
            for (String role : roles) {
                if (TextUtils.equals(targetRole, role)) {
                    beTarget = true;
                    break;
                }
            }
        }
        return beTarget;
    }
}
