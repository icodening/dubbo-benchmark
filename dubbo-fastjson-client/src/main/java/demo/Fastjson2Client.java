package demo;

import org.apache.dubbo.benchmark.Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author icodening
 * @date 2022.10.10
 */
public class Fastjson2Client {

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            try {
                executorService.execute(()->{
                    try {
                        client.getUser();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(client.getUser());
        }
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        executorService.execute(() -> {
//            for (int i = 0; true; i++) {
//                try {
//                    client.getUser();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////            System.out.println(client.getUser());
//            }
//        });
//        executorService.execute(() -> {
//            for (int i = 0; true; i++) {
//                try {
//                    client.getUser();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////            System.out.println(client.getUser());
//            }
//        });
//        executorService.execute(() -> {
//            for (int i = 0; true; i++) {
//                try {
//                    client.getUser();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////            System.out.println(client.getUser());
//            }
//        });

    }
}
