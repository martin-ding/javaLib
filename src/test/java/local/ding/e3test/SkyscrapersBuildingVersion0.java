package local.ding.e3test;

/**
 * You are given an array A of N integers, representing the maximum heights of N skyscrapers(摩天大厦) to be built.
 * Your task is to specify the actual heights of the skyscrapers, given that:
 * • the height of the K-th skyscraper should be positive and not bigger than A[K];
 * • no two skyscrapers should be of the same height;
 * • the total sum of the skyscrapers' heights should be the maximum possible.
 * Write a function:
 * class Solution { public At[] solution (int[] A); }
 *
 * that, given an array A of N integers, returns an array B of N integers where
 * B[K] is the assigned height of the Kth skyscraper satisfying the above conditions.
 * If there are several possible answers, the function may return any of them.
 * You may assume that it is always possible to build all skyscrapers while fulfilling all the requirements.
 * Examples:
 * - 1. Given A = [1, 2, 3], your function should return [1, 2, 3], as all of the skyscrapers
 * may be built to their maximum height.
 * - 2. Given A = [9, 4, 3, 7, 7], your function may return [9, 4, 3, 7, 6].
 * Note that [9, 4, 3, 6, 7] is also a valid answer. It is not possible for the last two skyscrapers to
 * have the same height. The height of one of them should be 7 and the other should be 6.
 * - 3. Given A = [2, 5, 4, 5, 5], your function should return [1, 2, 3, 4, 5]
 *
 *  Write an efficient algorithm for the following assumptions:
 * • N is an integer within the range [1..50,000] (高楼数量很多);
 * • each element of array A is an integer within the range [1..1,000,000,000](每个高楼楼层很高);
 * • there is always a solution for the given input.
 *
 * 保证摩天大厦不能有重复的高度,重复高度的只能向下减一直到没有重复的高度为止,保证所有摩天大厦的高度总和最大
 * 注意这里有顺序要求,如果没有顺序需求应该如何处理?
 */

import java.util.*;
import java.util.stream.Collectors;

/**
 * 思路1,int a 遍历数组arr,用另外一个长度 = arr中最大元素的数组result来保存结果,
 * 比较, 然后查看对应的位置上有没有数据,没有的话就占用,否则就减一然后再看result上面是否被占用
 * 问题- result会很大,N相对很小, result空间很浪费
 *
 * 思路2, 使用hashmap, key是高度, value是该高度对应的数量,然后从高往低处理
 */
public class SkyscrapersBuildingVersion0 {
    public static void main(String[] args) {
        solution1(new int[]{2, 5, 4, 5, 5});
    }

    /**
     * 暂不考虑位置
     * @param arr
     * @return
     */
    public static int[] solution1(int[] arr) {
        Map<Integer,Integer> heightNumMap = new HashMap<>();
        Set<Integer> heightSet = new HashSet<>();//用于存放楼的高度,方便排序
        for(int i = 0; i < arr.length; i++) {
            Integer heightNum = heightNumMap.get(arr[i]);
            if(Objects.isNull(heightNum)) {
                heightNum = 1;
            } else {
                heightNum++;
            }
            heightSet.add(arr[i]);
            heightNumMap.put(arr[i], heightNum);
        }
        // reverse sorted
        List<Integer> reversedSortedHeight = heightSet.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//        sortedHeight.stream().forEach(System.out::println);
        for(int i = 0; i < reversedSortedHeight.size(); i++) {
             int currentHeight = reversedSortedHeight.get(i);
             int nextHeight = i == reversedSortedHeight.size() - 1 ? 0 : reversedSortedHeight.get(i + 1);
             //修改当前高度的数量
            int gap = heightNumMap.get(currentHeight) - (currentHeight - nextHeight);
            if (gap > 0) {
                if (nextHeight != 0) {
                    heightNumMap.put(nextHeight, heightNumMap.get(nextHeight) + gap);
                }
                heightNumMap.put(currentHeight, heightNumMap.get(currentHeight) - gap);
            }
        }
        // 如果不需要输出顺序,只需要输出所有高楼楼层总和,就只需要对heightNumMap处理就可以了
        // System.out.println(heightNumMap);

        //构建数组返回
        Map<Integer,Integer> heightNumOccupiedMap = new HashMap<>();
        int[] returnArr = new int[arr.length];
        for(int i = 0; i < arr.length; i++) {
            int height = arr[i];
            int enableHeightNum = heightNumMap.get(height);
            int heightNumOccupied = Objects.isNull(heightNumOccupiedMap.get(height))? 0: heightNumOccupiedMap.get(height);
            if (heightNumOccupied < enableHeightNum) {
                height = height - heightNumOccupied;
                heightNumOccupied++;
                heightNumOccupiedMap.put(arr[i], heightNumOccupied);
                returnArr[i] = height;
            }
        }
        System.out.println(returnArr);

        return returnArr;
    }
}
