package net.xeraa.morphia_demo.test;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
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
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * If there is no MongoDB process available, start one with Flapdoodle.
 */
public class BaseTest {

  private static final Logger LOG = Logger.getLogger(BaseTest.class.getName());

  private static IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
      .defaultsWithLogger(Command.MongoD, LOG) // Log MongoDB's output to the console
      .artifactStore(
          new ArtifactStoreBuilder().defaults(Command.MongoD).executableNaming(new UserTempNaming())) // Give the executable a static name
              .build();
  private static final MongodStarter starter = MongodStarter.getInstance(runtimeConfig);
  private static MongodExecutable _mongodExe;
  private static MongodProcess _mongod;

  protected Persistence persistence;
  protected GenericPersistence genericPersistence;

  static {
    try {
      _mongodExe = starter.prepare(new MongodConfigBuilder()
                                       .version(Version.Main.PRODUCTION).net(new Net(MongoDB.DB_PORT, false)).build());
    } catch (IOException e) {
      LOG.fine("Port " + MongoDB.DB_PORT + " is already in use. If you are using a standalone MongoDB Server, this is the intended behavior.");
      e.printStackTrace();
    }
    try {
      _mongod = _mongodExe.start();
    } catch (IOException e) {
      LOG.fine("Could not start the embedded MongoDB process. This should never happen.");
      e.printStackTrace();
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
    //_mongod.stop();
    //_mongodExe.stop();
  }
}
