package client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

/**
 * 监听器
 * @author 44644
 * Path Cache：监视一个路径下
 * 1）孩子结点的创建、
 * 2）删除，
 * 3）以及结点数据的更新。产生的事件会传递给注册的PathChildrenCacheListener。
 *
 * Node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地
 *
 * Tree Cache：Path Cache和Node Cache的“合体”，
 * 监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据。
 * https://www.cnblogs.com/seaspring/p/5536338.html
 */
public class CuratorWatcherTest {

    /** Zookeeper info */
    private static final String ZK_ADDRESS = "192.168.1.26:2181";
    private static final String ZK_PATH = "/zktest";

    public static void main(String[] args) {

        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );

        client.start();
        System.out.println("zk client start successfully!");

        PathChildrenCache watcher = new PathChildrenCache(
                client,
                ZK_PATH,
                true

        );

        watcher.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client1, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data == null) {
                    System.out.println("No data in event[" + event + "]");
                } else {
                    System.out.println("Receive event: "
                            + "type=[" + event.getType() + "]"
                            + ", path=[" + data.getPath() + "]"
                            + ", data=[" + new String(data.getData()) + "]"
                            + ", stat=[" + data.getStat() + "]");
                }
            }
        });
    }
}
