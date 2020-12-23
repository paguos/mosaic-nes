package stream.nebula.exceptions;

public class RESTExecption extends Exception{
    public RESTExecption(int message) {
        super("Error while retrieving data from NES REST API: "+ message);
    }
}
