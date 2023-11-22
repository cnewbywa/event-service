package com.cnewbywa.events.test.resources;

import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class CustomMongoResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

	private Optional<String> containerNetworkId;
    private GenericContainer container;
	
	@Override
	public void setIntegrationTestContext(DevServicesContext context) {
		containerNetworkId = context.containerNetworkId();
	}

	@Override
	public Map<String, String> start() {
		container = new GenericContainer(DockerImageName.parse("mongo:7.0"))
				.withExposedPorts(47017)
				.withEnv("MONGO_INITDB_DATABASE", "events")
				.withCommand("mongod --port 47017")
	            .withCopyFileToContainer(MountableFile.forClasspathResource("./init-it-schema.js"), "/docker-entrypoint-initdb.d/init-script.js");
		
		containerNetworkId.ifPresent(container::withNetworkMode);
		
		container.start();
		
		String host;
		
		if (containerNetworkId.isPresent()) {
			host = container.getCurrentContainerInfo().getConfig().getHostName();
		} else {
			host = container.getHost();
		}
		
		String connectionString = "mongodb://%s:%s/events";
		
		return Map.of(
	            "quarkus.mongodb.connection-string", String.format(connectionString, host, container.getFirstMappedPort()));
	}

	@Override
	public void stop() {
		container.stop();
	}
}
