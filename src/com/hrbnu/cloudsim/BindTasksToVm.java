package com.hrbnu.cloudsim;

import com.hrbnu.cloudsim.util.CalculatePower;

import java.util.Date;

public class BindTasksToVm {





    public static void RunTaskWithFifo(Task task,Vmware vmware){


        vmware.addTask(task);//设置虚拟机当前运行的程序集合
        vmware.setUsedUtilization(vmware.getUsedUtilization()+task.getNeedUtilization());//设置已经使用的利用率
        vmware.setResidualUtilization(vmware.getTotalUtilization()-vmware.getUsedUtilization());//设置虚拟机的剩余利用率
        vmware.setRunning(true);//虚拟机设置为正在运行状态
        task.setRunning(true);//设置任务正在运行
        vmware.addClock(task.getFinishTime());
        vmware.orderClock();//排序一下取得最小的运行时间

        //EVIM= CalculatePower.getCalculatePower(task);//计算能耗

        int taskId=task.getId();
        int vmwareId=vmware.getId();
        System.out.println("*********************************************");
        System.out.println(taskId+"号任务正在运行，请稍后。。。。。。。");
        System.out.println(taskId+"号任务运行在"+vmwareId+"号虚拟机上");
        System.out.println(taskId+"号任务的到达时间为："+task.getArrivalTime());
        System.out.println(taskId+"号任务开始时间为："+task.getStartTime());
        System.out.println(taskId+"号任务需要的时间为："+task.getPeriod());
        System.out.println(taskId+"号任务需要的利用率为："+task.getNeedUtilization());
        System.out.println("-------------------------------------");

        for (int time=0;time<task.getPeriod();time++){
            System.out.println(taskId+"号任务剩余运行时间为："+(task.getPeriod()-time));
           /* try {
                //Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/


        }
        //task.setFinishTime(new Date().getMinutes());//这是任务结束时间
        task.setFinish(true);//任务设置为完成状态
        vmware.setRunning(false);//虚拟机设置为没有在运行状态
        System.out.println("-------------------------------------");
        System.out.println(taskId+"号任务运行完毕，运行结束时间为："+task.getFinishTime());
        //System.out.println(taskId+"号任务在"+vmwareId+"号虚拟机上消耗的能耗为："+EVIM);
        System.out.println("*********************************************");
        System.out.println();



    }
}
