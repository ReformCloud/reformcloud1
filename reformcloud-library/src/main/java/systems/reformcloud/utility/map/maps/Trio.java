/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map.maps;

import systems.reformcloud.utility.map.MapUtility;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class Trio<F, S, T> implements Serializable {
    /**
     * The first value of the map
     */
    private F first;

    /**
     * The second value of the map
     */
    private S second;

    /**
     * The third value of the map
     */
    private T third;

    @java.beans.ConstructorProperties({"first", "second", "third"})
    public Trio(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Checks if a specific parameter is in the trio
     *
     * @param toFind The key which should be checked for
     * @param <V>    The type of the key
     * @return If the trio contains the key
     */
    public <V> boolean contains(V toFind) {
        return MapUtility.contains(this, toFind);
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public T getThird() {
        return this.third;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public void setThird(T third) {
        this.third = third;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Trio)) return false;
        final Trio<?, ?, ?> other = (Trio<?, ?, ?>) o;
        final Object this$first = this.getFirst();
        final Object other$first = other.getFirst();
        if (this$first == null ? other$first != null : !this$first.equals(other$first)) return false;
        final Object this$second = this.getSecond();
        final Object other$second = other.getSecond();
        if (this$second == null ? other$second != null : !this$second.equals(other$second)) return false;
        final Object this$third = this.getThird();
        final Object other$third = other.getThird();
        if (this$third == null ? other$third != null : !this$third.equals(other$third)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $first = this.getFirst();
        result = result * PRIME + ($first == null ? 43 : $first.hashCode());
        final Object $second = this.getSecond();
        result = result * PRIME + ($second == null ? 43 : $second.hashCode());
        final Object $third = this.getThird();
        result = result * PRIME + ($third == null ? 43 : $third.hashCode());
        return result;
    }

    public String toString() {
        return "Trio(first=" + this.getFirst() + ", second=" + this.getSecond() + ", third=" + this.getThird() + ")";
    }
}
