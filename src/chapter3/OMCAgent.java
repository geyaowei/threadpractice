package chapter3;

/**
 * @author geyy
 * @version $Id: OMCAgent.java, v 0.1 2018-12-28 14:56 geyy Exp $
 * 与运维中心对接的类
 * 模式角色：ImmutableObject.ImmutableObject
 */
public class OMCAgent extends Thread {

    @Override
    public void run() {
        boolean isTableModifyicationMsg=false;
        String updatedTableName=null;
        while(true){
            //省略其他代码
            /**
             * 从于OMC连接的socket中读取消息并解析，解析到数据表更新消息后，重置MMSCRouter实例
             */
            if(isTableModifyicationMsg){
                if("MMSCInfo".equals(updatedTableName)){
                    MMSCRouter.setInstance(new MMSCRouter());
                }
            }
        }
    }
}