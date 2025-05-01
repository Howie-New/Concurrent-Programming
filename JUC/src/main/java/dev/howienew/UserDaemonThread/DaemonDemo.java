package dev.howienew.UserDaemonThread;

import java.util.concurrent.TimeUnit;

/**
 * ClassName: DaemonDemo
 * Package: dev.howienew.UserDaemonThread
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/1 - 15:08
 */
public class DaemonDemo {

    public static void main(String[] args) {
        //Thread t1 is daemon thread
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start running");
            System.out.println(Thread.currentThread().getName() + " is " + (Thread.currentThread().isDaemon() ? "Daemon Thread" : "User Thread"));
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " is still running");
            }
        }, "t1");
        //This method must be invoked before the thread is started.
        t1.setDaemon(true);
        t1.start();

        //Main
        System.out.println(Thread.currentThread().getName() + " start running");
        System.out.println(Thread.currentThread().getName() + " is " + (Thread.currentThread().isDaemon() ? "Daemon Thread" : "User Thread"));
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + " end running");
    }
}
