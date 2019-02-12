package com.jal.calculator.store.ds.model.tbk;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKFavoritesResp {

    private long favorites_id;
    private String favorites_title;
    private long type;

    public long getFavorites_id() {
        return favorites_id;
    }

    public void setFavorites_id(long favorites_id) {
        this.favorites_id = favorites_id;
    }

    public String getFavorites_title() {
        return favorites_title;
    }

    public void setFavorites_title(String favorites_title) {
        this.favorites_title = favorites_title;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
