package com.smartcollege.smartcollege.database;

import Encryption.Encryption;

import java.util.ArrayList;

public class Tokens {
    static ArrayList<String> list = new ArrayList<String>();

    public static String generateNewToken(){
        String token = Encryption.generateRandomPassword(10);
        list.add(token);

        //clear token thread
        ClearTokenThread clearToken = new ClearTokenThread(token);
        Thread thread = new Thread(clearToken);
        thread.start();

        return token;
    }
    private static class ClearTokenThread implements Runnable{
        String token;

        ClearTokenThread(String token){
            this.token = token;
        }
        @Override
        public void run() {
            try {
                System.out.println(list);

                Thread.sleep(20000);
                //remove token in 10 seconds;
                list.remove(this.token);
                System.out.println("Removed:"+list);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
