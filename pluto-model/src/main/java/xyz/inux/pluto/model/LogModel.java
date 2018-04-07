package xyz.inux.pluto.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;

public class LogModel {
    /** 附加信息集 */
    private Map<String, Object> datas;
    private String method;
    private long start = 0L;
    private final AtomicInteger serialId = new AtomicInteger(0);

    /**
     * 私有构造方法，初始化LogModel
     * @param name
     */
    private LogModel(String name) {
        datas = new HashMap<String, Object>();
        start=System.currentTimeMillis();
        method = name + "#" +start  + "#";
        datas.put("_method", method);
    }

    /**
     * LogModel入口，创建一个新的LogModel实例
     * @param method 该LogModel实例的标志
     * @return
     */
    public static LogModel newLogModel(String method) {
        return new LogModel(method);
    }
    /**
     * 设置结果码和结果提示信息
     * @param result
     * @param message
     * @return
     */
    public LogModel setResultMessage(long result, String message) {
        addMetaData("_result", result).addMetaData("_message", message);
        return this;
    }
    /**
     * 增加一个名称为key，值为value的属性
     * @param key
     * @param value
     * @return
     */
    public LogModel addMetaData(String key, Object value) {
        if (value != null){
            datas.put(key, value);
        } else {
            datas.put(key, "");
        }
        return this;
    }
    /**
     * 删除一个名称为key的属性
     * @param key
     * @return
     */
    public LogModel delMetaData(String key) {
        if (key != null) {
            datas.remove(key);
        }
        return this;
    }
    /**
     * LogModel转化为Map
     * @return
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
    /**
     * LogModel转化为json字符串
     * @param purge true清空LogModel（只保留_method属性）;false保留所有属性
     * @return
     */
    public String toJson(boolean purge) {
        try {
            datas.put("_serialId", serialId.incrementAndGet());
            if (purge) {
                datas.put("handleCost", System.currentTimeMillis() - start);
                JSONObject ja = (JSONObject)JSONObject.toJSON(datas);
                purge();

                return ja.toString();
            } else {
                Map<String, Object> map = toMap();
                map.put("handleCost", System.currentTimeMillis() - start);
                JSONObject ja = (JSONObject)JSONObject.toJSON(map);

                return ja.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{data:error}";
    }

    private void purge() {
        this.datas.clear();
        datas.put("_method", method);

    }
    public String endJson() {
        return toJson(true);

    }
    public String toJson() {
        return toJson(true);

    }
}