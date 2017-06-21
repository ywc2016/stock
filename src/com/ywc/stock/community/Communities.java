package com.ywc.stock.community;

import com.ywc.stock.entity.Stock;
import com.ywc.stock.entity.StockList;
import com.ywc.stock.util.Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ywcrm on 2017/6/16.
 */
public class Communities extends ArrayList<Community> {
    /**
     * 社团的数量
     */
    private int numOfCommunities;
    private StockList stockList = Utils.readCsvToStockList();

    public Communities() {

    }

    /**
     * 利用社团文件建立
     *
     * @param communityFile
     */
    public Communities(File communityFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(communityFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            bufferedReader.readLine();
            for (; ; ) {
                String line = bufferedReader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                String[] strings = line.split(",");
                this.addStock(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addStock(int stockId, int CommunityId) {
        Community community = findCommunityByCommunityId(CommunityId);
        if (community != null) {
            Stock stock = stockList.findStockById(stockId);
            if (stock != null) {
                community.add(stock);
            } else {
                System.out.println("id为" + stockId + "的股票未找到.");
            }
        } else {
            Community community1 = new Community(CommunityId);
            Stock stock = stockList.findStockById(stockId);
            if (stock != null) {
                community1.add(stock);
                this.add(community1);
            } else {
                System.out.println("id为" + stockId + "的股票未找到.");
            }

        }

    }

    private Community findCommunityByCommunityId(int communityId) {
        for (Community community : this) {
            if (community.getCommunityId() == communityId) {
                return community;
            }
        }
        return null;
    }
}
