package com.github.paguos.mosaic.fed.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class to create nes workers
 */
public class NesBuilder {

    protected final int id;
    protected final String name;

    /**
     * Create a builder for a nes coordinator
     *
     * @param id   of the nes coordinator
     * @param name of the nes coordinator
     * @return an instance of the builder
     */
    public static NesCoordinatorBuilder createCoordinator(int id, String name) {
        return new NesCoordinatorBuilder(id, name);
    }

    /**
     * Create a builder for a nes source
     *
     * @param id   of the nes source
     * @param name of the nes source
     * @return an instance of the builder
     */
    public static NesSourceBuilder createSource(int id, String name) {
        return new NesSourceBuilder(id, name);
    }

    /**
     * Create a builder for a nes worker
     *
     * @param id   of the nes worker
     * @param name of the nes worker
     * @return an instance of the builder
     */
    public static NesWorkerBuilder createWorker(int id, String name) {
        return new NesWorkerBuilder(id, name);
    }

    private NesBuilder(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static class  NesCoordinatorBuilder extends NesBuilder {

        protected int coordinatorPort;
        protected int restPort;

        protected List<NesNode> children;

        private NesCoordinatorBuilder(int id, String name) {
            super(id, name);
            this.coordinatorPort = NesCoordinator.DEFAULT_COORDINATOR_PORT;
            this.restPort = NesCoordinator.DEFAULT_REST_PORT;
            this.children = new ArrayList<>();
        }

        public NesCoordinatorBuilder coordinatorPort(int coordinatorPort) {
            this.coordinatorPort = coordinatorPort;
            return this;
        }

        public NesCoordinatorBuilder restPort(int restPort) {
            this.restPort = restPort;
            return this;
        }

        public NesCoordinatorBuilder children(List<NesNode> children) {
            this.children = children;
            return this;
        }

        public NesCoordinator build () {
            return new NesCoordinator(this);
        }
    }


    public static abstract class NesNodeBuilder extends NesBuilder {

        protected int parentId;
        protected int dataPort;
        protected int rpcPort;

        private NesNodeBuilder(int id, String name) {
            super(id, name);
            this.parentId = -1;
            this.dataPort = NesWorker.DEFAULT_DATA_PORT;
            this.rpcPort = NesWorker.DEFAULT_RPC_PORT;
        }
    }

    public static class NesSourceBuilder extends NesNodeBuilder {

        protected NesSourceType sourceType;

        private NesSourceBuilder(int id, String name) {
            super(id, name);
            this.sourceType = NesSourceType.DefaultSource;
        }

        public NesSourceBuilder sourceType(NesSourceType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public NesSourceBuilder parentId(int parentId) {
            this.parentId = parentId;
            return this;
        }

        public NesSourceBuilder dataPort(int dataPort) {
            this.dataPort = dataPort;
            return this;
        }

        public NesSourceBuilder rpcPort(int rpcPort) {
            this.rpcPort = rpcPort;
            return this;
        }

        public NesSource build() {
            return new NesSource(this);
        }

    }

    public static class NesWorkerBuilder extends NesNodeBuilder {

        protected List<NesNode> children;

        protected NesWorkerBuilder(int id, String name) {
            super(id, name);
            children = new ArrayList<>();
        }

        public NesWorkerBuilder parentId(int parentId) {
            this.parentId = parentId;
            return this;
        }

        public NesWorkerBuilder dataPort(int dataPort) {
            this.dataPort = dataPort;
            return this;
        }

        public NesWorkerBuilder rpcPort(int rpcPort) {
            this.rpcPort = rpcPort;
            return this;
        }

        public NesWorkerBuilder children(List<NesNode> children) {
            this.children = children;
            return this;
        }

        public NesWorker build() {
            return new NesWorker(this);
        }
    }
}
