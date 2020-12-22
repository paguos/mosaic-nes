package stream.nebula.utils;

import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;

public class FieldExistenceCheckerUtil {
    public static boolean isPredicateFieldInLogicalStreamSchema(LogicalStream logicalStream, String seachField){
        // Check if the selected field is in the schema
        // Assume fieldName is NOT case sensitive
        // TODO: Check if fieldName in C++ code is case sensitive
        boolean isPredicateFieldInLogicalStreamFieldList = false;
        for(Field field: logicalStream.getFieldList()){
            isPredicateFieldInLogicalStreamFieldList |= field.getName().equalsIgnoreCase(seachField);
            if(isPredicateFieldInLogicalStreamFieldList){
                return true;
            }
        }
        return false;
    }
}
