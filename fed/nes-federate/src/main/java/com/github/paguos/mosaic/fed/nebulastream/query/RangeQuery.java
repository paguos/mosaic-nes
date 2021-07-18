package com.github.paguos.mosaic.fed.nebulastream.query;

import com.github.paguos.mosaic.fed.geo.GeoSquare;
import com.github.paguos.mosaic.fed.nebulastream.query.operator.FilterOperator;
import com.github.paguos.mosaic.fed.nebulastream.query.operator.Predicate;
import com.github.paguos.mosaic.fed.nebulastream.query.operator.PredicateBuilder;
import org.eclipse.mosaic.lib.geo.GeoPoint;
import stream.nebula.exceptions.EmptyFieldException;

import stream.nebula.operators.From;
import stream.nebula.queryinterface.Query;

public class RangeQuery extends Query {

    private static final String LAT_FIELD = "latitude";
    private static final String LONG_FIELD = "longitude";

    private final GeoSquare range;

    public RangeQuery(GeoPoint position, double area) {
        this.range = new GeoSquare(position, area);
    }

    @Override
    public Query from(String logicalStreamName) throws EmptyFieldException {
        if (logicalStreamName.isEmpty()) {
            throw new EmptyFieldException();
        }
        this.operators.add(new From(logicalStreamName));

        Predicate predicate = PredicateBuilder.createPredicate(
                Predicate.onField(LAT_FIELD).lessThanOrEqual(range.getNorthBound().getLatitude()))
                .and(Predicate.onField(LAT_FIELD).greaterThanOrEqual(range.getSouthBound().getLatitude()))
                .and(Predicate.onField(LONG_FIELD).lessThanOrEqual(range.getEastBound().getLongitude()))
                .and(Predicate.onField(LONG_FIELD).greaterThanOrEqual(range.getWestBound().getLongitude()))
                .build();
        super.operators.add(new FilterOperator(predicate));

        return  this;
    }


}
