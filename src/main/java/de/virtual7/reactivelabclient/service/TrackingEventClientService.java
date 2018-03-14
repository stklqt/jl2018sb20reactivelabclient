package de.virtual7.reactivelabclient.service;

import de.virtual7.reactivelabclient.event.TrackingEvent;
import de.virtual7.reactivelabclient.repository.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mihai.dobrescu
 */
@Service
public class TrackingEventClientService {

    @Autowired
    private TrackingEventRepository repository;

    @Autowired
    CassandraOperations cassandraOperations;


    public void saveEvent(TrackingEvent event) {
        repository.save(event);
    }


    public void saveBulkEvents(List<TrackingEvent> buffer) {
        repository.saveAll(buffer);
        /*
        cassandraOperations
                .batchOps()
                .insert(buffer,
                        WriteOptions.builder().ttl(60).build())
                .execute();*/
    }
}
