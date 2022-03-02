package output.impl;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import output.Output;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HdfsOutput implements Output {
    @Override
    public void write(List<Map<String, String>> datas, String name) {
        try {
            List<String> datasValueStr = datas.stream().map(data -> data.values().stream().map(v -> v + "").collect(Collectors.joining(","))).collect(Collectors.toList());
            createFile("/datas/test/" + name + ".txt", datasValueStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据插入hdfs中，用于load到hive表中，默认分隔符是"\001"
     *
     * @param dst
     * @throws IOException
     */
    public static void createFile(String dst, List<String> argList) throws IOException {
        System.setProperty("HADOOP_USER_NAME", "root");
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl.disable.cache", "true");
        conf.set("fs.defaultFS", "hdfs://myapp:9000");
        FileSystem fs = FileSystem.get(conf);
//        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fs.listFiles(new Path("/datas"), false);
        Path dstPath = new Path(dst); //目标路径
        //打开一个输出流
        fs.createNewFile(dstPath);
        FSDataOutputStream outputStream = fs.create(dstPath);
        StringBuffer sb = new StringBuffer();
        for (String arg : argList) {
            sb.append(arg + "\n");
        }
        sb.deleteCharAt(sb.length() - 2);//去掉最后一个换行符
        byte[] contents = sb.toString().getBytes();
        outputStream.write(contents);
        outputStream.close();
        fs.close();
        System.out.println("文件创建成功！");

    }

    /**
     * 将HDFS文件load到hive表中
     *
     * @param dst
     */
    public static void loadData2Hive(String dst) {
        String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";
        String CONNECTION_URL = "jdbc:hive2://server-13:10000/default;auth=noSasl";
        String username = "admin";
        String password = "admin";
        Connection con = null;

        try {
            Class.forName(JDBC_DRIVER);
            con = (Connection) DriverManager.getConnection(CONNECTION_URL, username, password);
            Statement stmt = con.createStatement();

            String sql = " load data inpath '" + dst + "' into table population.population_information ";

            stmt.execute(sql);
            System.out.println("loadData到Hive表成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 关闭rs、ps和con
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
