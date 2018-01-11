package cn.et;

import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ZkpTest {

    public static void main(String[] args) throws Exception {

        String zkServers = "localhost:2181"; // ip:端口,多台机器之间用逗号（,）隔开

        ZkClient zkClient = new ZkClient(zkServers, 10000, 5000);

        //写入(修改)数据和读取数据的方法：writeData(path, object)---zkClient.readData(path)

        // 当/user节点不存在事就创建
        if (!zkClient.exists("/user")) {
            // 创建一个永久节点/user
            zkClient.createPersistent("/user");

            // 创建两个顺序节点/user/ls 返回值是顺序节点的真实的名字 ls0000000000  ls0000000001
            zkClient.create("/user/ls", "boy", CreateMode.PERSISTENT_SEQUENTIAL);
            zkClient.create("/user/ls", "boys", CreateMode.PERSISTENT_SEQUENTIAL);
        }

        // 创建一个零时节点/user/zs
        zkClient.createEphemeral("/user/zs", "girl");

        // 监控/db节点
        zkClient.subscribeDataChanges("/user/zs", new IZkDataListener() {
            // 当/user/zs节点被删除时触发
            public void handleDataDeleted(String arg0) throws Exception {

            }

            // 当/user/zs节点被修改时触发
            public void handleDataChange(String arg0, Object arg1) throws Exception {
                System.out.println(arg0 + "---" + arg1);
            }
        });

        // 与server保持连接
        while (true) {
            TimeUnit.SECONDS.sleep(5);
        }
    }

}
