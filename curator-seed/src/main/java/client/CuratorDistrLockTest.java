package client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

/**
 * @author 44644
 */
public class CuratorDistrLockTest {
    /** Zookeeper info */
    private static final String ZK_ADDRESS = "192.168.1.100:2181";
    private static final String ZK_LOCK_PATH = "/zktest";

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(
                        ZK_ADDRESS,
                        new RetryNTimes(10, 5000));
        client.start();
        System.out.println("zk client start successfully!");

        Thread t1 = new Thread(() -> {
            doWithLock(client);
        }, "t1");

        Thread t2 = new Thread(() ->{
            doWithLock(client);
        });

        t1.start();
        t2.start();
    }

    private static void doWithLock(CuratorFramework client) {
        InterProcessLock lock = new InterProcessMutex(client, ZK_LOCK_PATH);
        try {
            if (lock.acquire(10 * 100, TimeUnit.SECONDS)){
                System.out.println(Thread.currentThread().getName() + " hold lock");
                Thread.sleep(5000L);
                System.out.println(Thread.currentThread().getName() + " release lock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
