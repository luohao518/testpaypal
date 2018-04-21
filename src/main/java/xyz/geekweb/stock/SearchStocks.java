package xyz.geekweb.stock;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.geekweb.stock.savesinastockdata.RealTimeData;
import xyz.geekweb.stock.savesinastockdata.RealTimeDataPOJO;

import java.util.Arrays;
import java.util.List;

public class SearchStocks {

    /**
     * 国债逆回购
     */
    final static String[] REVERSE_BONDS = {"sh204001", "sh204002", "sh204003", "sh204004", "sh204007", "sh204014"};
    final static double MIN_REVERSE_BONDS_VALUE = 4.0;
    /**
     * 货币基金
     */
    final static String[] MONETARY_FUNDS = {"sh511990", "sh511660", "sh511810", "sh511690", "sh511900"};
    final static double MAX_MONETARY_FUNDS_VALUE = 99.990;
    /**
     * 股票
     */
    final static String[] STOCKS = {"sh600185", "sh600448", "sh601098", "sz000066", "sz002570", "sh601828", "sh601928", "sh601369", "sz000417"};
    final static double MAX_STOCKS_PERCENT = 3.0;
    /**
     * 分级基金 fd
     */
    final static String[] FJ_FUNDS = {"of150018", "of150171", "of150181", "of150227", "of150200", "of150022"};
    private Logger logger = LoggerFactory.getLogger(SearchStocks.class);

    public void doALL() {

        String strMoney = processMoney();

        String strStocks = processStocks();


        logger.warn(strMoney);
        logger.warn(strStocks);
//        logger.warn(ToStringBuilder.reflectionToString(getFjFunds()));
        getFjFunds().forEach(item -> {
            logger.warn(Arrays.deepToString(item));
        });


    }

    /**
     * 现金管理(货币基金，国债逆回购)
     *
     * @return
     */
    public String processMoney() {

        final List<RealTimeDataPOJO> realTimeDataPOJOS = RealTimeData.getRealTimeDataObjects(ArrayUtils.addAll(REVERSE_BONDS, MONETARY_FUNDS));

        StringBuilder sb = new StringBuilder("\n");

        //国债逆回购判断
        for (int i = 0; i < REVERSE_BONDS.length; i++) {

            RealTimeDataPOJO realTimeDataPOJO = realTimeDataPOJOS.get(i);
            if (realTimeDataPOJO.getNow() > MIN_REVERSE_BONDS_VALUE) {
                //国债逆回购触发价格高点（对标：浦发天天）
                sb.append(String.format("购买国债逆回购:%5s 当前价[%2.2f] 买入价[%2.2f]%n", realTimeDataPOJO.getName(), realTimeDataPOJO.getNow(), realTimeDataPOJO.getBuy1Pricae()));
            }
        }

        //货币基金判断
        realTimeDataPOJOS.forEach(item -> {
            if (item.getNow() < MAX_MONETARY_FUNDS_VALUE) {
                //货币基金触发价格低点
                if (!item.getName().startsWith("GC")) {
                    sb.append(String.format("购买货币基金:%s 当前价[%6.2f] 卖出价[%6.2f] 卖量[%5.0f]%n", item.getFullCode(), item.getNow(), item.getSell1Pricae(), item.getSell1Num()));
                }
            }
        });

        return sb.toString();
    }

    /**
     * 股票
     *
     * @return
     */
    public String processStocks() {

        final List<RealTimeDataPOJO> realTimeDataPOJOS = RealTimeData.getRealTimeDataObjects(STOCKS);

        StringBuilder sb = new StringBuilder("\n");

        realTimeDataPOJOS.forEach(item -> {

            double percent = ((item.getNow() - item.getClose()) / item.getClose()) * 100;
            if (Math.abs(percent) > MAX_STOCKS_PERCENT) {
                //价格突破设定值
                sb.append(String.format("%-6s %8s 幅度[%6.2f%%] 当前价[%6.2f] 卖出价[%6.2f] 买入价[%6.2f]\n", item.getName(), item.getFullCode(), percent, item.getNow(), item.getSell1Pricae(), item.getBuy1Pricae()));
            }
        });

        return sb.toString();
    }

    /**
     * 分级基金
     *
     * @return
     */
    public List<String[]> getFjFunds() {

        return RealTimeData.getRealTimeDataObjectsForFjFunds(FJ_FUNDS);

    }

}
