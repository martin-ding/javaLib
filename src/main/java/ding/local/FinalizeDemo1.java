package ding.local;

public class FinalizeDemo1 {
    static FinalizeExample fe =null;

    public static void main(String[] args) throws InterruptedException {
        fe = new FinalizeExample();
        fe = null;
        System.gc();
        Thread.sleep(3000);//等待GC完成
        System.out.println("~~~~~~~~");
        System.out.println(fe);
        System.out.println("~~~~end~~~~");

        fe = null;
        System.gc();
        Thread.sleep(3000);//等待GC完成
        System.out.println("~~~~~~~~");
        System.out.println(fe);
        System.out.println("~~~~end~~~~");

        System.runFinalization();

    }

    static class FinalizeExample{
        protected void finalize(){
            System.out.println("---fin start---");
            System.out.println(this);
            fe = this;
            System.out.println(fe);
            System.out.println("+++fin end+++");
        }
    }


}
