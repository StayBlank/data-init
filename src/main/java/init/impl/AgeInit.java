package init.impl;

import init.Init;
import utils.NameRandom;

import java.util.Random;

public class AgeInit implements Init {
    private Random random = new Random();

    @Override
    public String init() {
        return random.nextInt(100) + "";
    }
}
