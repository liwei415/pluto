package xyz.inux.pluto.model.pojo.vo.sample;

import javax.validation.constraints.Size;

public class RedisInVo {

    @Size(min = 1, max = 8)
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
