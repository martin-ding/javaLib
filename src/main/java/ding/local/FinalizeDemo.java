package ding.local;

public class FinalizeDemo {
    static FinalizeExample fe =null;

    public static void main(String[] args) throws InterruptedException {
        FinalizeExample obj = new FinalizeExample();
        obj = null;
        System.gc();
        Thread.sleep(1000);//等待GC完成
        System.out.println("~~~~~~~~");

        System.out.println(fe);
        System.out.println("~~~~~~~~");

        fe = null;//去掉唯一的引用
        System.gc();//没有GC
        Thread.sleep(3000);
        System.out.println(fe);//null
    }

    static class FinalizeExample{
        static int i = 1;
        public FinalizeExample getA() {
            return a;
        }

        public void setA(FinalizeExample a) {
            this.a = a;
        }

        private FinalizeExample a;

        protected void finalize(){
            System.out.println("---fin start---");
            System.out.println(this);
            fe = this;
            System.out.println(fe);
            System.out.println("+++fin end+++");
        }
    }


}
