package chapter5;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模式角色：Two-phaseTermination.ConcreteTerminatableThread
 * @author geyy
 * @version $Id: AlarmSendingThread.java, v 0.1 2019-01-07 14:34 geyy Exp $
 */
public class AlarmSendingThread extends AbstractTerminatableThread {

    private final AlarmAgent alarmAgent = new AlarmAgent();

    //告警队列
    private final BlockingQueue<AlarmInfo> alarmQueue;
    private final ConcurrentMap<String,AtomicInteger> submittedAlarmRegistry;

    public AlarmSendingThread(){
        this.alarmQueue = new ArrayBlockingQueue<AlarmInfo>(100);
        submittedAlarmRegistry = new ConcurrentHashMap<String, AtomicInteger>();
        alarmAgent.init();
    }

    @Override
    protected void doRun() throws Exception {
    AlarmInfo alarm;
    alarm = alarmQueue.take();
    terminationToken.reservations.decrementAndGet();
    try{
        //将告警信息发送至告警服务器
        alarmAgent.sendAlarm(alarm);
    }catch(Exception e){
        e.printStackTrace();
    }
        /**
         * 处理回复告警：将响应的顾航告警从注册表中删除，使得响应故障回复后若再次出现相同故障，该故障信息能够上传至服务器
         */
    if(AlarmType.RESUME == alarm.getType()){
        String key = AlarmType.FAULT.toString()+":"+alarm.getId()+"@"+alarm.getExtraInfo();
        submittedAlarmRegistry.remove(key);
        key = AlarmType.FAULT.toString()+":"+alarm.getId()+"@"+alarm.getExtraInfo();
        submittedAlarmRegistry.remove(key);
    }
    }
    public int sendAlarm(final AlarmInfo alarmInfo){
        AlarmType type = alarmInfo.getType();
        String id = alarmInfo.getId();
        String extraInfo = alarmInfo.getExtraInfo();
        if(terminationToken.isToShutdown()){
            //记录告警
            System.err.println("rejected alarm:"+id+"，"+extraInfo);
            return -1;
        }
        int duplicateSubmissionCount = 0;
        try{
            AtomicInteger prevSubmittedCounter;
            prevSubmittedCounter = submittedAlarmRegistry.putIfAbsent(type.toString()+':'+id+'@'+extraInfo,new AtomicInteger(0));
            if(null ==prevSubmittedCounter){
                terminationToken.reservations.incrementAndGet();
                alarmQueue.put(alarmInfo);
            }else{
                //故障未回复,不用重复发送告警信息给服务器，故仅增加计数
                duplicateSubmissionCount = prevSubmittedCounter.incrementAndGet();
            }
        }catch(Throwable t){
            t.printStackTrace();
        }
        return duplicateSubmissionCount;
    }

    @Override
    protected void doCleanup(Exception exp) {
        if(null != exp && !(exp instanceof InterruptedException)){
            exp.printStackTrace();
        }
        alarmAgent.disconnect();
    }
}