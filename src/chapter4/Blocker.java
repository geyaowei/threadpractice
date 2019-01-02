package chapter4;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.Callable;

/**
 * @author geyy
 * @version $Id: Blocker.java, v 0.1 2019-01-02 10:46 geyy Exp $
 */
public interface Blocker {
    /**
     * 在保护条件成立时执行目标动作，否则阻塞当前线程,直到保护条件成立
     * @param guardedAction 带保护条件的目标动作
     * @param <V>
     * @return
     * @throws Exception
     */
    <V> V callWithGuard(GuardedAction<V> guardedAction) throws Exception;

    /**
     * 执行stateOperation所指定操作后，决定是否唤醒本Bloker所暂挂的所有线程中的一个线程
     * @param stateOperation 更改状态的操作，其call方法的返回值为true时，该方法才会唤醒被暂挂的线程
     * @throws Exception
     */
    void signalAfter(Callable<Boolean> stateOperation) throws Exception;

    void signal() throws InterruptedException;

    /**
     * 执行stateOperation所指定操作后，决定是否唤醒本Bloker所暂挂的所有线程
     * @param stateOperation 更改状态的操作，其call方法的返回值为true时，该方法才会唤醒被暂挂的线程
     * @throws Exception
     */
    void broadcastAfter(Callable<Boolean> stateOperation) throws Exception;

}