import com.alibaba.fastjson.JSONObject;
import init.Init;
import model.Config;
import org.apache.commons.io.FileUtils;
import output.Output;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static Map<String, Class> classMap = new HashMap<>();
    private static Map<String, Init> initMap = new HashMap<>();
    private static ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        String configPath = "src/main/resources/config.json";
        File configFile = new File(configPath);
        try {
            String configStr = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
            Config configObject = JSONObject.parseObject(configStr, Config.class);

            int number = configObject.getNumber();
            int threadNumber = configObject.getThreadNumber();
            int numberPerThread = number / threadNumber;
            int lastThradNumber = number % threadNumber;
            for (int j = 0; j < threadNumber; j++) {

                Thread thread = getThread(configObject, numberPerThread, lastThradNumber, j);
                exec.submit(thread);
            }
            exec.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Thread getThread(Config configObject, int numberPerThread, int lastThradNumber, int j) {
        int finalJ = j;
        Thread thread = new Thread(finalJ + "") {
            @Override
            public void run() {
                List datas = new ArrayList();
                int realNumber = numberPerThread;
                if (finalJ == 0) {
                    realNumber += lastThradNumber;
                }
                for (int i = 0; i < realNumber; i++) {
                    Map data = new LinkedHashMap();
                    configObject.getFieldConfig().forEach(config -> {
                        String configClazz = config.getClazz();
                        try {
                            Class clazz = getaClass(configClazz);
                            Init init = getInit(configClazz, clazz);
                            String initValue = init.init();
                            data.put(config.getName(), initValue);
                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    });
                    datas.add(data);
                }
                Class<?> outputClass = null;
                try {
                    outputClass = Class.forName(configObject.getOutput());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    Output output = (Output) outputClass.newInstance();
                    output.write(datas, finalJ + "");
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        };
        return thread;
    }

    private static Init getInit(String configClazz, Class clazz) throws InstantiationException, IllegalAccessException {
        Init init;
        if (initMap.containsKey(configClazz)) {
            init = initMap.get(configClazz);
        } else {
            init = (Init) clazz.newInstance();
        }
        return init;
    }

    private static Class getaClass(String configClazz) throws ClassNotFoundException {
        Class clazz;
        if (classMap.containsKey(configClazz)) {
            clazz = classMap.get(configClazz);
        } else {
            clazz = Class.forName(configClazz);
            classMap.put(configClazz, clazz);
        }
        return clazz;
    }


}
