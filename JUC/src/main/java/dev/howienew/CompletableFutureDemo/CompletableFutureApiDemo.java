package dev.howienew.CompletableFutureDemo;

import java.util.concurrent.*;

/**
 * ClassName: CompletableFutureApiDemo
 * Package: dev.howienew.CompletableFutureDemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/3 - 13:57
 */
public class CompletableFutureApiDemo {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
//        runAsync();
//        supplyAsync();
//        chainCalls();
//        chainCallsThenApply();
//        chainCallsHandle();
//        chainCallsThenAccept();
//        threadPoolRunningOptions();
//        winnerLoserModel();
        combineResult();
        long endTime = System.currentTimeMillis();
        System.out.println("Cost time: " + (endTime - beginTime) + "ms");
    }

    private static void combineResult() {
         CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
             System.out.println("Task 1 start running");
             try {
                 TimeUnit.MILLISECONDS.sleep(200);
             } catch (InterruptedException e) {
                 throw new RuntimeException(e);
             }
             return 1;
         }).thenCombine(CompletableFuture.supplyAsync(() -> {
             System.out.println("Task 2 start running");
             try {
                 TimeUnit.MILLISECONDS.sleep(300);
             } catch (InterruptedException e) {
                 throw new RuntimeException(e);
             }
             return 10;
         }), (x,y) -> {
             System.out.println("Start combine task 1 and task 2, the result is " + (x + y));
             return x + y;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println("Task 3 start running");
             try {
                 TimeUnit.MILLISECONDS.sleep(400);
             } catch (InterruptedException e) {
                 throw new RuntimeException(e);
             }
             return 100;
         }), (x, y) -> {
             System.out.println("Start combine sum(task 1, task 2) and task 3, the result is " + (x + y));
             return x + y;
         });
        System.out.println("main thread is running");
        System.out.println("The final result is " + completableFuture.join());
    }

    private static void winnerLoserModel() {
        System.out.println("Game begin!");
        CompletableFuture<String> playerA = CompletableFuture.supplyAsync(() -> {
            System.out.println("player A is come in");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Player A";
        });
        CompletableFuture<String> playerB = CompletableFuture.supplyAsync(() -> {
            System.out.println("player B is come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Player B";
        });
        //main
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        CompletableFuture<String> result = playerA.applyToEither(playerB, f -> {
            System.out.println(Thread.currentThread().getName() + " is trial announce " + f + " is winner!");
            return "game over!";
        });
        System.out.println(result.join());
    }

    private static void threadPoolRunningOptions() {
        try {
            CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
                        System.out.println(Thread.currentThread().getName() + " is executing mission 1");
                        return 1;
                    }, threadPool)
                    .thenRunAsync(() -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(Thread.currentThread().getName() + " is executing mission 2");
                    })
                    .thenRun(() -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(Thread.currentThread().getName() + " is executing mission 3");
                    });
            System.out.println(completableFuture.get(2, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    private static void chainCallsThenAccept() {
        try {
            CompletableFuture.supplyAsync(() -> {
                return 1;
            }, threadPool).thenApply(f -> {
                return f + 10;
            }).thenApply(f -> {
                return f + 100;
            }).thenAccept(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    private static void chainCallsHandle() {
        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return 1;
            }, threadPool).handle((num, ex) -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int res = num / 0;
                return num * 2;
            }).handle((num, ex) -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return num * 3;
            }).whenComplete((res, ex) -> {
                if (ex == null) {
                    System.out.println("mission complete , the final res is " + res);
                }
            }).exceptionally(ex -> {
                System.out.println("mission failed , the cause is " + ex.getCause() + "\nthe detail is " + ex.getMessage());
                return -1;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    private static void chainCallsThenApply() {
        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return 1;
            }, threadPool).thenApply(num -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                int res = num / 0;
                return num * 2;
            }).thenApply(num -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return num * 3;
            }).whenComplete((res, ex) -> {
                if (ex == null) {
                    System.out.println("mission complete , the final res is " + res);
                }
            }).exceptionally(ex -> {
                System.out.println("mission failed , the cause is " + ex.getCause() + "\nthe detail is " + ex.getMessage());
                return -1;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
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
            }, threadPool).whenComplete((result, throwable) -> {
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
            threadPool.shutdown();
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
        }, threadPool);
        System.out.println(Thread.currentThread().getName() + " is Running");
        try {
            System.out.println(supplyAsync.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        threadPool.shutdown();
    }


    private static void runAsync() {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " run Async");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool);
        System.out.println(Thread.currentThread().getName() + " is Running");
        try {
            System.out.println(runAsync.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        threadPool.shutdown();
    }
}
