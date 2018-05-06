package com.truck.food;

import com.truck.food.db.Dish;

import java.util.List;

/**
 * Created by Dmitry on 06.05.2018.
 */

public class SugarHelper {
    public static int getDishCount() {
        List<Dish> dishes = Dish.listAll(Dish.class);
        int c = 0;
        for (Dish dish : dishes) {
            c += dish.getCount();
        }
        return c;
    }
    public static int getTotal(){
        List<Dish> dishes = Dish.listAll(Dish.class);
        int t = 0;
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            t += Double.parseDouble(dish.getPrice())*dish.getCount();
        }
        return t;
    }
}
