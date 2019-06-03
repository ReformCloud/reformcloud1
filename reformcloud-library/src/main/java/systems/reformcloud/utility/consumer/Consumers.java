/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.consumer;

import systems.reformcloud.utility.Require;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class Consumers implements Serializable {

    @FunctionalInterface
    public interface TriConsumer<V, T, U> {

        void accept(V v, T t, U u);

        default TriConsumer<V, T, U> andThen(TriConsumer<? super V, ? super T, ? super U> after) {
            Require.requireNotNull(after);

            return (v, l, r) -> {
                accept(v, l, r);
                after.accept(v, l, r);
            };
        }
    }

    @FunctionalInterface
    public interface QuadConsumer<V, T, U, X> {

        void accept(V v, T t, U u, X x);

        default QuadConsumer<V, T, U, X> andThen(
            QuadConsumer<? super V, ? super T, ? super U, ? super X> after) {
            Require.requireNotNull(after);

            return (v, t, u, x) -> {
                accept(v, t, u, x);
                after.accept(v, t, u, x);
            };
        }
    }
}
