package chapter4;

import java.util.concurrent.Callable;

/**
 * @author geyy
 * @version $Id: GuardedAction.java, v 0.1 2019-01-02 10:44 geyy Exp $
 */
public abstract class GuardedAction<V> implements Callable<V> {
    protected final Predicate guard;

    public GuardedAction(Predicate guard) {
        this.guard = guard;
    }
}