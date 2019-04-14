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
    @NonNull
    public static <T, S> Double<T, S> newDouble(T t, S s) {
        return new Double<>(t, s);
    }

    @NonNull
    public static <F, S, T> Trio<F, S, T> newTrio(F f, S s, T t) {
        return new Trio<>(f, s, t);
    }

    @NonNull
    public static <F, S, T> Trio<F, S, T> fromExisting(Trio<F, S, T> in) {
        return newTrio(in.getFirst(), in.getSecond(), in.getThird());
    }

    @NonNull
    public static <F, S, T> boolean contains(Double<F, S> in, T toFind) {
        return in.getFirst().equals(toFind) || in.getSecond().equals(toFind);
    }

    @NonNull
    public static <F, S, T, V> boolean contains(Trio<F, S, T> in, V toFind) {
        return in.getFirst().equals(toFind) || in.getSecond().equals(toFind) || in.getThird().equals(toFind);
    }

    @NonNull
    public static <F, S> Map<F, S> convert(Double<F, S> in) {
        Map<F, S> out = new HashMap<>();
        out.put(in.getFirst(), in.getSecond());
        return out;
    }

    @NonNull
    public static <F, S> Optional<Map<F, S>> convertToOptionalMap(Double<F, S> in) {
        return Optional.of(convert(in));
    }

    @NonNull
    public static <F, S, T, V, X> Optional<Map<Trio<F, S, T>, Double<V, X>>> convertToOptionalMapWithTrioAndDouble(Trio<F, S, T> inTrio, Double<V, X> inDouble) {
        Map<Trio<F, S, T>, Double<V, X>> map = new HashMap<>();
        map.put(inTrio, inDouble);
        return Optional.of(map);
    }

    @NonNull
    public static <F> List<F> copyOf(List<F> in) {
        return new ArrayList<>(in);
    }

    @NonNull
    public static <F, S> Map<F, S> copyOf(Map<F, S> in) {
        return new ConcurrentHashMap<>(in);
    }
}
