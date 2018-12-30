package chapter1;

public class ThreadCreationViaSubclass {

    public static void main(String[] args) {
        Thread thread = new CustemThread();
        thread.start();
    }

    static class CustemThread extends Thread {

        @Override
        public void run() {
            System.out.println("Running....");
        }
    }
}