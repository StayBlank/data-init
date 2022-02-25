package init.impl;

import init.Init;

import java.util.UUID;

public class IdInit implements Init {
    @Override
    public String init() {
        return UUID.randomUUID().toString();
    }
}
