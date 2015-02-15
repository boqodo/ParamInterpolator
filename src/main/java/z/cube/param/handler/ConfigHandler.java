package z.cube.param.handler;


import z.cube.param.config.InitConfig;

public interface ConfigHandler {

    void init(InitConfig initConfig);

    Object getValue(String key);
}
