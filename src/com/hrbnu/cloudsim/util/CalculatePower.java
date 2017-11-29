package com.hrbnu.cloudsim.util;
import com.hrbnu.cloudsim.Start;
import com.hrbnu.cloudsim.Task;
import com.hrbnu.cloudsim.Vmware;

import java.util.ArrayList;
import java.util.List;

public class CalculatePower {

    public static final double P20 = 78.5;
    public static final double P30 = 83;
    public static final double P40 = 85;
    public static final double P50 = 88;
    public static final double P60 = 93;
    public static final double P70 = 102;
    public static final double P80 = 109;
    public static final double P90 = 122;
    public static final double P100 = 136;

    //最后的功耗

    public static double getCalculatePower(List<Task> taskList, List<Vmware> vmwareList,String tableName) {

        double EVIM = 0;

        List<Double> perSecondEngerList=new ArrayList<Double>();

        OutPutFile outPutFile=new OutPutFile();

        for (int i = 0; i < vmwareList.size(); i++) {

            double currentVmPower = 0;//当前这一秒的虚拟机的功耗
            int maxFt;
            int minAt;

            if (vmwareList.get(i).getTaskList().size() > 0) {//如果这个虚拟机的任务列表不为空就继续

                maxFt = vmwareList.get(i).getTaskList().get(0).getFinishTime();//当前虚拟机中最大的完成时间，把当前虚拟机中的第一个任务的完成时间设置为初始值
                minAt = vmwareList.get(i).getTaskList().get(0).getArrivalTime();//当前虚拟机中最小的到达时间，把当前虚拟机中的第一个任务的到达时间设置为初始值

                for (int tagFt = 0; tagFt < vmwareList.get(i).getTaskList().size(); tagFt++) {//获取当前虚拟机中的任务最大到达时间
                    if (vmwareList.get(i).getTaskList().get(tagFt).getFinishTime() > maxFt) {
                        maxFt = vmwareList.get(i).getTaskList().get(tagFt).getFinishTime();
                    }
                }
/*
其实这里可以不用判断，因为最早到达时间肯定是任务列表里面第一个，因为在添加到List的时候已经通过compare排序过了
 */
                for (int tagAt = 0; tagAt < vmwareList.get(i).getTaskList().size(); tagAt++) {//获取当前虚拟机中的任务最小到达时间
                    if (vmwareList.get(i).getTaskList().get(tagAt).getArrivalTime() < minAt) {
                        minAt = vmwareList.get(i).getTaskList().get(tagAt).getArrivalTime();
                    }
                }
            } else {//如果当前虚拟机的任务列表为空，那就按照最大运行时间*p20算，并且开始下一个任务，以下为找最大运行时间

                maxFt = taskList.get(0).getFinishTime();//任务集合中的第一个任务的完成时间设置为初始时间把当前虚拟机中的第一个任务的完成时间设置为初始值
                minAt = taskList.get(0).getArrivalTime();//任务集合中的第一个任务的到达时间设置为初始时间把当前虚拟机中的第一个任务的完成时间设置为初始值

                for (int tagFt = 0; tagFt < taskList.size(); tagFt++) {//获取当前虚拟机中的任务最大到达时间
                    if (taskList.get(tagFt).getFinishTime() > maxFt) {
                        maxFt = taskList.get(tagFt).getFinishTime();
                    }
                }
/*
其实这里可以不用判断，因为最早到达时间肯定是任务列表里面第一个，因为在添加到List的时候已经通过compare排序过了
 */
                for (int tagAt = 0; tagAt < taskList.size(); tagAt++) {//获取当前虚拟机中的任务最小到达时间
                    if (taskList.get(tagAt).getArrivalTime() < minAt) {
                        minAt = taskList.get(tagAt).getArrivalTime();
                    }
                }

                EVIM += (maxFt - minAt) * 75.5;
                continue;

            }


            for (int currentTime = minAt; currentTime <= (maxFt - minAt); currentTime++) {//当前虚拟机开始从第一个任务到达的时间起开始算，一秒一秒算当前的利用率


                double currentSecondUtlization = 0;//当前这一秒本虚拟机上所有运行的任务的利用率

                for (int j = 0; j < vmwareList.get(i).getTaskList().size(); j++) {//一个一个取任务

                    Task currentTask = vmwareList.get(i).getTaskList().get(j);//取当前虚拟机下的任务集合中的任务

                    if (currentTime >= currentTask.getStartTime() && currentTime <= currentTask.getFinishTime()) {//获取当前正在运行的任务的利用率

                        //程序在运行

                            currentSecondUtlization += currentTask.getNeedUtilization();



                        }



                }

                if (currentSecondUtlization<20){
                    currentSecondUtlization=20;
                }

                currentVmPower = caculateCurrentUtPowerWith(currentSecondUtlization);
                perSecondEngerList.add(currentVmPower);
                EVIM += currentVmPower;//总计功耗加上当前这一秒的功耗
            }

            double[] perSecondEngerArrys=new double[perSecondEngerList.size()];
            for (int k=0;k<perSecondEngerArrys.length;k++){
                perSecondEngerArrys[k]=perSecondEngerList.get(k);
            }

            outPutFile.writeFile(perSecondEngerArrys,vmwareList.get(i).getId(),tableName);


        }
        return EVIM;


    }


    public static double caculateCurrentUtPowerWith(double utilzation) {
        double[] watts = new double[]{83, 85, 88, 93, 102, 109, 122, 136};
        int unit = 0;//利用率的个位数
        int digit = 0;//利用率的十位数
        int i;//记录能耗取到哪里了


        if (utilzation > 100 ) {
            unit = (int) (Math.ceil(100 % 10));//取个位数
            digit = (int) (100 - 20) / 10;//取十位数
        } else {
            unit = (int) (Math.ceil(utilzation % 10));//取个位数
            digit = (int) (utilzation - 20) / 10;//取十位数
        }
        double count = 0;//总共消耗

        if (digit > 2||utilzation>20) {
            count += 78.5;

            int j;
            for (j = 0; j < digit; j++) {
                count += watts[j];
            }

            if (unit != 0) {//如果个位不为0就计算个位并且利用率大于20
                count += watts[j] / 10 * unit;//算个位的能耗,有问题
            }

            return count;
        } else {
            return 78.5;
        }


    }
}
