package dev.howienew.CompletableFutureDemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ClassName: NetMallDemo
 * Package: dev.howienew.CompletableFutureDemo
 * Description:
 *
 * @Author: Howie-New
 * @Create: 2025/5/3 - 16:52
 */
public class NetMallDemo {
    private static final List<NetMall> netMalls = Arrays.asList(
            new NetMall("taobao"),
            new NetMall("jd"),
            new NetMall("amazon"),
            new NetMall("dangdang"));

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        String prodName = "MySQL";
//        getResultInSingleThread(prodName);
        getResultInMultiThread(prodName);
        long endTime = System.currentTimeMillis();
        System.out.println("Cost time: " + (endTime - beginTime) + "ms");
    }

    private static void getResultInMultiThread(String prodName) {
        List<String> priceList = netMalls.stream()
                .map(netMall -> CompletableFuture.supplyAsync(() -> {
                    return String.format("%s in %s price is %.2f",
                            prodName, netMall.getMallName(), netMall.calcPrice(prodName));
                })).collect(Collectors.toList())
                .stream()
                .map(stringCompletableFuture -> stringCompletableFuture.join())
                .collect(Collectors.toList());
        priceList.forEach(System.out::println);
    }

    private static void getResultInSingleThread(String prodName) {
        List<String> priceList = netMalls.stream()
                .map(netMall -> String.format("%s in %s price is %.2f",
                        prodName, netMall.getMallName(), netMall.calcPrice(prodName)))
                .collect(Collectors.toList());
        priceList.forEach(System.out::println);
    }
}

@NoArgsConstructor
@Data
class NetMall {
    @Getter
    private String mallName;

    public NetMall(String mallName) {
        this.mallName = mallName;
    }

    public double calcPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ThreadLocalRandom.current().nextDouble(2) * 2 + productName.charAt(0);
    }
}
