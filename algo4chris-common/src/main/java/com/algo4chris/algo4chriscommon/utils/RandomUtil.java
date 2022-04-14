package com.algo4chris.algo4chriscommon.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author chris
 * @Description: 隨機生成隨機數，字母加數字
 */
public class RandomUtil {

    private static char[] A_z = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static Random r = new Random();

    public static String getDeFaultRandom() {
        int length = 10;
        String rand;
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= length; i++)//6次执行输出6个不同字符
        {
            //判断产生的随机数是0还是1，是0进入if语句用于输出数字，是1进入else用于输出字符
            int rd = Math.random() >= 0.5 ? 1 : 0;
            if (rd == 0) {
                rand = r.nextInt(9) + 1 + ""; //产生1-9数字
            } else {
                //'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
                int sub = r.nextInt(A_z.length);
                rand = A_z[sub] + "";//产生A——z字符
            }
            result.append(rand);
        }
        return result.toString();
    }

    public static String getRandom(int length) {
        String rand;
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= length; i++)//6次执行输出6个不同字符
        {
            //判断产生的随机数是0还是1，是0进入if语句用于输出数字，是1进入else用于输出字符
            int rd = Math.random() >= 0.5 ? 1 : 0;
            if (rd == 0) {
                rand = r.nextInt(9) + 1 + ""; //产生1-9数字
            } else {
                //'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
                int sub = r.nextInt(A_z.length);
                rand = A_z[sub] + "";//产生A——z字符
            }
            result.append(rand);
        }
        return result.toString();
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

}
