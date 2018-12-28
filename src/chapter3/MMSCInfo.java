package chapter3;

/**
 * @author geyy
 * @version $Id: MMSCInfo.java, v 0.1 2018-12-28 14:17 geyy Exp $
 * 彩信中心信息
 * 模式角色：Immutable.Immutable
 */
public final class MMSCInfo {
    /**
     *设备编号
     */
    private final String deviceId;

    /**
     * 彩信中心url
     */
    private final String url;

    /**
     * 该彩信中心允许的最大附件大小
     */
    private final int maxAttachmentSizeInBytes;

    public MMSCInfo(String deviceId, String url, int maxAttachmentSizeInBytes) {
        this.deviceId = deviceId;
        this.url = url;
        this.maxAttachmentSizeInBytes = maxAttachmentSizeInBytes;
    }

    public MMSCInfo(MMSCInfo prototype){
        this.deviceId = prototype.deviceId;
        this.url = prototype.url;
        this.maxAttachmentSizeInBytes = prototype.maxAttachmentSizeInBytes;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUrl() {
        return url;
    }

    public int getMaxAttachmentSizeInBytes() {
        return maxAttachmentSizeInBytes;
    }
}