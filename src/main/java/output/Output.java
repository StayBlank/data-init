package output;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface Output {
    void write(List<Map<String,String>> datas);
}
