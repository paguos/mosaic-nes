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

    public static ZeroMQSourceBuilder createZeroMQSource (String name) {
        return new ZeroMQSourceBuilder(name);
    }

    private NesBuilder(String name) {
        this.name = name;
    }

    public static class  NesCoordinatorBuilder extends NesBuilder {

        protected int coordinatorPort;
        protected int restPort;
        protected int locationUpdateInterval;

        protected boolean dynamicDuplicatesFilterEnabled;
        protected boolean routePredictionEnabled;
        protected int numberOfPointsInLocationStorage;
        protected int numberOfTuplesInFilterStorage;

        protected List<NesNode> children;

        private NesCoordinatorBuilder(String name) {
            super(name);
            this.coordinatorPort = Coordinator.DEFAULT_COORDINATOR_PORT;
            this.restPort = Coordinator.DEFAULT_REST_PORT;
            this.locationUpdateInterval = Coordinator.DEFAULT_UPDATE_INTERVAL;
            this.dynamicDuplicatesFilterEnabled = Coordinator.DEFAULT_DYNAMIC_FILTER_ENABLED;
            this.routePredictionEnabled = Coordinator.DEFAULT_ROUTE_PREDICTION_ENABLED;
            this.numberOfPointsInLocationStorage = Coordinator.DEFAULT_POINTS_IN_LOCAL_STORAGE;
            this.numberOfTuplesInFilterStorage = Coordinator.DEFAULT_TUPLES_IN_FILTER_STORAGE;
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

        public NesCoordinatorBuilder locationUpdateInterval(int locationUpdateInterval) {
            this.locationUpdateInterval = locationUpdateInterval;
            return this;
        }

        public NesCoordinatorBuilder dynamicDuplicatesFilterEnabled(boolean dynamicDuplicatesFilterEnabled) {
            this.dynamicDuplicatesFilterEnabled = dynamicDuplicatesFilterEnabled;
            return this;
        }

        public NesCoordinatorBuilder routePredictionEnabled(boolean routePredictionEnabled) {
            this.routePredictionEnabled = routePredictionEnabled;
            return this;
        }

        public NesCoordinatorBuilder numberOfPointsInLocationStorage(int numberOfPointsInLocationStorage) {
            this.numberOfPointsInLocationStorage = numberOfPointsInLocationStorage;
            return this;
        }

        public NesCoordinatorBuilder numberOfTuplesInFilterStorage(int numberOfTuplesInFilterStorage) {
            this.numberOfTuplesInFilterStorage = numberOfTuplesInFilterStorage;
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
        protected int numberOfTuplesToProducePerBuffer;
        protected String physicalStreamName;

        protected String sourceConfig;
        protected SourceType sourceType;

        protected int coordinatorRestPort;
        protected boolean registerLocation;
        protected int locationUpdateInterval;
        protected int workerRange;

        private NesSourceBuilder(String name) {
            super(name);
            this.logicalStreamName = "default_logical";
            this.physicalStreamName = "default_physical";
            this.numberOfTuplesToProducePerBuffer = 0;
            this.sourceConfig = null;
            this.sourceType = SourceType.DefaultSource;
            this.coordinatorRestPort = Coordinator.DEFAULT_REST_PORT;
            this.registerLocation = false;
            this.locationUpdateInterval = 500;
            this.workerRange = 0;
        }

        public NesSourceBuilder logicalStreamName(String logicalStreamName) {
            this.logicalStreamName = logicalStreamName;
            return this;
        }

        public NesSourceBuilder numberOfTuplesToProducePerBuffer(int numberOfTuplesToProducePerBuffer) {
            this.numberOfTuplesToProducePerBuffer = numberOfTuplesToProducePerBuffer;
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

        public NesSourceBuilder coordinatorRestPort(int coordinatorRestPort) {
            this.coordinatorRestPort = coordinatorRestPort;
            return this;
        }

        public NesSourceBuilder registerLocation(boolean registerLocation) {
            this.registerLocation = registerLocation;
            return this;
        }

        public NesSourceBuilder locationUpdateInterval(int locationUpdateInterval) {
            this.locationUpdateInterval = locationUpdateInterval;
            return this;
        }

        public NesSourceBuilder workerRange(int workerRange) {
            this.workerRange = workerRange;
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

    public static class ZeroMQSourceBuilder extends NesSourceBuilder {

        protected String zeroMQHost;
        protected int zeroMQPort;

        private ZeroMQSourceBuilder(String name) {
            super(name);
            this.zeroMQHost = "127.0.0.1";
            this.zeroMQPort = 5555;
            this.sourceType = SourceType.ZMQSource;
        }

        public ZeroMQSourceBuilder zmqHost(String zmqHost) {
            this.zeroMQHost = zmqHost;
            return this;
        }

        public ZeroMQSourceBuilder zmqPort(int port) {
            this.zeroMQPort = port;
            return this;
        }

        /** From Parent **/

        public ZeroMQSourceBuilder coordinatorRestPort(int coordinatorRestPort) {
            this.coordinatorRestPort = coordinatorRestPort;
            return this;
        }

        public ZeroMQSourceBuilder registerLocation(boolean registerLocation) {
            this.registerLocation = registerLocation;
            return this;
        }

        public ZeroMQSourceBuilder locationUpdateInterval(int locationUpdateInterval) {
            this.locationUpdateInterval = locationUpdateInterval;
            return this;
        }

        public ZeroMQSourceBuilder workerRange(int workerRange) {
            this.workerRange = workerRange;
            return this;
        }

        public ZeroMQSourceBuilder logicalStreamName(String logicalStreamName) {
            this.logicalStreamName = logicalStreamName;
            return this;
        }

        public ZeroMQSourceBuilder numberOfTuplesToProducePerBuffer(int numberOfTuplesToProducePerBuffer) {
            this.numberOfTuplesToProducePerBuffer = numberOfTuplesToProducePerBuffer;
            return this;
        }

        public ZeroMQSourceBuilder physicalStreamName(String physicalStreamName) {
            this.physicalStreamName = physicalStreamName;
            return this;
        }

        public ZeroMQSourceBuilder parentId(int parentId) {
            this.parentId = parentId;
            return this;
        }

        public ZeroMQSourceBuilder dataPort(int dataPort) {
            this.dataPort = dataPort;
            return this;
        }

        public ZeroMQSourceBuilder rpcPort(int rpcPort) {
            this.rpcPort = rpcPort;
            return this;
        }

        public ZeroMQSource build() {
            return new ZeroMQSource(this);
        }
    }
}
