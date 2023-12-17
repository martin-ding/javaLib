package local.ding.e3test;

/**
 * There are N patients (numbered from 0 to N-1) who want to visit the doctor.
 * The doctor has S possible appointment slots, numbered from 1 to S. Each of the patients has two preferences. Patient K would like to visit the doctor during either slot A[K] or slot B[K]. The doctor can treat only one patient during each slot.
 * Is it possible to assign every patient to one of their preferred slots so that there will be at most one patient assigned to each slot?
 * Write a function:
 * class Solution { public boolean solution(int[] A, int[] B, int S); }
 * that, given two arrays A and B, both of N integers, and an integer S, returns true if it is possible to assign every patient to one of their preferred slots, one patient to one slot, and false otherwise.
 * Examples:
 * 1. Given A = [1, 1, 3]. B = [2, 2, 1] and S = 3, the function should return true.
 * We could assign patients in the following way: [1, 2, 3], where the K-th element of the array represents the number of the slot to which patient K was assigned. Another correct assignment would be [2, 1, 3]. On the other hand, [2, 2, 1] would be an incorrect assignment as two patients would be
 * 2. Given A = [3, 2, 3, 1], B = [1, 3, 1, 2] and S = 3, the function should return
 * false. There are only three slots available, but there are four patients who want to visit the doctor. It is therefore not possible to assign the patients to the slots so that only one patient at a time would visit the doctor.
 * 3. Given A = [2, 5, 6, 5], B = [5, 4, 2, 2] and S = 8, the function should return
 * true. For example, we could assign patients in the following way: [5, 4, 6, 2].
 * 4. Given A = [1, 2, 1, 6, 8, 7, 8], B = [2, 3, 4, 7, 7, 8, 7] and S = 10, the function
 * should return false. It is not possible to assign all of the patients to one of their preferred slots so that only one patient will visit the doctor during one slot.
 * Write an efficient algorithm for the following assumptions:
 * • Nis an integer within the range [1.100,000];
 * • S is an integer within the range [2...100,000]:
 * • each element of arrays A and B is an integer within the range
 */
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 * 思路: 使用DP的思想,也用到了回溯
 * 暴力迭代的时间复杂度O(2^N * N) N 是病人的个数,因为一共有2^N 个迭代,每个迭代中最耗时的是slot的prefer的复制,最高是N
 * 空间复杂度树的高度是N(迭代数),然后每个迭代中用了两个数组,且最大是N,所以我们可以理解空间使用上O(N^2)
 * |3  (3) |
 * |2  (2) |
 * |1  (1) |
 * 因为这些数组会保留在栈中所以空间复杂度是O(N^2)
 *
 * 加上缓存,slot set 和对应下一个选的座位作为key,boolean作为value
 */
public class SeatOccupy1 {
    public static boolean solution(int[] preferA, int[] preferB, int slot) {
        HashSet<Integer> slots = new HashSet<>();
        for(int i=0;i<slot;i++){
            slots.add(i+1);
        }

        return slotTake(slots, preferA, preferB);
    }

    public static boolean slotTake(HashSet<Integer> slots, int[] perferA, int[] perferB) {
        // if select left
        boolean ret = false;
        if(slots.contains(perferA[0])) {
            if(perferA.length == 1)  return true;
            slots.remove(perferA[0]);
            int[] arr1 = new int[perferA.length - 1];
            for (int i = 1; i < perferA.length; i++) {
                arr1[i - 1] = perferA[i];
            }
            int[] arr2 = new int[perferB.length - 1];
            for (int i = 1; i < perferB.length; i++) {
                arr2[i - 1] = perferB[i];
            }
            ret = slotTake(slots, arr1, arr2);
            if (!ret) slots.add(perferA[0]);
        }
        // if select right
        if(slots.contains(perferB[0]) && !ret) {
            if(perferB.length == 1)  return true;
            slots.remove(perferB[0]);
            int[] arr1 = new int[perferA.length - 1];
            for (int i = 1; i < perferA.length; i++) {
                arr1[i - 1] = perferA[i];
            }
            int[] arr2 = new int[perferB.length - 1];
            for (int i = 1; i < perferB.length; i++) {
                arr2[i - 1] = perferB[i];
            }
            ret = slotTake(slots, arr1, arr2);
            if (!ret) slots.add(perferB[0]);
        }

        return ret;

    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 1, 3}, new int[]{2, 2, 1}, 3));
        System.out.println(solution(new int[]{3, 2, 3, 2}, new int[]{1, 3, 1, 2}, 3));
        System.out.println(solution(new int[]{2, 5, 6, 5}, new int[]{5, 4, 2, 2}, 8));
        System.out.println(solution(new int[]{1, 2, 1, 6, 8, 7, 8}, new int[]{2, 3, 4, 7, 7, 8, 7}, 10));
        System.out.println(solution(new int[]{1, 1, 2, 3, 5, 5, 4}, new int[]{2, 2, 3, 4, 6, 6, 5}, 10));

        /**
         * <<<--- result --->>>
         * true
         * false
         * true
         * false
         * false
         */
    }
}
