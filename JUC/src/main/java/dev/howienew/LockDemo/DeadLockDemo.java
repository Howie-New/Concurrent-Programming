package dev.howienew.LockDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName: DeadLockDemo
 * Package: dev.howienew.lockdemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/5 - 11:55
 */
public class DeadLockDemo {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();
    private static final Object o1 = new Object();
    private static final Object o2 = new Object();

    public static void main(String[] args) {
        DeadLockDemo deadLockDemo = new DeadLockDemo();
//        deadLockDemo.testDeadLock1();
        deadLockDemo.testDeadLock2();
    }

    private void testDeadLock2() {
        new Thread(() -> {
            synchronized (o1) {
                System.out.println(Thread.currentThread().getName() + " get o1 lock and come in");
                synchronized (o2) {
                    System.out.println(Thread.currentThread().getName() + " get o2 lock and come in");
                }
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (o2) {
                System.out.println(Thread.currentThread().getName() + " get o2 lock and come in");
                synchronized (o1) {
                    System.out.println(Thread.currentThread().getName() + " get o1 lock and come in");
                }
            }
        }, "t2").start();
    }

    private void testDeadLock1() {
        new Thread(() -> {
            lock1.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " get lock1 and come in");
                lock2.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " get lock2 and come in");
                } finally {
                    lock2.unlock();
                }
            } finally {
                lock1.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock2.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " get lock2 and come in");
                lock1.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " get lock1 and come in");
                } finally {
                    lock1.unlock();
                }
            } finally {
                lock2.unlock();
            }
        }, "t2").start();
    }
}
