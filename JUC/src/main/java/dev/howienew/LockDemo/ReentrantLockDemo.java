package dev.howienew.LockDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName: ReentrantLockDemo
 * Package: dev.howienew.lockdemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/5 - 11:31
 */
public class ReentrantLockDemo {
    private static Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        demo.demo1();
    }



    private void demo1() {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " get lock and come in");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " get lock and come in");
                } finally {
                    System.out.println(Thread.currentThread().getName() + " unlock and quit");
                    lock.unlock();
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " unlock and quit");
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " get lock and come in");
            } finally {
                System.out.println(Thread.currentThread().getName() + " unlock and quit");
                lock.unlock();
            }
        }, "t2").start();
    }
}
