package dev.howienew.CompletableFutureDemo;

import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * ClassName: CompletableFutureBuildDemo
 * Package: dev.howienew.CompletableFutureDemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/3 - 13:57
 */
public class CompletableFutureBuildDemo {
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
//        runAsync();
//        supplyAsync();
        chainCalls();
    }

    private static void chainCalls() {
        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + " is Running at stage 1");
                int random = ThreadLocalRandom.current().nextInt(10);
                if (random < 5) {
                    int div = random / 0;
                }
                return random;
            }, executorService).whenComplete((result, throwable) -> {
                if (throwable == null) {
                    System.out.println("stage 1 result: " + result);
                    System.out.println(Thread.currentThread().getName() + "is Running at stage 2");
                }
            }).exceptionally(throwable -> {
                System.out.println("stage 1 have some problems " + throwable.getCause() + "\t" + throwable.getMessage());
                return 0;
            });
            System.out.println(Thread.currentThread().getName() + " is Running");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }


    private static void supplyAsync() {
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " supply Async");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Supply Async";
        }, executorService);
        System.out.println(Thread.currentThread().getName() + " is Running");
        try {
            System.out.println(supplyAsync.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
    }


    private static void runAsync() {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " run Async");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
        System.out.println(Thread.currentThread().getName() + " is Running");
        try {
            System.out.println(runAsync.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
    }
}
