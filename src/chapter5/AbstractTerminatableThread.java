package chapter5;

/**
 * 可停止的抽象线程
 * 模式角色：Two-phaseTermination.AbstractTerminatableThread
 * @author geyy
 * @version $Id: AbstractTerminatableThread.java, v 0.1 2019-01-07 15:06 geyy Exp $
 */
public abstract class AbstractTerminatableThread extends Thread implements Terminatable  {

    //模式角色：Two-phaseTermination.TerminationToken
    public final TerminationToken terminationToken;

    public AbstractTerminatableThread(){
        this.terminationToken = new TerminationToken();
    }

    /**
     * 线程间共享的线程终止标志实例
     * @param terminationToken
     */
    public AbstractTerminatableThread(TerminationToken terminationToken){
        super();
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    /**
     * 留给子类实现其线程处理逻辑
     * @throws Exception
     */
    protected abstract void doRun() throws Exception;

    /**
     * 留给子类实现。用于实现线程停止后的一些清理动作。
     * @param cause
     */
    protected void doCleanup(Exception cause){
        //什么也不做
    }

    /**
     * 留给子类实现。用于执行线程停止所需的操作
     */
    protected void doTerminiate(){
        // 什么也不做
    }

    @Override
    public void run() {
        Exception ex = null;
        try{
            for(;;){
                //在执行线程的处理逻辑前先判断线程停止的标志
                if(terminationToken.isToShutdown()&&terminationToken.reservations.get() <=0){
                    break;
                }
                doRun();
            }
        }catch (Exception e){
            //使得线程能够响应interrupt调用二退出
            ex= e;
        }finally{
            try{
                doCleanup(ex);
            }finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    public void terminate(){
        terminationToken.setToShutdown(true);
        try{
            doTerminiate();
        }finally {
            //若无代理的任务，则试图强制终止线程
            if(terminationToken.reservations.get() <=0 ){
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUtilThreadTerminated){
        terminate();
        if(waitUtilThreadTerminated){
            try{
                this.join();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}