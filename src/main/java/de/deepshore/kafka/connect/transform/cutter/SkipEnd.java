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

@Title("SkipEnd")
@Description("This transformation will skip the end of a String or ByteArray.")
public class SkipEnd<R extends ConnectRecord<R>> extends BaseKeyValueTransformation<R> {
    SkipEndConfig config;

    protected SkipEnd(boolean isKey) {
        super(isKey);
    }

    @Override
    public ConfigDef config() {
        return SkipEndConfig.config();
    }

    @Override
    public void close() {

    }

    @Override
    protected SchemaAndValue processBytes(R record, Schema inputSchema, byte[] input) {
        return processString(record, inputSchema, new String(input));
    }

    @Override
    protected SchemaAndValue processString(R record, Schema inputSchema, String input) {
        final Schema outputSchema = inputSchema.STRING_SCHEMA;

        String output = input;

        if (config.endstring != null && !config.endstring.trim().isEmpty()) {
            try {
                output = truncateString(output, config.endstring);
            } catch (Exception e) {
                throw new DataException("Exception thrown while truncating string", e);
            }
        }

        return new SchemaAndValue(outputSchema, output);
    }

    @Override
    public void configure(Map<String, ?> map) {
        this.config = new SkipEndConfig(map);
    }

    /**
     * This implementation works against the key of the record.
     * @param <R>
     */
    public static class Key<R extends ConnectRecord<R>> extends SkipEnd<R> {
        public Key() {
            super(true);
        }
    }

    /**
     * This implementation works against the value of the record.
     * @param <R>
     */
    public static class Value<R extends ConnectRecord<R>> extends SkipEnd<R> {
        public Value() {
            super(false);
        }
    }

    private static String truncateString(String input, String endString) {
        int idx = input.lastIndexOf(endString);
        return input.substring(0, idx + endString.length());
    }
}