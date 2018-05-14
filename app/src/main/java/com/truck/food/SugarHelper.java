package com.truck.food;

import com.truck.food.db.DishDB;

import java.util.List;

/**
 * Created by Dmitry on 06.05.2018.
 */

public class SugarHelper {
    public static int getDishCount() {
        List<DishDB> dishDBs = DishDB.listAll(DishDB.class);
        int c = 0;
        if(dishDBs.size()>0) {

            for (DishDB dishDB : dishDBs) {
                c += dishDB.getCount();
            }
        }
        return c;
    }
    public static int getTotal(){
        List<DishDB> dishDBs = DishDB.listAll(DishDB.class);
        int t = 0;
        for (int i = 0; i < dishDBs.size(); i++) {
            DishDB dishDB = dishDBs.get(i);
            t += Double.parseDouble(dishDB.getPrice())* dishDB.getCount();
        }
        return t;
    }
}
