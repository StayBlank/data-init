import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import init.Init;
import model.Config;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String configPath = "src/main/resources/config.json";
        File configFile = new File(configPath);
        try {
            String configStr = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
            List<Config> configList = JSONArray.parseArray(configStr, Config.class);
            JSONObject data = new JSONObject();
            configList.forEach(config -> {
                String configClazz = config.getClazz();
                try {
                    Class clazz = Class.forName(configClazz);
                    try {
                        Init init = (Init) clazz.newInstance();
                        String initValue = init.init();
                        System.out.println(initValue);
                        data.put(config.getName(), initValue);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
