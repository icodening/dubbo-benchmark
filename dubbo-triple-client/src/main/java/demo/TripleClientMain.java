package demo;

import org.apache.dubbo.benchmark.Client;
import org.apache.dubbo.benchmark.ClientGrpc;
import org.apache.dubbo.benchmark.ClientPb;
import org.apache.dubbo.benchmark.bean.Page;
import org.apache.dubbo.benchmark.bean.PagePB;
import org.apache.dubbo.benchmark.bean.User;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author icodening
 * @date 2022.09.04
 */
public class TripleClientMain {

    public static void main(String[] args) throws Exception {
        try {
            pb();
//            grpc();
//            simple();
        } finally {
            System.in.read();
        }
//
    }

    private static void grpc() throws Exception {
        ClientGrpc clientGrpc = new ClientGrpc();
        for (int i = 0; i < 10000; i++) {
            PagePB.Page page = clientGrpc.listUser();
        }
    }

    private static void simple() throws Exception {
        Client client = new Client();
        System.out.println("started. sleep 5s");
//        Thread.sleep(5000);
        User user = client.getUser();
        System.out.println(user);
//        System.out.println("1");
//        Thread.sleep(5000);
//        Page<User> page1 = client.listUser();
//        System.out.println("2");
//        Thread.sleep(5000);
    }

    private static void pb() throws Exception {
        ClientPb client = new ClientPb();
        System.out.println("started. sleep 5s");
//        Thread.sleep(5000);
        System.out.println("started");
        PagePB.User detect = client.getUser();
        System.out.println("detect success." + detect.getName());
        ExecutorService executorService = Executors.newFixedThreadPool(64);
//        ThreadPoolExecutor executorService = new ThreadPoolExecutor(32,
//                32,
//                0,
//                TimeUnit.MILLISECONDS,
////                new SynchronousQueue<>());
//                new LinkedBlockingQueue<>(1000));
//        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//
//            try {
//                PagePB.Page page = client.listUser();
////                System.out.println(page);
//                System.out.println(i);
//            } catch (Throwable e){
//                System.out.println(e.getMessage());
//            }
//            finally {
//                Thread.sleep(1000);
//            }
//        }
        for (int j = 0; j < 1000; j++) {
            for (int i = 1; i <= 1000; i++) {
                final int k = i;
                executorService.submit(() -> {
                    try {
                        Class<? extends ExecutorService> aClass = executorService.getClass();
//                        PagePB.User user = client.getUser();
//                        System.out.println(user.getName());
                        PagePB.Page page = client.listUser();
//                        System.out.println(page.getUsersList());
//                        System.out.println("r:"+k);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
            }
            Thread.sleep(100);
        }
        System.in.read();
//        Thread.sleep(5000);
    }
}
