package init.impl;

import init.Init;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FileInit implements Init {
    private Random random = new Random();

    @Override
    public String init() {
        try {
            List<String> datas = FileUtils.readLines(new File("src/main/resources/user_id.txt"), StandardCharsets.UTF_8);
            return datas.get(random.nextInt(datas.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }
}
