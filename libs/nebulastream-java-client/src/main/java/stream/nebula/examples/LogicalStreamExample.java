package stream.nebula.examples;

import stream.nebula.exceptions.RESTExecption;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.runtime.NebulaStreamRuntime;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.queryinterface.Query;

import java.io.IOException;
import java.util.List;

/**
 * This example demonstrate how to utilize logical stream on the NES Java client. Particularly, it shows how to:
 * 1. Get the available logical stream (as Java object)
 * 2. Get list of logical stream name
 * 3. Get list of fields and its data type in a logical stream
 *
 * About Logical Stream
 * NES Java Client store abstraction of logical stream as java objects of class LogicalStream (org.nebula.stream.model
 * .LogicalStream). This object holds a String of name and a list of field of class Field (Field
 * ). Each field object holds a String of field name, a String of field c++ data type, and a class type of java counterpart
 * of the c++ data type.The logical stream object exposes getName() method to get the name of the logical stream and
 * getFields() method to retrieve the list of fields in the current logical stream object.
 *
 * The logical stream is used as input to from() method of an Query object. The subsequent operator applied to this
 * Query object will manipulate the data attached to this logical stream object. This will enable the InputQuery
 * object to perform pre-check against field existence and field type when comparing field to a value.
 */

public class LogicalStreamExample {
    public static void main(String[] args) throws UnknownDataTypeException, IOException, RESTExecption {
        // Configure network connection to NES REST server
        NebulaStreamRuntime ner = NebulaStreamRuntime.getRuntime();
        ner.getConfig().setHost("localhost")
                .setPort("8081");

        // Get a list of available logical stream
        List<LogicalStream> availableLogicalStream = ner.getAvailableLogicalStreams();

        // Printing the name of available logical streams
        availableLogicalStream.forEach(logicalStream -> System.out.println(logicalStream.getName()));
        System.out.println("============================================================");
        // Picking one of the available logical stream
        LogicalStream defaultLogical = availableLogicalStream.get(0);

        // Getting list of fields in the defaultLogical logical stream
        List<Field> fieldInDefaultLogical = defaultLogical.getFieldList();

        // Printing field and its type in the default logical stream
        fieldInDefaultLogical.forEach(field -> System.out.println(field.getName() + " | C++_dtype:" + field.getType()
                + " | Java_dtype:" + field.getJavaDataType()));
        System.out.println("============================================================");

        // Attaching logical stream to a Query and add print() method.
        Query query = new Query().from(defaultLogical)
                .print();

        // Print out the generated C++ code
        System.out.println(query.generateCppCode());

    }

}
