package chapter1;

import com.sun.deploy.ref.Helpers;

/**
 * @author geyy
 * @version $Id: JavaThreadAnyWhere.java, v 0.1 2018-12-24 13:55 geyy Exp $
 */
public class JavaThreadAnyWhere {

    public static void main(String[] args){
        System.out.println("The main method was excuted by thread: "+Thread.currentThread().getName());
       Helper helper = new Helper("Java thread anywhere");
       helper.run();
    }
    static class Helper implements Runnable{

        private final String message;

        public Helper(String message){
            this.message = message;
        }

        private void doSomeThing(String message){
            System.out.println("The doSomeThing method was excuted by thread: "+Thread.currentThread().getName());
            System.out.print("Do SomeThing with "+message);
        }
        @Override
        public void run() {
            doSomeThing(message);
        }
    }
}