package com.hrbnu.cloudsim;

import com.hrbnu.cloudsim.util.PossionDis;

import java.io.Serializable;

public class Task implements Comparable<Task>,Serializable {

    private int id;//Id
    private int arrivalTime;//到达时间
    private int startTime;//开始时间
    private int period;//运行时间，周期
    private int finishTime;//完成时间
    private int needUtilization;//需要的利用率
    private double fitness;//健康度
    private int burstTime;

    private boolean isRunning=false;//任务是否在运行的标识
    private boolean isFinish=false;//任务是否完成的标识

    public Task(int id){
        this.id=id;
        arrivalTime= PossionDis.getPossionVariable(id);//初始化到达使劲，到达时间服从泊松分布
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
        setBurstTime(getStartTime()-getArrivalTime());
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getNeedUtilization() {
        return needUtilization;
    }

    public void setNeedUtilization(int needUtilization) {
        this.needUtilization = needUtilization;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    /*
                按照到达时间进行集合排序

             */

    public int compareTo(Task o) {
        int i = this.getArrivalTime() - o.getArrivalTime();//

        return i;
    }

   /* public int compareTo(Task o) {
        int i = this.getBurstTime() - o.getBurstTime();//

        return i;
    }*/


 /*  public int compareTo(Task o) {
        int i;
        if ( (this.getFitness() - o.getFitness())>0){
            i=1;
        }else if((this.getFitness() - o.getFitness())==0){
            i=0;
        }
        else{
            i=-1;
        }


        return i;
    }*/
}
