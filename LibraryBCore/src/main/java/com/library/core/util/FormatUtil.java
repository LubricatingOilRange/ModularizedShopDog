package com.library.core.util;

//  Created by ruibing.han on 2018/5/15.

//数据进行处理 格式转换等
public class FormatUtil {

    /**
     * 将数据用 ，格式化
     */
    public static String getFormatComma(Object object) {
        if (object == null) {
            return "0";
        }
        try {
            String content = String.valueOf(object);
            return java.text.NumberFormat.getNumberInstance().format(Double.parseDouble(content));
        } catch (Exception e) {
            LogUtil.e("Incorrect data format ");
            return "0";
        }
    }

    /**
     * 将 ， 转换成  .
     */
    public static String getCommaToPoint(Object object) {
        if (object == null) {
            return "0";
        }
        String content = String.valueOf(object);
        try {
            Double count = Double.valueOf(content);
            java.text.NumberFormat num = java.text.NumberFormat.getNumberInstance();
            String data = num.format(count);
            return data.replace(",", ".");
        } catch (Exception e) {
            LogUtil.e("Incorrect data format ");
            return "0";
        }
    }
}
