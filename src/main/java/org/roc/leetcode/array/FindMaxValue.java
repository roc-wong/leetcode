package org.roc.leetcode.array;

/**
 * @author roc
 * @since 2020/6/18 15:27
 */
public class FindMaxValue {

    public static void main(String[] args) {
        int[] array = new int[]{1, 3, 7, 20, 28, 16, 9, 2, 1};

        int maxValue = getMaxValue(array);
        System.out.println(maxValue);
    }

    //参数arr，先升序后降序的数组，不能为空
    public static int getMaxValue(int[] arr) {
        int len = arr.length;
        //当只有一个数时，直接返回
        if(len == 1){
            return arr[0];
        }
        //当数组只有下降部分时，直接返回
        if(arr[0] > arr[1]) {
            return arr[0];
        }
        //当数组只有上升部分时，直接返回
        if(arr[len - 1] > arr[len - 2]) {
            return arr[len - 1];
        }

        //接下来处理既有上升部分又有下降部分的数组（这样的数组至少有3个元素）
        int left = 0, right = arr.length - 1;
        while(left < right) {
            int mid = (int) Math.floor((left + right) / 2);

            //判断arr[mid]是否为最大值
            if(mid != 0 && arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
                return arr[mid];
            }

            //当arr[mid]不是最大值时，判断arr[mid]处于数组上升部分还是下降部分
            if(mid == 0 || arr[mid] > arr[mid - 1]) {
                //处于上升部分
                left = mid + 1;
            } else if(arr[mid] < arr[mid - 1]) {
                //处于下降部分
                right = mid - 1;
            }
        }
        return arr[left];
    }

}
