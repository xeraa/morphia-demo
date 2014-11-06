package net.xeraa.morphia_demo.persistence;

import net.xeraa.morphia_demo.entities.BaseEntity;

import org.bson.types.ObjectId;

/**
 * The generic Persistence interface - in our specific case this will be implemented by
 * MongodbGenericPersistence.
 */
public interface GenericPersistence {

  <E extends BaseEntity> ObjectId persist(E entity);

  <E extends BaseEntity> long count(Class<E> clazz);

  <E extends BaseEntity> E get(Class<E> clazz, ObjectId id);

}
