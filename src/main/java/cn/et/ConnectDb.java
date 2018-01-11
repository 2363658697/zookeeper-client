package cn.et;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ConnectDb {

    private static String url = null;
    private static String driverClass = null;
    private static String username = null;
    private static String password = null;

    public static Connection getConnection(String url, String driverClass, String username, String password)
            throws Exception {

        Class.forName(driverClass);

        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

    public static void main(String[] args) throws Exception {

        String zkServers = "localhost:2181"; // ip:端口,多台机器之间用逗号（,）隔开

        final ZkClient zkClient = new ZkClient(zkServers, 10000, 5000);

        if (!zkClient.exists("/db")) {

            zkClient.createPersistent("/db");

            zkClient.createPersistent("/db/url", "jdbc:mysql://192.168.14.66:3306/work");
            zkClient.createPersistent("/db/driverClass", "com.mysql.jdbc.Driver");
            zkClient.createPersistent("/db/username", "root");
            zkClient.createPersistent("/db/password", "123456");

        }

        url = zkClient.readData("/db/url");
        driverClass = zkClient.readData("/db/driverClass");
        username = zkClient.readData("/db/username");
        password = zkClient.readData("/db/password");

        Connection connection = getConnection(url, driverClass, username, password);

        System.out.println(connection);

        // 监控/db节点
        zkClient.subscribeDataChanges("/db/url", new IZkDataListener() {
            // 当/user/zs节点被删除时触发
            public void handleDataDeleted(String arg0) throws Exception {

            }

            // 当/db/url节点被修改时触发
            public void handleDataChange(String arg0, Object arg1) throws Exception {
                url = zkClient.readData("/db/url");
                driverClass = zkClient.readData("/db/driverClass");
                username = zkClient.readData("/db/username");
                password = zkClient.readData("/db/password");

                Connection connection = getConnection(url, driverClass, username, password);
                System.out.println(connection);
            }
        });

        // 与server保持连接
        while (true) {
            TimeUnit.SECONDS.sleep(5);
        }
    }

}
