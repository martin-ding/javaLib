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
 * 思路: 按照组的概念,新加入的点要么是加入到一个大组,要么链接两个组
 * 组包含了所有组员即的所有座位的集合,
 * 比如第一个人a选了(1,2) 两个座位,此时组员集合是(a),座位集合是(1,2)
 * 第二个人b选了(2,3)两个座位,此时组员集合是(a,b), 座位集合是(1,2,3)
 * 第三个人c选了(1,3)两个座位,此时组员集合是(a,b,c),座位集合是(1,2,3) <- 这个是和本组就饱和了
 * 第四个人d选(2,1) 两个座位就会无法分配
 *
 * 思考这么一个问题,包含N个组员的正常组(可以合法分配座位)可以有多少个座位(N和N+1个),N表示饱和, N+1表示可以有一个盈余
 * 在思考一个问题,对于N个组员,N+1个位置,新加入进来的组员是否可以选择组内的任何位置
 * - 首先,什么时候才能保证N个组员有N+1个位置,注意组员座位加入到组的条件是必然有一个座位在组内已经存在,
 * 如(3,2) 加入的(1,2)组, 回到问题,(1,2)(2,3)(3,4) 这样的存在才有可能是保证N个组员 有N+1个位置
 * 就是形成了一个单链,一旦有回路就一定是N个组员N个位置,对于这个单链我们可以将新的成员放在1,2,3,4的任意位置
 *
 * 组的构建: 新节点比较左右两个子group是否有位置空出来,如果没有直接失败,如果有就将两边的子组构成一个大组
 * 注意如果左右两个子group同一个组那直接看这个组是否有位置,把新节点加入到这个组中,位置数不变
 *
 * 复杂度最高O(N^2)
 */

public class SeatOccupy {

    @AllArgsConstructor
    @Data
    static class Group {
        Set<Integer> seats;
        Set<Integer> person;
    }

    public static boolean solution(int[] preferA, int[] preferB, int slot) {
        //使用一个hashMap来记录seat到Group的映射
        Map<Integer, Group> seatGroupMap = new HashMap<>();

        //人数超过slot数,或者数据格式不对 直接返回false
        if (preferA.length > slot || preferA.length != preferA.length) {
            return false;
        }

        for (int i = 0; i < preferA.length; i++) {
            //校验数据
            if (preferA[i] > slot || preferB[i] > slot) {
                return false;
            }

            Group leftGroup = Objects.isNull(seatGroupMap.get(preferA[i])) ? new Group(new HashSet<>(Arrays.asList(preferA[i])), new HashSet<>()) : seatGroupMap.get(preferA[i]);
            Group rightGroup = Objects.isNull(seatGroupMap.get(preferB[i])) ? new Group(new HashSet<>(Arrays.asList(preferB[i])), new HashSet<>()) : seatGroupMap.get(preferB[i]);
            Set<Integer> unionSeatSet = new HashSet<>(leftGroup.getSeats());
            unionSeatSet.addAll(rightGroup.getSeats());

            int sizeSeats = unionSeatSet.size();
            Set<Integer> unionPersonSet = new HashSet<>(leftGroup.getPerson());
            unionPersonSet.addAll(rightGroup.getPerson());
            int sizePerson = unionPersonSet.size();
            if (sizePerson >= sizeSeats) {
                return false;
            }
            //add new person
            unionSeatSet.add(preferA[i]);
            unionSeatSet.add(preferB[i]);
            unionPersonSet.add(i);
            Group group = new Group(unionSeatSet, unionPersonSet);
            unionSeatSet.stream().forEach(seat -> {
                seatGroupMap.put(seat, group);
            });
        }
        return true;
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
