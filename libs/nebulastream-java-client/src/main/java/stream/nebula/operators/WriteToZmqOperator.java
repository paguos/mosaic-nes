package stream.nebula.operators;

public class WriteToZmqOperator extends Operator {
    String streamName;
    String host;
    int port;

    public WriteToZmqOperator(String streamName, String host, int port) {
        this.streamName = streamName;
        this.host = host;
        this.port = port;
    }

    @Override
    public String getCppCode() {
        throw new UnsupportedOperationException();
//        return ".writeToZmq("+this.streamName+",\""+this.host+"\","+ port +")";
    }
}
