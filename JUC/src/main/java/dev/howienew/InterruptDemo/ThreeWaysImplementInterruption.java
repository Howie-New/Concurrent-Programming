package dev.howienew.InterruptDemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ClassName: ThreeWaysImplementInterruption
 * Package: dev.howienew.interruptDemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/5 - 16:57
 */
public class ThreeWaysImplementInterruption {
    private static volatile boolean interrupted = false;
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    public static void main(String[] args) {
//        volatileWay();
//        atomicBooleanWay();
        interruptWay();
    }

    private static void interruptWay() {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " is interrupted!");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " is still running!");
            }
        }, "t1");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " trying to interrupt t1!");
            t1.interrupt();
            System.out.println(Thread.currentThread().getName() + " interrupted t1!");
        }, "t2").start();
    }

    private static void atomicBooleanWay() {
        new Thread(() -> {
            while (true) {
                if (atomicBoolean.get()) {
                    System.out.println(Thread.currentThread().getName() + " is interrupted!");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " is still running!");
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " trying to interrupt t1!");
            atomicBoolean.set(true);
            System.out.println(Thread.currentThread().getName() + " interrupted t1!");
        }, "t2").start();
    }

    private static void volatileWay() {
        new Thread(() -> {
            while (true) {
                if (interrupted) {
                    System.out.println(Thread.currentThread().getName() + " is interrupted!");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " is still running!");
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " trying to interrupt t1!");
            interrupted = true;
            System.out.println(Thread.currentThread().getName() + " interrupted t1!");
        }, "t2").start();
    }
}
