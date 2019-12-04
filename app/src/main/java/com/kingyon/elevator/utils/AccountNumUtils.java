package com.kingyon.elevator.utils;

import com.qiniu.android.utils.StringUtils;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public class AccountNumUtils {


    public static String hideBankCardNum(String cardNum) {
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cardNum.length(); i++) {
            if (i < 4 || i >= (cardNum.length() - 4)) {
                sb.append(cardNum.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }
        return sb.toString();
    }


    public static String hidePhoneNum(String accout) {
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < accout.length(); i++) {
            if (i < 3 || i >= 7) {
                sb.append(accout.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }
        return sb.toString();
    }


    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return 26
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

}
