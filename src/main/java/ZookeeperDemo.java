import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @classDesc: 功能描述:(使用java语言操作zk)
 * @author: 王武
 * @createTime: 2017年10月11日 下午11:14:08
 * @version: v1.0
 * @copyright:上海江豚教育科技有限公司
 * @QQ:834667820
 */
public class ZookeeperDemo {

	private static final String CONNECT_ADDR = "192.168.229.128:2181,192.168.229.129:2181,192.168.229.130:2181";
	private static final Integer SESSION_TIME = 2000;
	private static final CountDownLatch countDownLatch = new CountDownLatch(1);

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_TIME, new Watcher() {

			public void process(WatchedEvent watchedEvent) {
				// 获取事件的状态
				KeeperState keeperState = watchedEvent.getState();
				EventType tventType = watchedEvent.getType();
				// 如果是建立连接
				if (KeeperState.SyncConnected == keeperState) {
					if (EventType.None == tventType) {
						System.out.println("zk 建立连接");
						// zk连接成功后， 放行，唤醒其他线程 让其让线程执行 notefi
						countDownLatch.countDown();
					}
				}
			}
		});
		// 让主线程等待5秒 让 主线程等待(join)
		countDownLatch.await();
		System.out.println("开始创建zk节点....");
		// String result=zk.create("/parent2","son2".getBytes(),
		// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		String result = zk.create("/parentTemp", "parentTemp".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("创建zk节点成功..." + result);
		Thread.sleep(5000);
		zk.close();
	}
}
