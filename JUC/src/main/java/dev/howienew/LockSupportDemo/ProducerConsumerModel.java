package dev.howienew.LockSupportDemo;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName: ProducerConsumerModel
 * Package: dev.howienew.LockSupportDemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/6 - 13:15
 */
public class ProducerConsumerModel {
    public static void main(String[] args) {
//        syncWaitNotifyDemo();
//        lockAwaitSignal();
        parkUnpark();
    }

    private static void parkUnpark() {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " come in");
            System.out.println(Thread.currentThread().getName() + " park");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " wake up ");
        }, "t1");
        t1.start();

//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " come in and trying to unpark t1");
            LockSupport.unpark(t1);
        }, "t2").start();
    }

    private static void lockAwaitSignal() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in");
                System.out.println(Thread.currentThread().getName() + " start await");
                condition.await();
                System.out.println(Thread.currentThread().getName() + " wake up ");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in and trying to signal t1");
                condition.signal();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }


    private static void syncWaitNotifyDemo() {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " come in");
                try {
                    System.out.println(Thread.currentThread().getName() + " start wait");
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " wake up ");
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " come in and trying to notify t1");
                lock.notify();
            }
        }, "t2").start();
    }

    private static void printNumber() {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                for (int i = 0; i <= 20; i += 2) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                    try {
                        lock.notify();
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            synchronized (lock) {
                for (int i = 1; i <= 20; i += 2) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                    try {
                        lock.notify();
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }, "t2").start();
    }
}
