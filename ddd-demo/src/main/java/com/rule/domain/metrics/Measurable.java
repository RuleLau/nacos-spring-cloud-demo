package com.rule.domain.metrics;

import java.io.Serializable;

public interface Measurable extends Serializable {

    public double calculateScore();
}
