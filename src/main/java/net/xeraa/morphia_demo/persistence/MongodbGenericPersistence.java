package net.xeraa.morphia_demo.persistence;

import net.xeraa.morphia_demo.config.MongoDB;
import net.xeraa.morphia_demo.entities.AutoIncrementEntity;
import net.xeraa.morphia_demo.entities.BaseEntity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/**
 * The generic Persistence implementation, showing how to do persists, count entities, return a
 * specific one, and generate unique IDs.
 */
public class MongodbGenericPersistence {

  private final Datastore mongoDatastore;

  public MongodbGenericPersistence() {
    mongoDatastore = MongoDB.instance().getDatabase();
  }

  public <E extends BaseEntity> ObjectId persist(E entity) {
    mongoDatastore.save(entity);
    return entity.getId();
  }

  public <E extends BaseEntity> long count(Class<E> clazz) {
    if (clazz == null) {
      return 0;
    }

    return mongoDatastore.find(clazz).countAll();
  }

  public <E extends BaseEntity> E get(Class<E> clazz, final ObjectId id) {
    if ((clazz == null) || (id == null)) {
      return null;
    }

    return mongoDatastore.find(clazz).field("id").equal(id).get();
  }

  public long generateAutoIncrement(final String key, final long minimumValue){

    // Get the given key from the auto increment entity and try to increment it.
    final Query<AutoIncrementEntity> query = mongoDatastore.find(AutoIncrementEntity.class)
        .field("_id").equal(key);
    final UpdateOperations<AutoIncrementEntity> update = mongoDatastore
        .createUpdateOperations(AutoIncrementEntity.class).inc("value");
    AutoIncrementEntity autoIncrement = mongoDatastore.findAndModify(query, update);

    // If none is found, we need to create one for the given key.
    if (autoIncrement == null) {
      autoIncrement = new AutoIncrementEntity(key, minimumValue);
      mongoDatastore.save(autoIncrement);
    }
    return autoIncrement.getValue();
  }

}
