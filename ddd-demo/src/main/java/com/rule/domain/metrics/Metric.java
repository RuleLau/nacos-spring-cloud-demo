package com.rule.domain.metrics;

import com.alibaba.cola.domain.EntityObject;
import com.rule.domain.user.UserProfile;
import lombok.Getter;
import lombok.Setter;

public abstract class Metric extends EntityObject implements Measurable {

    private double score;

    @Getter
    @Setter
    protected UserProfile metricOwner;

    public Metric(){

    }

    public Metric(UserProfile metricOwner){
        this.metricOwner = metricOwner;
    }


    /**
     * 度量名称，用于UI显示
     * @return
     */
    abstract public String getName();

    /**
     * 度量Code，用于数据库存储
     * @return
     */
    abstract public String getCode();

    abstract public double getWeight();

    @Override
    public String toString(){
        return this.getName() + " " + this.score;
    }

}
