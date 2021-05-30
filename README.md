# de.deepshore.kafka.connect.transform.cutter

Das Projekt enthält zwei verschiedene Single-Message-Tranformatoren, mit denen sichergestellt werden kann,
dass die Values bzw. Keys einzelner Kafka-Messages bei der Verarbeitung durch einen Kafka-Connector
jeweils erst ab einer definierten Startsequenz bzw. bis nach einer definierten Endsequenz berücksichtigt werden.
Zeichenfolgen könne vor der besagten Startsequenz bzw. nach der besagten Endsequenz de facto abgeschnitten werden.

Beim Erzeugen des Projektes wurde auf [https://github.com/jcustenborder/kafka-connect-archtype](https://github.com/jcustenborder/kafka-connect-archtype) zurückgegriffen.

## Package bauen

Ein Build des Packages kann wie folgt erzeugt werden:

```bash
mvn package
```

## Tests ausführen

Die Unit-Tests können wie folgt ausgeführt werden:

```bash
mvn test
```

## SkipStart

### Funktionsweise

Eine Input-Message `prefix_start_start_postfix` wird mit einer Startsequenz `start` in eine Output-Message `start_start_postfix` transformiert.
Fehlt die Startsequenz in einer Nachricht, wird ein Fehler ausgeworfen und der Connector beendet.

### Konfiguration Standalone

Im Standalone-Fall kann die oben beschriebene `SkipStart`-Transformation mit der folgenden Konfiguration erzielt werden:

```properties
transforms=cutter
transforms.cutter.type=de.deepshore.kafka.connect.transform.cutter.SkipStart$Value
transforms.cutter.startstring=start
```

### Konfiguration Distributed

Im Distributed-Fall ergibt sich entsprechend die folgende Konfiguration:

```
"transforms": "cutter",
"transforms.cutter.type": "de.deepshore.kafka.connect.transform.cutter.SkipStart$Value",
"transforms.cutter.startstring": "start"
```

## SkipEnd

### Funktionsweise

Eine Input-Message `prefix_end_end_postfix` wird mit einer Endsequenz `end` in eine Output-Message `prefix_end_end` transformiert.
Fehlt die Startsequenz in einer Nachricht, wird ein Fehler ausgeworfen und der Connector beendet.

### Konfiguration Standalone

Im Standalone-Fall kann die oben beschriebene `SkipEnd`-Transformation mit der folgenden Konfiguration erzielt werden:

```properties
transforms=cutter
transforms.cutter.type=de.deepshore.kafka.connect.transform.cutter.SkipEnd$Value
transforms.cutter.endstring=end
```

### Konfiguration Distributed

Im Distributed-Fall ergibt sich entsprechend die folgende Konfiguration:

```
"transforms": "cutter",
"transforms.cutter.type": "de.deepshore.kafka.connect.transform.cutter.SkipEnd$Value",
"transforms.cutter.endstring": "end"
```
