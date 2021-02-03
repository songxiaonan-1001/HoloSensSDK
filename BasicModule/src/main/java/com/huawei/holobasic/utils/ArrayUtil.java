package com.huawei.holobasic.utils;

import java.util.Arrays;

public class ArrayUtil {

    /**
     * 判断数组是否为空
     * */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }


    /**
     * 判断数组中是否包含某元素
     *
     * @param array
     * @param v
     * @return
     */
    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (v != null && v.equals(e) || e == v)
                return true;

        return false;
    }


    /**
     * 删除数组中指定位置的元素
     *
     * @param arr      源数组
     * @param position 删除的下标
     * @return 删除完指定元素之后的新数组
     */
    public static <T> T[] arrayRemoveAt(T[] arr, int position) {

        if (arr == null) return null;
        if (position < 0 || position >= arr.length) return arr;
        // 删除最后一个元素
        if (position == arr.length - 1) return Arrays.copyOf(arr, position);
        // 删除第一个元素
        if (position == 0) return Arrays.copyOfRange(arr, position + 1, arr.length);

        T[] retArr;
        // 获取指定位置之前的所有元素，从arr数组中0下标开始复制position长度个元素
        T[] arr1 = Arrays.copyOf(arr, position);
        // 获取指定位置之后的所有元素，从arr数组中(position + 1)下标开始到(arr.length)下标结束复制元素
        T[] arr2 = Arrays.copyOfRange(arr, position + 1, arr.length);
        // 创建一个(arr1.length + arr2.length)长度的数组，先把arr1所有元素复制过来，其余补null
        retArr = Arrays.copyOf(arr1, arr1.length + arr2.length);
        // 从arr2数组的0下标开始复制，复制到retArr数组的arr1.length下标处，共复制arr2.length个长度的元素
        System.arraycopy(arr2, 0, retArr, arr1.length, arr2.length);
        return retArr;
    }

}
