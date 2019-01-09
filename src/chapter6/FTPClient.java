package chapter6;

import java.io.InputStream;

/**
 * @author geyy
 * @version $Id: FTPClient.java, v 0.1 2019-01-08 16:19 geyy Exp $
 */
public class FTPClient {

    public final static String ASCII_FILE_TYPE = "";

    private String replyString;

    private int replyCode;

    private String fileType;

    public static boolean isConnected(){
        return Boolean.TRUE;
    }

    public boolean storeFile(String dirName, InputStream dataIn){
        return  true;
    }

    public void makeDirectory(String dirName){

    }

    public void disconnect(){

    }

    public boolean login(String userName,String password){
        return true;
    }

    public int cwd(String param){
        return 0;
    }

    public static void configure(FTPClientConfig config){

    }

    public static void connect(String ftpServer){

    }

    public String getReplyString() {
        return replyString;
    }

    public void setReplyString(String replyString) {
        this.replyString = replyString;
    }

    public int getReplyCode() {
        return replyCode;
    }

    public void setReplyCode(int replyCode) {
        this.replyCode = replyCode;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}