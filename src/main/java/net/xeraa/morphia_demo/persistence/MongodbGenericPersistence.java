package net.xeraa.morphia_demo.persistence;

import net.xeraa.morphia_demo.config.MongoDB;
import net.xeraa.morphia_demo.entities.BaseEntity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

/**
 * The generic Persistence implementation, showing how to do persists, various queries,...
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

}
