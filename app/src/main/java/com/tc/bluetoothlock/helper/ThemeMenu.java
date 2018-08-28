package com.tc.bluetoothlock.helper;

/**
 * 主题
 * Created by mac on 2017/12/6.
 */

public enum ThemeMenu {

    NORMAL_THEME(0),
    YELLOW_THEME(1),
    THREE_THEME(2),
    FOUR_THEME(3),
    FIVE_THEME(4),
    SIX_THEME(5)
    ;



    private int name;

    ThemeMenu(int theme){
        this.name = theme;
    }

    public int getValue(){
        return name;
    }


    public static ThemeMenu valueOf(int value){

        switch (value){
            case 0:
                return NORMAL_THEME;
            case 1:
                return YELLOW_THEME;
            case 2:
                return THREE_THEME;
            case 3:
                return FOUR_THEME;
            case 4:
                return FIVE_THEME;
            case 5:
                return SIX_THEME;
        }

        return NORMAL_THEME;
    }



}
