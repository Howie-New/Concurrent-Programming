package dev.howienew.CompletableFutureDemo;

import java.util.concurrent.*;

/**
 * ClassName: FutureTaskDemo
 * Package: dev.howienew.CompletableFutureDemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/1 - 15:47
 */
public class FutureTaskDemo {
    public static void main(String[] args) {
//        simpleUse();
//        mainUsage();
        problems();
    }

    private static void problems() {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            //Execute a task that cost long time
            TimeUnit.SECONDS.sleep(10);
            return "Task finished";
        });
        new Thread(futureTask, "t1").start();

        //1. futureTask.get() will blocking to get results
//        try {
//            System.out.println(futureTask.get());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println(Thread.currentThread().getName() + " executing some task");
        //2. It can also be limited to a specified time to complete, if it cannot be completed, an exception is thrown
//        try {
//            futureTask.get(3L, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (TimeoutException e) {
//            throw new RuntimeException(e);
//        }
        //3. CPU polling access
        while (!futureTask.isDone()) {
            try {
                System.out.println("Waiting for task to finish");
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mainUsage() {
        long begin = System.currentTimeMillis();
        AsynchronousTasks();
        long end = System.currentTimeMillis();
        System.out.println("cost " + (end - begin) + " millis seconds");
    }

    private static void AsynchronousTasks() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            FutureTask<String> futureTask1 = new FutureTask<>(() -> {
                TimeUnit.MILLISECONDS.sleep(300);
                return "task 1 finished ";
            });
            FutureTask<String> futureTask2 = new FutureTask<>(() -> {
                TimeUnit.MILLISECONDS.sleep(350);
                return "task 2 finished ";
            });
            FutureTask<String> futureTask3 = new FutureTask<>(() -> {
                TimeUnit.MILLISECONDS.sleep(400);
                return "task 3 finished ";
            });
            threadPool.submit(futureTask1);
            threadPool.submit(futureTask2);
            threadPool.submit(futureTask3);
            String answer = futureTask1.get() + futureTask2.get() + futureTask3.get();
            System.out.println(answer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    private static void simpleUse() {
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " start running");
        return "Hello from Callable!";
    }
}
