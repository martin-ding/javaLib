package local.ding.e3test;

/**
 * You are given a string letters made of N English letters.
 * Count the number of different letters that appear in both uppercase and lowercase
 * where all lowercase occurrences of the given letter appear before any uppercase occurrence.
 *
 * For example, for letters = "aaAbcCABBc" the answer is 2.
 * The condition is met for letters 'a' and 'b', but not for 'c'
 * Write a function:
 * class Solution { public int solution(String letters)};
 * that, given a string letters, returns the number of different letters fulfilling the conditions above.
 * Examples:
 * 1. Given letters = "aaAbcCABBc", the function should return 2, as explained above.
 * 2. Given letters = "xyzXYZabcABC", the function should return 6.
 * 3. Given letters - "ABCabcAefG, the function should return 0.
 *
 * Write an efficient algorithm for the following assumptions:
 * - N is an integer within the range (1..100.000),
 * - string letters is made only of letters (a-z and/or A-Z).
 *
 * 保证所有小写字母出现在大写字母前面,且同一个字母的大小写字母必须都出现
 */

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 * 思路1,可以使用一个hashmap来表示数据,key是小写字母,value是一个三元组,
 * 第一个表示a的最新位置,
 * 第二个表示大A的最新位置,
 * 第三个表示状态,-1表示不匹配, 1 表示匹配, 0表示初始状态
 */
public class StringCountVersion0 {
    @Data
    @AllArgsConstructor
    public static class Tuple {
        Integer lowCasePosition;
        Integer upperCasePosition;
        Integer state;
    }
    public static void main(String[] args) {
        String str = "aaAbcCABBc";
        String str1 = "xyzXYZabcABC";
        String str2 = "ABCabcAefG";
        System.out.println(solution(str));
        System.out.println(solution(str1));
        System.out.println(solution(str2));
    }

    public static long solution(String letters) {
        char[] charArray = letters.toCharArray();
        Map<Character, Tuple> characterTupleMap = new HashMap<>();
        for (int i = 0; i < charArray.length; i++) {
            Tuple tuple = null;
            if(characterTupleMap.containsKey(Character.toLowerCase(charArray[i]))) {
                tuple = characterTupleMap.get(Character.toLowerCase(charArray[i]));
            } else {
                //初始状态是0
                tuple = new Tuple(null, null, 0);
            }

            //update tuple
            if(tuple.getState() != -1) {
                if(Character.isLowerCase(charArray[i])) {
                    //如果大写的位置上已经有值就直接更新状态是-1
                    if(Objects.nonNull(tuple.getUpperCasePosition())) {
                        tuple.setState(-1);
                    } else {
                        tuple.setLowCasePosition(i);
                    }
                }

                if(Character.isUpperCase(charArray[i])) {
                    //如果小写的位置上没值就直接更新状态是-1
                    if(Objects.isNull(tuple.getLowCasePosition())) {
                        tuple.setState(-1);
                    } else {
                        tuple.setState(1);
                        tuple.setUpperCasePosition(i);
                    }
                }
            }
            characterTupleMap.put(Character.toLowerCase(charArray[i]), tuple);
        }
        long count = characterTupleMap.values().stream().filter(t -> t.getState() == 1).count();
        return count;
    }
}
