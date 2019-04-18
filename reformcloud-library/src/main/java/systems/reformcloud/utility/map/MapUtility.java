/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map;

import lombok.NonNull;
import systems.reformcloud.utility.map.maps.Double;
import systems.reformcloud.utility.map.maps.Trio;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author _Klaro | Pasqual K. / created on 31.03.2019
 */

public final class MapUtility implements Serializable {
    /**
     * Creates a new double by the given parameters
     *
     * @param t   The first value in the double
     * @param s   The second value in the double
     * @param <T> The type of the first value
     * @param <S> The type of the second value
     * @return The created double
     */
    @NonNull
    public static <T, S> Double<T, S> newDouble(T t, S s) {
        return new Double<>(t, s);
    }

    /**
     * Creates a new trio by the given parameters
     *
     * @param f         The first value in the trio
     * @param s         The second value in the trio
     * @param t         The third value in the trio
     * @param <F>       The type of the first value
     * @param <S>       The type of the second value
     * @param <T>       The type of the third value
     * @return The created trio
     */
    @NonNull
    public static <F, S, T> Trio<F, S, T> newTrio(F f, S s, T t) {
        return new Trio<>(f, s, t);
    }

    /**
     * Creates a new trio from an existing one
     *
     * @param in        The existing trio
     * @param <F>       The type of the first value
     * @param <S>       The type of the second value
     * @param <T>       The type of the third value
     * @return The cloned trio
     */
    @NonNull
    public static <F, S, T> Trio<F, S, T> fromExisting(Trio<F, S, T> in) {
        return newTrio(in.getFirst(), in.getSecond(), in.getThird());
    }

    /**
     * Checks if a specific double contains an object
     *
     * @param in        The double which should be checked
     * @param toFind    The object which should be checked for
     * @param <F>       The type of the first value
     * @param <S>       The type of the second value
     * @param <T>       The type of the item which should be found
     * @return If the double contains the item or not
     */
    @NonNull
    public static <F, S, T> boolean contains(Double<F, S> in, T toFind) {
        return in.getFirst().equals(toFind) || in.getSecond().equals(toFind);
    }

    /**
     * Checks if a specific trio contains an object
     *
     * @param in        The trio which should be checked
     * @param toFind    The object which should be checked for
     * @param <F>       The type of the first value
     * @param <S>       The type of the second value
     * @param <T>       The type of the third value
     * @param <V>       The type of the item which should be found
     * @return If the trio contains the item or not
     */
    @NonNull
    public static <F, S, T, V> boolean contains(Trio<F, S, T> in, V toFind) {
        return in.getFirst().equals(toFind) || in.getSecond().equals(toFind) || in.getThird().equals(toFind);
    }

    /**
     * Converts a double to a map
     *
     * @param in        The input double which should be converted
     * @param <F>       The type of the first value
     * @param <S>       The type of the second value
     * @return The created map
     */
    @NonNull
    public static <F, S> Map<F, S> convert(Double<F, S> in) {
        Map<F, S> out = new HashMap<>();
        out.put(in.getFirst(), in.getSecond());
        return out;
    }

    /**
     * Converts the double into an optional containing the created map
     *
     * @param in        The input double which should be converted
     * @param <F>       The type of the first value
     * @param <S>       The type of the second value
     * @return The created optional with the map
     */
    @NonNull
    public static <F, S> Optional<Map<F, S>> convertToOptionalMap(Double<F, S> in) {
        return Optional.of(convert(in));
    }

    /**
     * Converts a trio and double into a map containing them
     *
     * @param inTrio        The trio which should be added to the map
     * @param inDouble      The double which should be added to the map
     * @param <F>           The type of the first trio value
     * @param <S>           The type of the second trio value
     * @param <T>           The type of the third trio value
     * @param <V>           The type of the first double value
     * @param <X>           The type of the second double value
     * @return The created map containing the trio and double
     */
    @NonNull
    public static <F, S, T, V, X> Optional<Map<Trio<F, S, T>, Double<V, X>>> convertToOptionalMapWithTrioAndDouble(Trio<F, S, T> inTrio, Double<V, X> inDouble) {
        Map<Trio<F, S, T>, Double<V, X>> map = new HashMap<>();
        map.put(inTrio, inDouble);
        return Optional.of(map);
    }

    /**
     * Copies a list
     *
     * @param in        The list which should be copied
     * @param <F>       The type of the value in the list
     * @return The new list containing the values of the old one
     */
    @NonNull
    public static <F> List<F> copyOf(List<F> in) {
        return new ArrayList<>(in);
    }

    /**
     * Copies a map
     *
     * @param in        The map which should be copied
     * @param <F>       The type of the key in the map
     * @param <S>       The type of the value in the map
     * @return The new map containing the values of the old one
     */
    @NonNull
    public static <F, S> Map<F, S> copyOf(Map<F, S> in) {
        return new ConcurrentHashMap<>(in);
    }

    /**
     * Copies a set
     *
     * @param in        The set which should be copied
     * @param <F>       The type of the key in the set
     * @param <S>       The type of the value in the set
     * @return The new set containing the values of the old one
     */
    @NonNull
    public static <F, S> Set<Map.Entry<F, S>> copyOf(Set<Map.Entry<F, S>> in) {
        return new HashSet<>(in);
    }
}
