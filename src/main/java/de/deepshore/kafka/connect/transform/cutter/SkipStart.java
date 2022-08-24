package de.deepshore.kafka.connect.transform.cutter;

import com.github.jcustenborder.kafka.connect.utils.config.Description;
import com.github.jcustenborder.kafka.connect.utils.config.Title;
import com.github.jcustenborder.kafka.connect.utils.transformation.BaseKeyValueTransformation;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.DataException;

import java.util.Map;

@Title("SkipStart")
@Description("This transformation will skip the beginning of a String or ByteArray.")
public class SkipStart<R extends ConnectRecord<R>> extends BaseKeyValueTransformation<R> {
  SkipStartConfig config;

  protected SkipStart(boolean isKey) {
    super(isKey);
  }

  @Override
  public ConfigDef config() {
    return SkipStartConfig.config();
  }

  @Override
  public void close() {
    //noop
  }

  @Override
  protected SchemaAndValue processBytes(R rec, Schema inputSchema, byte[] input) {
    return processString(rec, inputSchema, new String(input));
  }

  @Override
  protected SchemaAndValue processString(R rec, Schema inputSchema, String input) {
    final Schema outputSchema = inputSchema.STRING_SCHEMA;

    String output = input;

    if (config.startstring != null && !config.startstring.trim().isEmpty()) {
      try {
        output = truncateString(output, config.startstring);
      } catch (Exception e) {
        throw new DataException("Exception thrown while truncating string", e);
      }
    }

    return new SchemaAndValue(outputSchema, output);
  }

  @Override
  public void configure(Map<String, ?> map) {
    this.config = new SkipStartConfig(map);
  }

  /**
   * This implementation works against the key of the record.
   * @param <R>
   */
  public static class Key<R extends ConnectRecord<R>> extends SkipStart<R> {
    public Key() {
      super(true);
    }
  }

  /**
   * This implementation works against the value of the record.
   * @param <R>
   */
  public static class Value<R extends ConnectRecord<R>> extends SkipStart<R> {
    public Value() {
      super(false);
    }
  }

  private static String truncateString(String input, String startString) {
    int idx = input.indexOf(startString);
    return input.substring(idx);
  }
}