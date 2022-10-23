package demo;

import org.apache.dubbo.benchmark.ClientPb;

/**
 * @author icodening
 * @date 2022.10.10
 */
public class PbClientMain {

    public static void main(String[] args) throws Exception {

        ClientPb clientPb = new ClientPb();
        boolean user = clientPb.createUser();
        System.out.println(user);
    }
}
