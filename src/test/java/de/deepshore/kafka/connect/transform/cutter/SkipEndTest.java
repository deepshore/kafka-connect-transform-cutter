package de.deepshore.kafka.connect.transform.cutter;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class SkipEndTest {

    private SkipEnd.Value transformValue;
    private SkipEnd.Key transformKey;
    private String input;
    private String expected;


    @BeforeEach
    public void before() throws Exception {
        this.transformValue = new SkipEnd.Value();
        this.transformValue.configure(
                ImmutableMap.of(SkipEndConfig.ENDSTRING_CONFIG, "end")
        );

        this.transformKey = new SkipEnd.Key();
        this.transformKey.configure(
                ImmutableMap.of(SkipEndConfig.ENDSTRING_CONFIG, "end")
        );

        this.input = "prefix_end_end_postfix";
        this.expected = "prefix_end_end";
    }

    @AfterEach
    public void after() {
        this.transformValue.close();
    }

    @Test
    public void apply_value_stringSchema() throws Exception {
        final ConnectRecord inputRecord = new SinkRecord(
                "topic",
                1,
                null,
                null,
                Schema.STRING_SCHEMA,
                this.input,
                new Date().getTime()
        );

        ConnectRecord record = this.transformValue.apply(inputRecord);
        Assertions.assertEquals(this.expected, record.value());
    }

    @Test
    public void apply_key_bytesSchema() throws Exception {
        byte[] inputBytes = this.input.getBytes();
        final ConnectRecord inputRecord = new SinkRecord(
                "topic",
                1,
                null,
                null,
                Schema.BYTES_SCHEMA,
                inputBytes,
                new Date().getTime()
        );

        ConnectRecord record = this.transformValue.apply(inputRecord);
        Assertions.assertEquals(this.expected, record.value());
    }

    @Test
    public void apply_key_stringSchema() throws Exception {
        final ConnectRecord inputRecord = new SinkRecord(
                "topic",
                1,
                Schema.STRING_SCHEMA,
                this.input,
                null,
                null,
                new Date().getTime()
        );

        ConnectRecord record = this.transformKey.apply(inputRecord);
        Assertions.assertEquals(this.expected, record.key());
    }

    @Test
    public void apply_value_bytesSchema() throws Exception {
        byte[] inputBytes = this.input.getBytes();
        final ConnectRecord inputRecord = new SinkRecord(
                "topic",
                1,
                Schema.BYTES_SCHEMA,
                inputBytes,
                null,
                null,
                new Date().getTime()
        );

        ConnectRecord record = this.transformKey.apply(inputRecord);
        Assertions.assertEquals(this.expected, record.key());
    }
}
