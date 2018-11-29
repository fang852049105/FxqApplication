package com.example.fangxq.myapplication.manager;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Fangxq on 2018/5/25.
 */

public class SortManager {

    private static Integer[] a = {2, 33, 40, 14, 18, 37, 59, 6, 10, 20, 18};
    private static List<Integer> numbers = Arrays.asList(a);

    /**
     * 选择排序算法
     * 在未排序序列中找到最小元素，存放到排序序列的起始位置
     * 再从剩余未排序元素中继续寻找最小元素，然后放到排序序列末尾。
     * 以此类推，直到所有元素均排序完毕。
     */
    public static Integer[] selectSort() {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            int value =  a[i];
            int position = i;
            for (int j = i + 1; j < len; j++) {
                if (a[j] < value) {
                    value = a[j];
                    position = j;
                }
            }
            a[position] = a[i];
            a[i] = value;
        }
        Log.e("fxq", "a = " + Arrays.asList(a));
        return a;
    }


    /**希尔排序的原理:根据需求，如果你想要结果从大到小排列，它会首先将数组进行分组，然后将较大值移到前面，较小值
     * 移到后面，最后将整个数组进行插入排序，这样比起一开始就用插入排序减少了数据交换和移动的次数，可以说希尔排序是加强
     * 版的插入排序
     * 拿数组5, 2, 8, 9, 1, 3，4来说，数组长度为7，当increment为3时，数组分为两个序列
     * 5，2，8和9，1，3，4，第一次排序，9和5比较，1和2比较，3和8比较，4和比其下标值小increment的数组值相比较
     * 此例子是按照从大到小排列，所以大的会排在前面，第一次排序后数组为9, 2, 8, 5, 1, 3，4
     * 第一次后increment的值变为3/2=1,此时对数组进行插入排序，
     *实现数组从大到小排
     */

    public static List<Integer> sheelSort() {
        int len = numbers.size();
        while (len != 0) {
            len = len / 2;
            for (int i = 0; i < len; i ++) { // 分组
//                for (int j = i + len; j < numbers.size(); j += len) { //元素从分组中的第二个开始
//                    int k = j - len; //k为有序序列最后一个数的位置
//                    int temp = numbers.get(j);
//                    while (k >= 0 && temp < numbers.get(k)) { //从后往前遍历
//                        numbers.set(k + len, numbers.get(k));
//                        k -= len;
//                    }
//                    numbers.set(k + len, temp);
//                }
                for (int j = i + len; j < a.length; j += len) {//元素从第二个开始
                    int k = j - len;//k为有序序列最后一位的位数
                    int temp = a[j];//要插入的元素
                    while (k >= 0 && temp < a[k]) {//从后往前遍历
                        a[k + len] = a[k];
                        k -= len;//向前移动len位
                    }
                    a[k + len] = temp;
                }
            }
        }
        Log.e("fxq", "numbers = " + numbers.toString());
        return numbers;
    }


    /**
     * 插入排序
     *
     * 从第一个元素开始，该元素可以认为已经被排序
     * 取出下一个元素，在已经排序的元素序列中从后向前扫描
     * 如果该元素（已排序）大于新元素，将该元素移到下一位置
     * 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置
     * 将新元素插入到该位置中
     * 重复步骤2
     * O（n^2）
     */

    public static Integer[] insertSort() {
        int len = a.length;
        int insertNum; //要插入的数
        for (int i = 1; i < len; i ++) { //插入次数，即循环次数
            insertNum = a[i];
            int j = i - 1; //已经排好的序列的最后一个数的位置
            while(j >= 0 && a[j] > insertNum) {  //从后往前，将大于insertNum的数向后移动
                a[j+1] = a[j];
                j --;
            }
            a[j+1] = insertNum;
        }
        Log.e("fxq", "a = " + Arrays.asList(a));
        return a;
    }

     /**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     */
    public static Integer[] bubbleSort() {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {//注意第二重循环的条件
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
        Log.e("fxq", "a = " + Arrays.asList(a));
        return a;
    }

    public void quickSort(int[] a, int start, int end) {
        if (start < end) {
            int baseNum = a[start];//选基准值
            int midNum;//记录中间值
            int i = start;
            int j = end;
            do {
                while ((a[i] < baseNum) && i < end) {
                    i++;
                }
                while ((a[j] > baseNum) && j > start) {
                    j--;
                }
                if (i <= j) {
                    midNum = a[i];
                    a[i] = a[j];
                    a[j] = midNum;
                    i++;
                    j--;
                }
            } while (i <= j);
            if (start < j) {
                quickSort(a, start, j);
            }
            if (end > i) {
                quickSort(a, i, end);
            }
        }
    }

}
