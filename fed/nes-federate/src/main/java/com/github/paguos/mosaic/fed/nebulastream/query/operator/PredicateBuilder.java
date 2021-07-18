package com.github.paguos.mosaic.fed.nebulastream.query.operator;

import org.apache.commons.lang3.tuple.MutablePair;

public class PredicateBuilder {

    private final Predicate root;
    private Predicate last;

    private PredicateBuilder(Predicate root) {
        this.root = root;
        this.last = root;
    }

    public static PredicateBuilder createPredicate(Predicate predicate) {
        return new PredicateBuilder(predicate);
    }

    public PredicateBuilder and(Predicate predicate) {
        this.last.setNext(new MutablePair<>("&&", predicate));
        this.last = predicate;
        return this;
    }

    public PredicateBuilder or(Predicate predicate) {
        this.last.setNext(new MutablePair<>("||", predicate));
        this.last = predicate;
        return this;
    }
    
    public Predicate build() {
        return root;
    }
    
}
