/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author apple
 */
public class TimerManager {

   static class RunMeTask extends TimerTask {
       int i=0;
        @Override
        public void run() {
            System.out.println("Run Me ~" + i++);
        }

        public void schedule(TimerTask task, long delay, long period) {
        }
    }

   /*    public static void main(String[] args) {
   
   TimerTask task = new RunMeTask();
   
   Timer timer = new Timer();
   timer.schedule(task, 1000, 20000);
   
   }*/
}
