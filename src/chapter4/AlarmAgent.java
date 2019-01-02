package chapter4;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * 负责连接告警服务器，并发送告警信息至告警服务器
 * @author geyy
 * @version $Id: AlarmAgent.java, v 0.1 2019-01-02 13:26 geyy Exp $
 */
public class AlarmAgent {
    private volatile boolean connectedToServer = false;
    private final Predicate agentConnected = new Predicate() {
        @Override
        public boolean evaluate() {
            return  connectedToServer;
        }
    };

    //模式角色：GuardedSuspension.Blocker
    private final Blocker blocker = new ConditionVarBlocker();

    //心跳定时器
    private final Timer heartbeatTimer = new Timer(true);

    //省略其他代码

    /**
     * 发送告警信息
     * @param alarm
     * @throws Exception
     */
    public void sendAlarm(final AlarmInfo alarm) throws Exception{
        //可能需要等待，直到AlarmAgent连接上告警服务器（或者连接中断后重新连上服务器）
        //模式角色：GuardSuspension.GuardedAction
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            @Override
            public Void call() throws Exception {
                doSendAlarm(alarm);
                return null;
            }
        };
        blocker.callWithGuard(guardedAction);
    }

    //通过网络连接将告警信息发送至告警服务器
    private void  doSendAlarm(AlarmInfo alarm){
        //省略其他代码
        System.out.println("sending alarm "+alarm);
        //模拟发送告警至服务器的耗时
        try{
            Thread.sleep(50);
        }catch (Exception e){
            System.out.println("sending alarm exception"+e.toString());
        }
    }

    public void init(){
        // 省略其他代码

        //告警连接线程
        Thread connectingThread = new Thread(new ConnectingTask());
        connectingThread.start();
        heartbeatTimer.schedule(new HeartbeatTask(),60000,2000);
    }

    //
    protected void onConnected(){
        try{
            blocker.signalAfter(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    connectedToServer = true;
                    System.out.println("connect to server");
                    return Boolean.TRUE;
                }
            });
        }catch(Exception e){
            System.out.println("onConnected exception "+e.toString());
        }
    }

    protected void onDisconnected(){
        connectedToServer = false;
    }

    //负责与告警服务器建立网络连接
    private class ConnectingTask implements Runnable{

        @Override
        public void run() {
            //省略其他代码
            try{
                Thread.sleep(100);
            }catch(Exception e){
                System.out.println("ConnectingTask "+e.toString());
            }
            onConnected();
        }
    }

    /**
     * 心跳定时任务：自动检查与告警服务器的连接是否正常发现连接异常后自动重新连接
     */
    private class HeartbeatTask extends TimerTask{

        @Override
        public void run() {
            //省略其他代码
            if(!testConnection()){
                onDisconnected();
                reconnect();
            }
        }

        private boolean testConnection(){
            //省略其他代码
            return true;
        }

        private void reconnect(){
            ConnectingTask connectingThread = new ConnectingTask();

            //直接在心跳定时器执行
            connectingThread.run();
        }
    }


}