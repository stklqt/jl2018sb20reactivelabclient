package de.virtual7.reactivelabclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

/**
 * Created by mihai.dobrescu
 */
@Configuration
//@EnableCassandraRepositories(basePackageClasses = TrackingEvent.class)
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.contactpoints}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private Integer port;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification.createKeyspace(keyspaceName)
                        .ifNotExists() //TODO damit geht nur einmal anlegen
                        .with(KeyspaceOption.DURABLE_WRITES, true)
                        .withSimpleReplication();
        return Collections.singletonList(specification);
    }

    @Override
    protected String getContactPoints() {
        return this.contactPoints;
    }

    @Override
    protected int getPort() {
        return this.port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {"de.virtual7.reactivelabclient.event"};
    }
}
