/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database;

import systems.reformcloud.utility.future.Task;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class Database<K, I, V> implements Serializable {

    public abstract V insert(K key, V value);

    public abstract Task<V> insertAsync(K key, V value);

    public abstract Boolean remove(K key);

    public abstract Task<Boolean> removeAsync(K key);

    public abstract Boolean update(K key, V newValue);

    public abstract Task<Boolean> updateAsync(K key, V newValue);

    public abstract Boolean contains(K key);

    public abstract Task<Boolean> containsAsync(K key);

    public abstract V get(K key);

    public abstract Task<V> getAsync(K key);

    public abstract K getID(I identifier);

    public abstract Task<K> getIDAsync(I identifier);

    public abstract Void forEach(Consumer<V> consumer);

    public abstract Task<Void> forEachAsync(Consumer<V> consumer);
}
