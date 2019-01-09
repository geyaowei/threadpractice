package chapter6;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 数据同步模块入口类
 * @author geyy
 * @version $Id: DataSyncTak.java, v 0.1 2019-01-08 16:54 geyy Exp $
 */
public class DataSyncTak implements Runnable {
    private final Map<String,String> taskParmeters;

    public DataSyncTak(Map<String,String> taskParmeters){
        this.taskParmeters = taskParmeters;
    }

    @Override
    public void run() {
        String ftpServer = taskParmeters.get("server");
        String ftpUserName = taskParmeters.get("userName");
        String password = taskParmeters.get("password");

        //县初始化FTP客户端实例
        Future<FTPClientUtil> ftpClientUtilPromise = FTPClientUtil.newInstance(ftpServer,ftpUserName,password);

        //查询数据库生成本地文件
        generateFilesFromDB();

        FTPClientUtil ftpClientUtil = null;
        try{
            //获取初始化完毕的FTP客户端实例
            ftpClientUtil = ftpClientUtilPromise.get();
        }catch (InterruptedException e){
            ;
        }catch (ExecutionException e){
            throw new RuntimeException(e);
        }

        //上传文件
        uploadFiles(ftpClientUtil);

        //省略其他代码
    }

    private void generateFilesFromDB(){
        //省略其他代码
    }

    private void uploadFiles(FTPClientUtil ftpClientUtil){
        Set<File> files = retrieveGeneratedFiles();
        for(File file:files){
            try{
                ftpClientUtil.upload(file);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Set<File> retrieveGeneratedFiles(){
        Set<File> files = new HashSet<File>();
        // 。。。
        return  files;
    }
}