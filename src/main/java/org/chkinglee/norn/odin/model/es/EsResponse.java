package org.chkinglee.norn.odin.model.es;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/8/1
 **/
public class EsResponse {

    private int took;
    @JSONField(name = "timed_out")
    private boolean timedOut;
    @JSONField(name = "_shards")
    private Shards shards;
    private Hits hits;

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }
}
