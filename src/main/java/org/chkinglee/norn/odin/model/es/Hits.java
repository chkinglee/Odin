package org.chkinglee.norn.odin.model.es;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/8/1
 **/
public class Hits {

    private Total total;
    @JSONField(name = "max_score")
    private int maxScore;
    private List<Hit> hits;

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }
}
