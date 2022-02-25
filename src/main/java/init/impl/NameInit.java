package init.impl;

import init.Init;
import utils.NameRandom;

import java.util.UUID;

public class NameInit implements Init {
    @Override
    public String init() {
        return NameRandom.getChineseName();
    }
}
