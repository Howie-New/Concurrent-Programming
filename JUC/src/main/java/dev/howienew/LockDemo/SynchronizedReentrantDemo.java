package dev.howienew.LockDemo;

/**
 * ClassName: SynchronizedReentrantDemo
 * Package: dev.howienew.lockdemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/5 - 11:11
 */
public class SynchronizedReentrantDemo {
    public static void main(String[] args) {
        SynchronizedReentrantDemo demo = new SynchronizedReentrantDemo();
//        demo.demo1();
        demo.demo2();
    }

    private synchronized void demo2() {
        System.out.println("come in mission 2");
        m1();
        System.out.println("mission 2 complete!");
    }

    private synchronized void m1() {
        System.out.println("come in stage 1");
        m2();
    }

    private synchronized void m2() {
        System.out.println("come in stage 2");
    }


    private void demo1() {
        Object o = new Object();
        System.out.println("come in mission 1");
        synchronized (o) {
            System.out.println("come in stage 1");
            synchronized (o) {
                System.out.println("come in stage 2");
                synchronized (o) {
                    System.out.println("come in stage 3");
                }
            }
        }
        System.out.println("mission 1 complete!");
    }
}
