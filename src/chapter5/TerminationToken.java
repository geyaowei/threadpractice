package chapter5;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程停止标志
 * @author geyy
 * @version $Id: TerminationToken.java, v 0.1 2019-01-07 14:53 geyy Exp $
 */
public class TerminationToken {
    //使用volatile 修饰，以保证无须显式锁的情况下该标量的内存可见性
    protected volatile boolean toShutdown = false;
    public final AtomicInteger reservations = new AtomicInteger(0);

    /**
     * 在多个可停止线程实例共享一个TerminationToken实例的情况下，该队列用于记录那些共享TerminationToken实例的可停止线程，以便尽可能减少锁的使用的情况下，实现这些线程的停止
     */
    private final Queue<WeakReference<Terminatable>> coordinatedThreads;

    public TerminationToken(){
        coordinatedThreads = new ConcurrentLinkedDeque<WeakReference<Terminatable>>();
    }

    public boolean isToShutdown(){
        return toShutdown;
    }
    protected void setToShutdown(boolean toShutdown){
        this.toShutdown = true;
    }

    protected void register(Terminatable thread){
        coordinatedThreads.add(new WeakReference<Terminatable>(thread));
    }

    protected void notifyThreadTermination(Terminatable thread){
        WeakReference<Terminatable> wrThread;
        Terminatable otherThread;
        while(null != (wrThread = coordinatedThreads.poll())){
            otherThread = wrThread.get();
            if(null != otherThread&&otherThread != thread){
                otherThread.terminate();
            }
        }
    }
}