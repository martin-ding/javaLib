package ding.local;

public class Main {
    public static String name = "zhangsan";
    public final static String name2 = "zhangsan2";

    public String sss;

    public Main(String[] args) {
        sss = "zhangsan";
    }
    {
        sss = "lsi";
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");

        Thread.sleep(1000000);

    }

    public void test1() {
        {
            String aa = "adasda";
        }
        int a = 1;
        System.out.println("bbbbb");
    }

    public void test2() {
        test1();
    }
}