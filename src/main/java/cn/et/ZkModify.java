package cn.et;


import org.I0Itec.zkclient.ZkClient;

public class ZkModify {

    public static void main(String[] args) {
      String   zkServers="localhost:2181";  //ip:端口,多台机器之间用逗号（,）隔开
        
        ZkClient zkClient=new ZkClient(zkServers, 10000, 5000);
        
       // zkClient.writeData("/user/zs", "boy");

        //zkClient.writeData("/db/url", "jdbc:mysql://192.168.14.66:3306/work");
        
/*        zkClient.writeData("/db/driverClass", "oracle.jdbc.OracleDriver");
        zkClient.writeData("/db/username", "food");
        zkClient.writeData("/db/password", "123456");*/
        zkClient.writeData("/db/url", "jdbc:mysql://localhost:3306/test");

    }

}
