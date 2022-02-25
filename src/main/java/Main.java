import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import init.Init;
import model.Config;
import org.apache.commons.io.FileUtils;
import output.Output;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static Map<String, Class> classMap = new HashMap<>();
    private static Map<String, Init> initMap = new HashMap<>();

    public static void main(String[] args) {
        String configPath = "src/main/resources/config.json";
        File configFile = new File(configPath);
        try {
            String configStr = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
            Config configObject = JSONObject.parseObject(configStr, Config.class);
            List datas = new ArrayList();
            for (int i = 0; i < configObject.getNumber(); i++) {
                JSONObject data = new JSONObject();
                configObject.getFieldConfig().forEach(config -> {
                    String configClazz = config.getClazz();
                    try {
                        Class clazz;
                        if (classMap.containsKey(configClazz)) {
                            clazz = classMap.get(configClazz);
                        } else {
                            clazz = Class.forName(configClazz);
                            classMap.put(configClazz, clazz);
                        }
                        Init init;
                        if (initMap.containsKey(configClazz)) {
                            init = initMap.get(configClazz);
                        } else {
                            init = (Init) clazz.newInstance();
                        }
                        String initValue = init.init();
                        data.put(config.getName(), initValue);
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                });
                datas.add(data);
            }

            Class<?> outputClass = Class.forName(configObject.getOutput());
            try {
                Output output = (Output) outputClass.newInstance();
                output.write(datas);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
