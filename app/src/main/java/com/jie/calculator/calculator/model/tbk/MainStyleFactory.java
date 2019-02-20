package com.jie.calculator.calculator.model.tbk;

import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jal.calculator.store.ds.model.tbk.TBKMaterialsResp;
import com.jie.calculator.calculator.util.AppController;

/**
 * Created on 2019/2/18.
 *
 * @author Jie.Wu
 */
public class MainStyleFactory {

    public static TBKMaterialItem newItem(int mode, TBKMaterialsResp resp) {

        if (mode == AppController.MODE_GRID) {
            return new TBKMaterialItem(resp);
        }

        return new TBKMaterialLinearItem(resp);
    }

    public static TBKFavoriteItem newItem(int mode, TBKFavoritesItemResp resp) {

        if (mode == AppController.MODE_GRID) {
            return new TBKFavoriteItem(resp);
        }

        return new TBKFavoriteLinearItem(resp);
    }

}
