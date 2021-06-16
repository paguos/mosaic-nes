package com.github.paguos.mosaic.fed.nebulastream.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class to create nes workers
 */
public class NesBuilder {

    protected final String name;

    /**
     * Create a builder for a nes coordinator
     *
     * @param name of the nes coordinator
     * @return an instance of the builder
     */
    public static NesCoordinatorBuilder createCoordinator(String name) {
        return new NesCoordinatorBuilder(name);
    }

    /**
     * Create a builder for a nes source
     *
     * @param name of the nes source
     * @return an instance of the builder
     */
    public static NesSourceBuilder createSource(String name) {
        return new NesSourceBuilder(name);
    }

    /**
     * Create a builder for a nes worker
     *
     * @param name of the nes worker
     * @return an instance of the builder
     */
    public static NesWorkerBuilder createWorker(String name) {
        return new NesWorkerBuilder(name);
    }

    private NesBuilder(String name) {
        this.name = name;
    }

    public static class  NesCoordinatorBuilder extends NesBuilder {

        protected int coordinatorPort;
        protected int restPort;

        protected List<NesNode> children;

        private NesCoordinatorBuilder(String name) {
            super(name);
            this.coordinatorPort = Coordinator.DEFAULT_COORDINATOR_PORT;
            this.restPort = Coordinator.DEFAULT_REST_PORT;
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

        public Coordinator build () {
            return new Coordinator(this);
        }
    }


    public static abstract class NesNodeBuilder extends NesBuilder {

        protected int parentId;
        protected int dataPort;
        protected int rpcPort;

        private NesNodeBuilder(String name) {
            super(name);
            this.parentId = -1;
            this.dataPort = Worker.DEFAULT_DATA_PORT;
            this.rpcPort = Worker.DEFAULT_RPC_PORT;
        }
    }

    public static class NesSourceBuilder extends NesNodeBuilder {

        protected String logicalStreamName;
        protected String physicalStreamName;

        protected String sourceConfig;
        protected SourceType sourceType;

        private NesSourceBuilder(String name) {
            super(name);
            this.logicalStreamName = "default_logical";
            this.physicalStreamName = "default_physical";
            this.sourceConfig = null;
            this.sourceType = SourceType.DefaultSource;
        }

        public NesSourceBuilder logicalStreamName(String logicalStreamName) {
            this.logicalStreamName = logicalStreamName;
            return this;
        }

        public NesSourceBuilder physicalStreamName(String physicalStreamName) {
            this.physicalStreamName = physicalStreamName;
            return this;
        }

        public NesSourceBuilder sourceConfig(String sourceConfig) {
            this.sourceConfig = sourceConfig;
            return this;
        }

        public NesSourceBuilder sourceType(SourceType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        /** From Parent **/

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

        public Source build() {
            return new Source(this);
        }

    }

    public static class NesWorkerBuilder extends NesNodeBuilder {

        protected List<NesNode> children;

        protected NesWorkerBuilder(String name) {
            super(name);
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

        public Worker build() {
            return new Worker(this);
        }
    }
}
