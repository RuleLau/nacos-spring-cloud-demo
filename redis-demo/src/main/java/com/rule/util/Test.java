package com.rule.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {

    private static ArrayUtil singleton = new ArrayUtil();
    private static ArrayUtil singleton1 = null;
    private static volatile ArrayUtil singleton2 = null;

    public static synchronized ArrayUtil get() {
        if (singleton1 == null) {
            singleton1 = new ArrayUtil();
        }
        return singleton1;
    }


    public static ArrayUtil get2() {
        if (singleton2 == null) {
            synchronized (ArrayUtil.class) {
                if (singleton2 == null) {
                    singleton2 = new ArrayUtil();
                }
            }
        }
        return singleton2;
    }


    static class ArrayUtil {
    }


    /**
     * Create a function to implement these requirements: input an array of String,
     * and then return an array of all Strings that is the second max length. (Java Stream API is better)
     *
     * Test Case 1:
     * Input: {"cc", "bbb", "a", "eeeee", "hh", "123123", "abcd", "121212", "aaaaa", "w3"}
     * Output: {"eeeee", "aaaaa"}
     *
     * Test Case 2:
     * Input: {"cc", "bbb", "a", "eeeee", "hh", "123123", "abcd", null, "aaaaa", "w3"}
     * Output: {"eeeee", "aaaaa"}
     * @param args
     */
    public static void main(String[] args) {
        String[] strs = new String[]{"cc", "bbb", "a", "eeeee", "hh", "123123", "abcd", "121212", "aaaaa", "w3"};
        Map<Integer, List<String>> collect = Arrays.stream(strs).collect(Collectors.groupingBy(e -> e.length()));
        
        int max = Integer.MIN_VALUE;
        for (String s : strs) {
            max = Math.max(s.length(), max);
        }
        int finalMax = max;
        List<String> list = Arrays.stream(strs).filter(item -> item.length() != finalMax).sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
        int second = list.get(list.size() - 1).length();
        for (int i = list.size() - 1; i >=0; i--) {
            if (second == list.get(i).length()) {
                System.out.println(list.get(i));
            } else {
                break;
            }
        }

    }

}
