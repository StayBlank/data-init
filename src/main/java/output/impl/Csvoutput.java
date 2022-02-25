package output.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import output.Output;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Csvoutput implements Output {
    @Override
    public void write(List<Map<String, String>> datas) {
        try {
            List<String> datasValueStr = datas.stream().map(data -> data.values().stream().map(v -> v + "").collect(Collectors.joining(","))).collect(Collectors.toList());
            FileUtils.writeLines(new File("src/main/resources/datas.txt"), datasValueStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
