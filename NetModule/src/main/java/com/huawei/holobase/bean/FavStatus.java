package com.huawei.holobase.bean;

/**
 * 创建时间：2020-07-31
 * 创建人：dongdz
 * 功能描述：
 */
public class FavStatus {

    /**
     * 0:未收藏 1:收藏
     */
    private int favorite_status;

    public int getFavorite_status() {
        return favorite_status;
    }

    public void setFavorite_status(int favorite_status) {
        this.favorite_status = favorite_status;
    }

    public boolean isFav() {
        return favorite_status == 1;
    }
}
