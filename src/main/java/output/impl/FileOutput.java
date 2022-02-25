package output.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import output.Output;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileOutput implements Output {

    @Override
    public void write(List datas) {
        String datasStr = JSONObject.toJSONString(datas);
        try {
            FileUtils.writeStringToFile(new File("src/main/resources/datas.txt"), datasStr, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
