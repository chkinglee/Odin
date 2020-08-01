package org.chkinglee.norn.odin.model.es;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/8/1
 **/
public class Hit {

    @JSONField(name = "_index")
    private String index;
    @JSONField(name = "_type")
    private String type;
    @JSONField(name = "_id")
    private String id;
    @JSONField(name = "_score")
    private int score;
    @JSONField(name = "_source")
    private String source;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
