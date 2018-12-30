package chapter1;

import com.sun.deploy.ref.Helpers;

/**
 * @author geyy
 * @version $Id: JavaThreadAnyWhere.java, v 0.1 2018-12-24 13:55 geyy Exp $
 */
public class JavaThreadAnyWhere {

    public static void main(String[] args){
        System.out.println("The main method was excueted by thread :" + Thread.currentThread().getName());
        Helper helper = new Helper("Java Thread AnyWhere");
        Thread thread = new Thread(helper);
        thread.setName("A-Worker-Thread");
        thread.start();
    }

    static class Helper implements Runnable{
        private final String message;
        public Helper(String message){
            this.message = message;
        }

        private void doSomething(){
            System.out.println("The doSomething method was excueted by thread :" + Thread.currentThread().getName());
            System.out.println("Do Somethiong with :"+ message);
        }
        @Override
        public void run() {
            doSomething();
        }
    }
}