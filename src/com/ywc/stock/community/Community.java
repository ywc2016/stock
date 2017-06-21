package com.ywc.stock.community;

import com.ywc.stock.entity.Stock;

import java.util.ArrayList;

/**
 * Created by ywcrm on 2017/6/16.
 */
public class Community extends ArrayList<Stock> {
    /**
     * 社团的标号
     */
    private int communityId;

    public Community() {

    }

    public Community(int communityId) {
        this.communityId = communityId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }


}
