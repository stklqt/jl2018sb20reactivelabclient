package de.virtual7.reactivelabclient.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Created by mihai.dobrescu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("events")
public class TrackingEvent {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private Long eventID;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private TrackingEventType eventType;
    private BigDecimal eventValue;
    private Instant eventInstant;

}
