package client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;

/**
 * @author 44644
 */
public class CuratorLeaderTest {

    /** Zookeeper info */
    private static final String ZK_ADDRESS = "192.168.1.100:2181";
    private static final String ZK_PATH = "/zktest";

    public static void main(String[] args) {
        LeaderSelectorListener listener = new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println(Thread.currentThread().getName() + " take leadership");
                // takeLeadership() method should only return when leadership is being relinquished.
                Thread.sleep(5000L);
                System.out.println(Thread.currentThread().getName() + " relinquish leadership!");
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        };

        new Thread(() -> {
            registerListener(listener);
        }).start();
    }

    private static void registerListener(LeaderSelectorListener listener) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();


        // Ensure path
        try {
            new EnsurePath(ZK_PATH).ensure(client.getZookeeperClient());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register listener
        LeaderSelector selector = new LeaderSelector(client, ZK_PATH, listener);
        selector.autoRequeue();
        selector.start();
    }

}
