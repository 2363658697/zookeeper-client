package cn.et;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

public class DbConnection {

    public static Connection getConnection(String url, String driverClass, String username, String password)
            throws Exception {

        Class.forName(driverClass);

        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

    public static void main(String[] args) throws Exception {

        String zkServers = "localhost:2181"; // ip:�˿�,��̨����֮���ö��ţ�,������

      final ZkClient  zkClient = new ZkClient(zkServers, 10000, 5000,new BytesPushThroughSerializer());

       
        byte[] url = zkClient.readData("/db/url");
        byte[] driverClass = zkClient.readData("/db/driverClass");
        byte[] username = zkClient.readData("/db/username");
        byte[] password = zkClient.readData("/db/password");


       Connection connection = getConnection(new String(url),new String(driverClass),new String(username),new String(password));

       System.out.println(connection);
       
        // ���/db�ڵ�
        zkClient.subscribeDataChanges("/db/url", new IZkDataListener() {
            // ��/user/zs�ڵ㱻ɾ��ʱ����
            public void handleDataDeleted(String arg0) throws Exception {

            }

            // ��/db/url�ڵ㱻�޸�ʱ����
            public void handleDataChange(String arg0, Object arg1) throws Exception {

                byte[] url = zkClient.readData("/db/url");
                byte[] driverClass = zkClient.readData("/db/driverClass");
                byte[] username = zkClient.readData("/db/username");
                byte[] password = zkClient.readData("/db/password");
                Connection connection = getConnection(new String(url),new String(driverClass),new String(username),new String(password));
                System.out.println(connection);
            }
        });

        // ��server��������
        while (true) {
            TimeUnit.SECONDS.sleep(5);
        }
    }

}
