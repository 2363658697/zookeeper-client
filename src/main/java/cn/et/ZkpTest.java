package cn.et;

import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ZkpTest {

    public static void main(String[] args) throws Exception {

        String zkServers = "localhost:2181"; // ip:�˿�,��̨����֮���ö��ţ�,������

        ZkClient zkClient = new ZkClient(zkServers, 10000, 5000);

        //д��(�޸�)���ݺͶ�ȡ���ݵķ�����writeData(path, object)---zkClient.readData(path)

        // ��/user�ڵ㲻�����¾ʹ���
        if (!zkClient.exists("/user")) {
            // ����һ�����ýڵ�/user
            zkClient.createPersistent("/user");

            // ��������˳��ڵ�/user/ls ����ֵ��˳��ڵ����ʵ������ ls0000000000  ls0000000001
            zkClient.create("/user/ls", "boy", CreateMode.PERSISTENT_SEQUENTIAL);
            zkClient.create("/user/ls", "boys", CreateMode.PERSISTENT_SEQUENTIAL);
        }

        // ����һ����ʱ�ڵ�/user/zs
        zkClient.createEphemeral("/user/zs", "girl");

        // ���/db�ڵ�
        zkClient.subscribeDataChanges("/user/zs", new IZkDataListener() {
            // ��/user/zs�ڵ㱻ɾ��ʱ����
            public void handleDataDeleted(String arg0) throws Exception {

            }

            // ��/user/zs�ڵ㱻�޸�ʱ����
            public void handleDataChange(String arg0, Object arg1) throws Exception {
                System.out.println(arg0 + "---" + arg1);
            }
        });

        // ��server��������
        while (true) {
            TimeUnit.SECONDS.sleep(5);
        }
    }

}
