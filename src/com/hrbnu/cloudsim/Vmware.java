package com.hrbnu.cloudsim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vmware implements Serializable {

    private static List<Integer> clock=new ArrayList<>();//任务完成时间列表
    private int minClock=0;//当前最下的任务完成时间
    private int id;
    private int totalUtilization=100;//总利用率
    private int usedUtilization=0;//已使用利用率
    private int residualUtilization=100;//剩余利用率

    private  List<Task> taskList=new ArrayList<Task>();//运行的任务ID
    //private static int usedTime;//任务已经运行的时间
    //private static int residualTime;//任务还需要多少时间

    private boolean isRunning=false;//虚拟机是否字运行的标识

    public Vmware(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public int getTotalUtilization() {
        return totalUtilization;
    }

    public void setTotalUtilization(int totalUtilization) {
        this.totalUtilization = totalUtilization;
    }

    public int getUsedUtilization() {
        return usedUtilization;
    }

    public void setUsedUtilization(int usedUtilization) {
        this.usedUtilization = usedUtilization;
    }

    public int getResidualUtilization() {
        return residualUtilization;
    }

    public void setResidualUtilization(int residualUtilization) {
        this.residualUtilization = residualUtilization;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void addTask(Task task) {
        this.taskList.add(task);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public List<Integer> getClock() {
        return clock;
    }

    public void addClock(int clock) {
        this.clock.add(clock);
    }

    public int getMinClock() {
        return minClock;
    }

    public void setMinClock(int minClock) {
        this.minClock = minClock;
    }

    public  void orderClock(){//排序集合中最小的完成时间
        minClock=clock.get(0);
        for(int i=0;i<clock.size();i++){
            if (minClock>clock.get(i)){
                minClock=clock.get(i);
            }
        }
    }

}
