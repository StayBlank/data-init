package output.impl;

import org.apache.commons.collections.CollectionUtils;
import output.Output;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcOutput implements Output {
    @Override
    public void write(List<Map<String, String>> datas, String name) {
        if (CollectionUtils.isEmpty(datas)) {
            return;
        }
        String columns = datas.get(0).keySet().stream().collect(Collectors.joining(","));
        String columnsReplace = datas.get(0).keySet().stream().map(k -> "?").collect(Collectors.joining(","));
        String sql = "insert into " + name + "(" + columns + ") values(" + columnsReplace + ")";
        System.out.println(sql);
        this.insertArpStandardList(datas, sql);

    }

    public boolean insertArpStandardList(List<Map<String, String>> list, String sql) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://myapp:3306/kxh", "root", "root");
             PreparedStatement ps = conn.prepareStatement(sql);) {

            //优化插入第一步       设置手动提交
            conn.setAutoCommit(false);

            int len = list.size();
            Set<String> keySet = list.get(0).keySet();
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < keySet.size(); j++) {
                    ps.setString(j, list.get(i).get(keySet.toArray()[j]));
                }


                //if(ps.executeUpdate() != 1) r = false;    优化后，不用传统的插入方法了。

                //优化插入第二步       插入代码打包，等一定量后再一起插入。
                ps.addBatch();
                //if(ps.executeUpdate() != 1)result = false;
                //每200次提交一次
                if ((i != 0 && i % 200 == 0) || i == len - 1) {//可以设置不同的大小；如50，100，200，500，1000等等
                    ps.executeBatch();
                    //优化插入第三步       提交，批量插入数据库中。
                    conn.commit();
                    ps.clearBatch();        //提交后，Batch清空。
                }
            }

        } catch (Exception e) {
            System.out.println("MibTaskPack->getArpInfoList() error:" + e.getMessage());
            return false;   //出错才报false
        }
        return true;
    }
}
