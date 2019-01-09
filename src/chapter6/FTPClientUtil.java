package chapter6;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *
 * //模式角色：Promise.Promisor·Promise.Result
 * @author geyy
 * @version $Id: FTPClientUtil.java, v 0.1 2019-01-08 16:02 geyy Exp $
 */
public class FTPClientUtil {
    private final FTPClient ftp= new FTPClient();

    private final Map<String,Boolean> dirCreateMap = new HashMap<String,Boolean>();

    private FTPClientUtil(){

    }

    //模式角色: Promise.Promisor.compute
    public static Future<FTPClientUtil> newInstance(final String ftpServer,
        final String userName,final String password){
        Callable<FTPClientUtil> callable = new Callable<FTPClientUtil>() {
            @Override
            public FTPClientUtil call() throws Exception {
                FTPClientUtil self =new FTPClientUtil();
                self.init(ftpServer,userName,password);
                return self;
            }
        };

        //task 相当于模式角色：Promise.Promise
        final FutureTask<FTPClientUtil> task = new FutureTask<FTPClientUtil>(callable) ;

        /**
         * 下面这行代码与本案例的时机代码并不一致，这是为了方便。下面新建的线程相当于模式角色：Promise.TaskExecutor
         */
        new Thread(task).start();
        return task;
    }
    private void init(String ftpServer,String userName,String password)throws Exception{
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        int reply;
        ftp.connect(ftpServer);

        System.out.println(ftp.getReplyString());

        reply = ftp.getReplyCode();

        if(!FTPReply.isPositiveCompletion(reply)){
            ftp.disconnect();
            throw new RuntimeException("FTP server refused connection.");
        }
        boolean isOK = ftp.login(userName,password);
        if(isOK){
            System.out.println(ftp.getReplyString());
        }else{
            throw new RuntimeException("Failed to login."+ftp.getReplyString());
        }
        reply = ftp.cwd("~/subspsync");
        if(!FTPReply.isPositiveCompletion(reply)){
            ftp.disconnect();
            throw new RuntimeException("Failed to change working directory.reply:"+reply);
        }else{
            System.out.println(ftp.getReplyString());
        }
        ftp.setFileType(ftp.ASCII_FILE_TYPE);
    }

    public void upload(File file) throws Exception{
        InputStream dataIn = new BufferedInputStream(new FileInputStream(file),1024 * 8);
        boolean isOK;
        String dirName = file.getParentFile().getName();
        String fileName = dirName+'/'+file.getName();
        ByteArrayInputStream checkFileInputStream = new ByteArrayInputStream("".getBytes());
        try{
            if(!dirCreateMap.containsKey(dirName)){
                ftp.makeDirectory(dirName);
                dirCreateMap.put(dirName,null);
            }
            try{
                isOK = ftp.storeFile(fileName,dataIn);
            }catch (Exception e){
                throw new RuntimeException("Failed to upload "+file,e);
            }
            if(isOK){
                ftp.storeFile(fileName+ ".c",checkFileInputStream);
            }else{
                throw new RuntimeException("Failed to upload "+file+",reply:"+","+ftp.getReplyString());
            }
        }finally {
            dataIn.close();
        }
    }

    public void disconnect(){
        if(ftp.isConnected()){
            try{
                ftp.disconnect();
            }catch (Exception e){
                //...
            }
        }
    }

}