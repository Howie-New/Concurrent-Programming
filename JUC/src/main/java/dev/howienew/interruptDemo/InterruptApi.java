package dev.howienew.interruptDemo;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: InterruptApi
 * Package: dev.howienew.interruptDemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/5 - 18:17
 */
public class InterruptApi {
    public static void main(String[] args) {
//        testInterrupt();
//        testInterruptException();
        testInterrupted();
    }

    private static void testInterrupted() {
//        Thread.interrupted();//静态方法
//        Thread.currentThread().interrupt();//实例方法
        System.out.println(Thread.currentThread().getName() + " interrupt status: " + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + " interrupt status: " + Thread.interrupted());
        Thread.currentThread().interrupt();
        System.out.println(Thread.currentThread().getName() + " interrupt status: " + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + " interrupt status: " + Thread.interrupted());
    }

    private static void testInterruptException() {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " is interrupted, soon exit");
                    break;
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    //当处于wait,join,sleep状态中的线程被中断后会抛出一个interruptException,并恢复中断标志位为false,所以需要在catch代码块中再次调用自身的interrupt()
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " is still running");
            }
        }, "t1");
        t1.start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " trying to interrupt t1");
            t1.interrupt();
            System.out.println(Thread.currentThread().getName() + " interrupted t1, t1 interrupt status:" + t1.isInterrupted());
        }, "t2").start();
    }

    private static void testInterrupt() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                System.out.println(Thread.currentThread().getName() + " execute mission " + i);
            }
            System.out.println(Thread.currentThread().getName() + " mission complete, current interrupt status: " + Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        System.out.println("t1 interrupt status: " + t1.isInterrupted());
        try {
            TimeUnit.MILLISECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + " trying to interrupt t1");
        //设置中断标志位为true，并不会真正去中断或停止线程
        t1.interrupt();
        System.out.println(Thread.currentThread().getName() + " interrupt t1, t1 interupt status: " + t1.isInterrupted());

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("t1 interrupt status: " + t1.isInterrupted());
    }
}
