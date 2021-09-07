package com.github.paguos.mosaic.fed.nebulastream.query.operator;

import stream.nebula.operators.Operator;

public class MovingRangeOperator extends Operator  {

    private final String nodeId;
    private final int area;

    public MovingRangeOperator(String nodeId, int area) {
        this.nodeId = nodeId;
        this.area = area;
    }

    @Override
    public String getCppCode() {
        return String.format(".movingRange(\"%s\", %d)", nodeId, area);
    }
}
