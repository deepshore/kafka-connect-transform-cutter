package de.deepshore.kafka.connect.transform.cutter;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;

import java.util.Map;


public class SkipEndConfig extends AbstractConfig {

    public static final String ENDSTRING_CONFIG = "endstring";
    private static final String ENDSTRING_DOC = "This is a setting important to my connector.";

    public final String endstring;

    public SkipEndConfig(Map<?, ?> originals) {
        super(config(), originals);
        this.endstring = this.getString(ENDSTRING_CONFIG);
    }

    public static ConfigDef config() {
        return new ConfigDef()
                .define(
                        ConfigKeyBuilder.of(ENDSTRING_CONFIG, Type.STRING)
                                .documentation(ENDSTRING_DOC)
                                .importance(Importance.HIGH)
                                .defaultValue("")
                                .build()
                );
    }
}
