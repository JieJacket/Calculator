package com.jal.calculator.store.ds.model.tbk;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKFavoritesResp {

    private int favorites_id;
    private String favorites_title;
    private int type;

    public int getFavorites_id() {
        return favorites_id;
    }

    public void setFavorites_id(int favorites_id) {
        this.favorites_id = favorites_id;
    }

    public String getFavorites_title() {
        return favorites_title;
    }

    public void setFavorites_title(String favorites_title) {
        this.favorites_title = favorites_title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
