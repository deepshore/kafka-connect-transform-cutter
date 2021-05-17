package de.deepshore.kafka.connect.transform.cutter;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;

import java.util.Map;


public class SkipStartConfig extends AbstractConfig {

  public static final String STARTSTRING_CONFIG = "startstring";
  private static final String STARTSTRING_DOC = "This is a setting important to my connector.";

  public final String startstring;

  public SkipStartConfig(Map<?, ?> originals) {
    super(config(), originals);
    this.startstring = this.getString(STARTSTRING_CONFIG);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(
            ConfigKeyBuilder.of(STARTSTRING_CONFIG, Type.STRING)
                .documentation(STARTSTRING_DOC)
                .importance(Importance.HIGH)
                .defaultValue("")
                .build()
        );
  }
}
