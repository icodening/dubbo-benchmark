package demo;

import org.apache.dubbo.benchmark.ClientGrpc;
import org.apache.dubbo.benchmark.bean.PagePB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author icodening
 * @date 2022.09.04
 */
public class GrpcClientMain {

    public static void main(String[] args) throws Exception {
        ClientGrpc clientGrpc = new ClientGrpc();
        System.out.println("started. sleep 5s");
//        Thread.sleep(5000);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
//        ThreadPoolExecutor executorService = new ThreadPoolExecutor(20, 20,
//                0L, TimeUnit.MILLISECONDS,
//                new SynchronousQueue<>());
//        for (int i = 0; i < Integer.MAX_VALUE; i++) {
////                        System.out.println(j);
//            try {
//                PagePB.Page page = clientGrpc.listUser();
////                System.out.println(page);
//                System.out.println(i);
//            } catch (Throwable e){
//                System.out.println(e.getMessage());
//            }
//            finally {
//                Thread.sleep(1000);
//            }
//        }



        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                final int k = i;
                executorService.submit(() -> {
                    try {
                        PagePB.Page page = clientGrpc.listUser();
//                        System.out.println(j);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
