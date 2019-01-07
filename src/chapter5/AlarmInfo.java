package chapter5;

/**
 * @author geyy
 * @version $Id: AlarmInfo.java, v 0.1 2019-01-07 15:59 geyy Exp $
 */
public class AlarmInfo {

    private String id ;

    private AlarmType type;

    private String extraInfo;

    public AlarmInfo(String id,AlarmType type){
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AlarmType getType() {
        return type;
    }

    public void setType(AlarmType type) {
        this.type = type;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}