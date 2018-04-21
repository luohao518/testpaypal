package xyz.geekweb.stock;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class HolidayUtil {

    /**
     * 查询是否为节假日API接口  工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2
     */
    private static String URL = "http://tool.bitefu.net/jiari?d=%s";

    private static int[] STOCK_TIMES = new int[]{915, 1130, 1300, 1500};

    public static boolean isStockTime(String hhmm) {

        if (StringUtils.isEmpty(hhmm)) {
            throw new java.lang.IllegalArgumentException();
        }

        int intHHMM = Integer.parseInt(hhmm);
        if ((intHHMM >= STOCK_TIMES[0] && intHHMM <= STOCK_TIMES[1]) || (intHHMM >= STOCK_TIMES[2] && intHHMM <= STOCK_TIMES[3])) {
            return true;
        } else {
            return false;
        }


    }

    public static boolean isHoliday(String strDate) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.format(URL, strDate))
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }

        String result = response.body().string();
        System.out.println(result);
        System.out.println(result);
        return result.equals("0") ? false : true;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(isHoliday("20180501"));
        System.out.println(isHoliday("20180420"));
        System.out.println(isHoliday("20180401"));

        System.out.println(isStockTime("0914"));
        System.out.println(isStockTime("0939"));
        System.out.println(isStockTime("1131"));
        System.out.println(isStockTime("1200"));
        System.out.println(isStockTime("1501"));
        System.out.println(isStockTime("1401"));
    }
}
