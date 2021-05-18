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

public class SkipStartTest {

  private SkipStart.Value transformValue;
  private SkipStart.Key transformKey;
  private String input;
  private String expected;


  @BeforeEach
  public void before() throws Exception {
    this.transformValue = new SkipStart.Value();
    this.transformValue.configure(
            ImmutableMap.of(SkipStartConfig.STARTSTRING_CONFIG, "start")
    );

    this.transformKey = new SkipStart.Key();
    this.transformKey.configure(
            ImmutableMap.of(SkipStartConfig.STARTSTRING_CONFIG, "start")
    );

    this.input = "prefix_start_start_postfix";
    this.expected = "start_start_postfix";
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
