package chapter3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author geyy
 * @version $Id: MMSCRouter.java, v 0.1 2018-12-28 14:24 geyy Exp $
 * 彩信中心路由规则管理器
 * 模式角色：ImmutableObject.ImmutableObject
 */
public final class MMSCRouter {
    /**
     * volatile修饰保证多线程下该变量的可见性
     */
    private static volatile MMSCRouter instance = new MMSCRouter();

    /**
     * 维护手机号前缀与彩信中心之间的映射关系
     */
    private final Map<String,MMSCInfo> routeMap;

    public MMSCRouter() {
        //将数据库表中的数据加载到内存，存为map
        this.routeMap = MMSCRouter.retrieveRouteMapFromDB();
    }

    private static Map<String,MMSCInfo> retrieveRouteMapFromDB(){
        Map<String,MMSCInfo> map = new HashMap<String,MMSCInfo>();
        //省略具体操作
        return map;
    }

    private static MMSCRouter getInstance(){
        return instance;
    }

    /**
     *根据手机号码前缀获得彩信中心信息
     * @param msisdnPrefix 手机号码前缀
     * @return  彩信中心信息
     */
    public MMSCInfo getMMSC(String msisdnPrefix){
        return routeMap.get(msisdnPrefix);
    }

    /**
     * 将当前的MMSCRouter的实例更新为指定的实例
     * @param newInstance 新的MMSCRouter实例
     */
    public static void setInstance(MMSCRouter newInstance){
        instance = newInstance;
    }

    public static Map<String,MMSCInfo> deepCopy(Map<String,MMSCInfo> m){
        Map<String,MMSCInfo> result = new HashMap<String,MMSCInfo>();
        for(String key : m.keySet()){
            result.put(key,new MMSCInfo(m.get(key)));
        }
        return result;
    }

    public Map<String,MMSCInfo> getRouteMap(){
        //做防御性复制
        return Collections.unmodifiableMap(deepCopy(routeMap));
    }
}