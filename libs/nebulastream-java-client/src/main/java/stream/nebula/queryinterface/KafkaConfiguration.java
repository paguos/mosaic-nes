package stream.nebula.queryinterface;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfiguration {
    HashMap<String, Object> configurations;

    public KafkaConfiguration() {
        this.configurations = new HashMap<String, Object>();
    }

    public KafkaConfiguration set(String key, Object value){
        this.configurations.put(key, value);
        return this;
    }
    
    public String build() {
        String configurationString = "{";

        for (Map.Entry<String, Object> configurationItem: this.configurations.entrySet()) {
            Object value;
            if(configurationItem.getValue() instanceof String){
                value = "\""+configurationItem.getValue()+"\"";
            } else {
                value = configurationItem.getValue();
            }
            configurationString += "{\""+configurationItem.getKey()+"\""
                    + "," + (value)
                    + "},";
        }
        configurationString += "}";
        return configurationString;
    }
}
