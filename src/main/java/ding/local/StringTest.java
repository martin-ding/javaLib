package ding.local;

public class StringTest {
    volatile int aa = 12;

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        int i = 0;
        String[] strings = new String[10000];

        while(i < 10000) {
            strings[i] = new String(String.valueOf(i % 10));
            i++;
        }

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void test2() {
        String[][] aaa = new String[10][];
        aaa[1] = new String[10];
    }
}
