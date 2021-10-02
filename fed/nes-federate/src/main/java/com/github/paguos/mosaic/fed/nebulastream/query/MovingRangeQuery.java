package com.github.paguos.mosaic.fed.nebulastream.query;

import com.github.paguos.mosaic.fed.nebulastream.query.operator.MovingRangeOperator;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.From;
import stream.nebula.queryinterface.Query;

public class MovingRangeQuery extends Query {

    private final String nodeId;
    private final long area;

    public MovingRangeQuery(String nodeId, long area) {
        this.nodeId = nodeId;
        this.area = area;
    }

    @Override
    public Query from(String logicalStreamName) throws EmptyFieldException {
        if (logicalStreamName.isEmpty()) {
            throw new EmptyFieldException();
        }
        this.operators.add(new From(logicalStreamName));
        this.operators.add(new MovingRangeOperator(nodeId, area));
        return this;
    }
}
