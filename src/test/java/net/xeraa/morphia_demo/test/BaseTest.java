package net.xeraa.morphia_demo.test;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.UserTempNaming;

import net.xeraa.morphia_demo.config.MongoDB;
import net.xeraa.morphia_demo.persistence.GenericPersistence;
import net.xeraa.morphia_demo.persistence.MongodbGenericPersistence;
import net.xeraa.morphia_demo.persistence.MongodbPersistence;
import net.xeraa.morphia_demo.persistence.Persistence;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Handle the setup and teardown of the database.
 */
public class BaseTest {

  private static final Logger LOG = Logger.getLogger(BaseTest.class.getName());

  // Properties of the embedded MongoDB process.
  private static IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
      .defaultsWithLogger(Command.MongoD, LOG) // Log MongoDB's output to the console
      .artifactStore(
          new ArtifactStoreBuilder().defaults(Command.MongoD).executableNaming(new UserTempNaming())) // Give the executable a static name
              .build();
  private static final MongodStarter starter = MongodStarter.getInstance(runtimeConfig);
  private static MongodExecutable _mongodExecutable;

  // Persistence classes used in the tests.
  protected Persistence persistence;
  protected GenericPersistence genericPersistence;

  /**
   * Check if the MongoDB port is already in use, then there is nothing to do.
   * If it is still available, start an embedded MongoDB process with the Flapdoodle library.
   */
  static {
    try (Socket ignored = new Socket("127.0.0.1", MongoDB.DB_PORT)) {
      LOG.fine("Port " + MongoDB.DB_PORT + " is already in use. If you are using a standalone MongoDB Server, this is the intended behavior.");
    } catch (IOException ignored) {
      try {
        LOG.info("Trying to start embedded MongoDB.");
        _mongodExecutable = starter.prepare(new MongodConfigBuilder()
                                         .version(Version.Main.PRODUCTION)
                                         .net(new Net(MongoDB.DB_PORT, false)).build());
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        _mongodExecutable.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Get our persistence implementation and ensure it's not null. Were not inserting any test data
   * as there is no dataset we can easily use for all test cases. We need both MongodbPersistence
   * for clearData() and MongodbGenericPersistence for more specific queries.
   */
  @Before
  public void setUp() throws Exception {
    persistence = new MongodbPersistence();
    assertNotNull(persistence);
    genericPersistence = new MongodbGenericPersistence();
    assertNotNull(genericPersistence);
  }

  /**
   * After finishing the test, clean up the database. This is important to allow multiple test runs
   * in combination with unique key constraints.
   */
  @After
  public void tearDown() throws Exception {
    persistence.clearData();
  }
}
