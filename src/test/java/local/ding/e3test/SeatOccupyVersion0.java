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

import com.google.common.collect.Sets;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 问题：一个医生有N个时间段，这时候来了n个病人，每个病人
 * 可以有两个时间段偏好选择，问是否可以选择一种组合使所有的病人
 * 都满足他们的偏好
 * 用两个数组表示n个病人的两个偏好选择
 * eg. A = [1,2,3], B = [2,3,2] N = 5
 * 那么我们可以选择[1,2,4] 或者[2,3,4] 或者[1,3,4] 等
 * 附加条件： N 很大， n也很大
 *
 * 解题思路：肯定不能使用遍历，其复杂程度2^n ---直接跳转到思路3
 * 1.构建一个以偏好位置作为key, 人作为值(List)的hashmap
 * 2.构建一个n长度的空数组用于存放选择偏好，遍历从第一个人开始，
 * 选择其中一个偏好，那么和这个偏好相同的人只能选择另外一个偏好，
 * 把这个人的偏好加入进来，然后和这个人相同的偏好的自能选择另外
 * 一个
 * 3.如果第二步构建完成了，就继续遍历n长度的数组，找到一个空位置
 * 继续随机选择，然后继续2
 *
 * 有问题，如果之前的一个子集导致剩下的子集都有问题这就很麻烦
 * 回退的问题~
 * 可能要用到树的遍历算法
 *
 *
 * 思路2 回溯，实现复杂度极高，且算法复杂度也很高接近 O(2^N)
 * 先选择不冲突的位置，以这个为出发点是不是也是一种思路
 * 比如先让person3 选择4，然后person2 选择3 然后person1 选择1 或者2
 * 但是如果没有不冲突的位置呢？
 *
 * 思路3 (也是本算法的实现)
 * 思考这么一个问题，什么时候无法分配座位(冲突)，
 * 必然是当一个新人加入进来之后，他如果直接选择了对应的两个座位
 * 导致在这两个座位上面的人(包括新人)大于这些人所能选择的座位数
 * 即N个人只有N-1个座位的情况，时间复杂度是O(N),最坏情况O(N^2)
 *
 */

public class SeatOccupyVersion0 {

    @Data
    static class Tuple {
        public Tuple(Integer seat1, Integer seat2, Integer personId) {
            this.first = seat1;
            this.second = seat2;
            this.personId = personId;
        }

        Integer first;
        Integer second;
        Integer personId;
    }

    // 思路， 可以一个人同时占两个位置，只有在新进来的这个人导致比如3个人占两个位置，
    // 4个人占3个位置的情况时就会导致无法分配
    // 检测新进来的这个人所占用位置的其他人占用了多少位置，如果位置数低于人数就报错
    public static boolean solution(int[] perA, int[] perB, int slot) {
        Map<Integer, List<Tuple>> kPreferVPerson = new HashMap<>();

        //人数超过slot数,或者数据格式不对 直接返回false
        if (perA.length > slot || perA.length != perB.length) {
            return false;
        }

        for (int i = 0; i < perA.length; i++) {
            //校验数据
            if (perA[i] > slot || perB[i] > slot) {
                return false;
            }

            Tuple tupleFirst = new Tuple(perA[i], perB[i], i);
            Tuple tupleSecond = new Tuple(perB[i], perA[i], i);
            //get First
            List<Tuple> tupleListFirst = Objects.isNull(kPreferVPerson.get(perA[i])) ? new ArrayList<>() : kPreferVPerson.get(perA[i]);//first
            //get Second
            List<Tuple> tupleListSecond = Objects.isNull(kPreferVPerson.get(perB[i])) ? new ArrayList<>() : kPreferVPerson.get(perB[i]);//first

            tupleListFirst.add(tupleFirst);
            tupleListSecond.add(tupleSecond);
            kPreferVPerson.put(perA[i], tupleListFirst);
            kPreferVPerson.put(perB[i], tupleListSecond);

            Set<Integer> set1 = tupleListFirst.stream().map(e -> e.getSecond()).collect(Collectors.toSet());
            Set<Integer> setPerson1 = tupleListFirst.stream().map(e -> e.getPersonId()).collect(Collectors.toSet());

            Set<Integer> set2 = tupleListSecond.stream().map(e -> e.getSecond()).collect(Collectors.toSet());
            Set<Integer> setPerson2 = tupleListSecond.stream().map(e -> e.getPersonId()).collect(Collectors.toSet());
            int sizeSeatsTaken = Sets.union(set1, set2).size();
            int sizePerson = Sets.union(setPerson1, setPerson2).size();
            if (sizePerson > sizeSeatsTaken) {
                return false;
            }
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
         */
    }
}
