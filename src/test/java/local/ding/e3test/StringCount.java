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
 */

import java.util.HashMap;
import java.util.Map;

/**
 * 思路2,可以使用一个hashmap来表示数据,key是小写字母,value是STATE
 */
public class StringCount {

    public static void main(String[] args) {
        String str = "aaAbcCABBc";
        String str1 = "xyzXYZabcABC";
        String str2 = "ABCabcAefG";
        System.out.println(solution(str));//2
        System.out.println(solution(str1));//6
        System.out.println(solution(str2));//0
    }

    public enum STATE {
        STATE_INI, STATE_READY, STATE_SUC, STATE_FAIL
    }

    public static long solution(String letters) {
        char[] charArray = letters.toCharArray();
        Map<Character, STATE> characterTupleMap = new HashMap<>();
        for (int i = 0; i < charArray.length; i++) {
            STATE status = STATE.STATE_INI;
            if(characterTupleMap.containsKey(Character.toLowerCase(charArray[i]))) {
                status = characterTupleMap.get(Character.toLowerCase(charArray[i]));
            }
            if (status.equals(STATE.STATE_INI)) {
                if(Character.isLowerCase(charArray[i])) {
                    status = STATE.STATE_READY;
                } else {
                    status = STATE.STATE_FAIL;
                }
            }
            if (status.equals(STATE.STATE_READY)) {
                if(Character.isUpperCase(charArray[i])) {
                    status = STATE.STATE_SUC;
                }
            }
            if (status.equals(STATE.STATE_SUC)) {
                if(Character.isLowerCase(charArray[i])) {
                    status = STATE.STATE_FAIL;
                }
            }

            characterTupleMap.put(Character.toLowerCase(charArray[i]), status);
        }
        long count = characterTupleMap.values().stream().filter(s -> STATE.STATE_SUC.equals(s)).count();
        return count;
    }
}
