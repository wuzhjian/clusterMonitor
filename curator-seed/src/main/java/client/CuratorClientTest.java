package client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import static org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.Media.print;

/**
 * @author 44644
 */
public class CuratorClientTest {
    private static final String ZK_ADDRESS = "192.168.1.26:2181";
    private static final String ZK_PATH = "/zktest";

    public static void main(String[] args) throws Exception {
        // 连接
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(100, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");

        // create node
        String data1 = "hello";
        print("Create", ZK_PATH, data1);
        client.create()
                .creatingParentsIfNeeded()
                .forPath(ZK_PATH, data1.getBytes());

        // get nodes and data
        print("ls" ,"/");
        print(String.valueOf(client.getChildren().forPath("/")));
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        // modify data
        String data2 = "world";
        print("set", ZK_PATH, data2);
        client.setData().forPath(ZK_PATH, data2.getBytes());
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        // remove node
        print("delete", ZK_PATH);
        client.delete().forPath(ZK_PATH);
        print("ls", "/");
        print(client.getChildren().forPath("/"));
    }

    private static void print(String... cmds) {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds){
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }
    private static void print(Object result){
        System.out.println(
                result instanceof byte[] ? new String((byte[]) result) : result
        );
    }
}
