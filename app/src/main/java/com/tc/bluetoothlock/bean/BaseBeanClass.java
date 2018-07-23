package com.tc.bluetoothlock.bean;

/**
 * Created by tc on 2017/7/7.
 */

public class BaseBeanClass<T> {

    private T returnData;

    public T getReturnData() {
        return returnData;
    }

    public void setReturnData(T returnData) {
        this.returnData = returnData;
    }

    @Override
    public String toString() {
        return "BaseBeanClass{" +
                "result=" + returnData +
                '}';
    }
}
