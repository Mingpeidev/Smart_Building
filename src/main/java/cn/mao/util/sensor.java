package cn.mao.util;

import java.util.Timer;
import java.util.TimerTask;



public class sensor {
	
	

    public static void main(String[] args) {
       Timer timer=new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("系统正在运行……");
            }
        }, 5000,1000); //指定启动定时器5s之后运行定时器任务run方法，并且只运行一次
    }

}
