package chapter5;

/**
 * @author geyy
 * @version $Id: AlarmAgent.java, v 0.1 2019-01-07 15:51 geyy Exp $
 */
public class AlarmAgent {
    public void init(){
        System.out.print("告警开始...");
    }
    public void sendAlarm(AlarmInfo alarm){
        System.out.print("告警发送...");
    }
    public void disconnect(){
        System.out.print("关闭连接...");
    }
}