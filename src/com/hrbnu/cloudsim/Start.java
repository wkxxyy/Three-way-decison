package com.hrbnu.cloudsim;

import com.hrbnu.cloudsim.util.CalculatePower;

import java.io.*;
import java.util.*;

public class Start {
    private static double maxClock;//记录的最da任务完成时间
    private static List<Task> taskList = new ArrayList<Task>();
    private static List<Vmware> vmwareList = new ArrayList<Vmware>();
    private static int time = 0;//计数时间
    private static List<Task> taskListDone = new ArrayList<Task>();
    private static double EVIM;//消耗的能耗


    public static void main(String[] args) {

        List<Task> firstExList;//第一步执行
        List<Task> secondExList;//第二步执行


        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    time++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Random random = new Random();*/
   /*
   需要固定给定时间的值


    */
        taskList.clear();
        vmwareList.clear();
        File fileTask = new File("E:/object/fileTask.txt");

        File fileVmware = new File("E:/object/fileVmware.txt");

        try {
            FileInputStream fisTask = new FileInputStream(fileTask);
            ObjectInputStream oisTask = new ObjectInputStream(fisTask);
            taskList = (List<Task>) oisTask.readObject();
            oisTask.close();
            fisTask.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fisVmware = new FileInputStream(fileVmware);
            ObjectInputStream oisVmware = new ObjectInputStream(fisVmware);
            vmwareList = (List<Vmware>) oisVmware.readObject();
            oisVmware.close();
            fisVmware.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        maxClock = vmwareList.get(0).getMaxClock();//将第一个虚拟机的完成时间设置为minClock



/*
    计算各个的健康度


 */
       /* for (int i = 0; i < taskList.size(); i++) {

            List<Task> sameAt = new ArrayList<Task>();
            int maxPT;
            int maxUT;
            sameAt.add(taskList.get(i));
            maxPT=taskList.get(i).getPeriod();
            maxUT=taskList.get(i).getNeedUtilization();

        for (int fi = 0; fi < taskList.size(); fi++) {
            if (taskList.get(fi).getArrivalTime()==taskList.get(i).getArrivalTime()){
                sameAt.add(taskList.get(fi));
                if (taskList.get(fi).getPeriod()>maxPT){
                    maxPT=taskList.get(fi).getPeriod();
                }
                if (taskList.get(fi).getNeedUtilization()>maxUT){
                    maxUT=taskList.get(fi).getNeedUtilization();
                }
            }

        }

        for (int j=0;j<sameAt.size();j++){
            sameAt.get(j).setFitness(0.5*(sameAt.get(j).getPeriod())/maxPT+(1-0.5)*(sameAt.get(j).getNeedUtilization())/maxUT);
        }


    }*/
        Collections.sort(taskList);//按照健康度排序


        //startDispatchWithESTC();
        startThird();
        EVIM = CalculatePower.getCalculatePower(taskListDone, vmwareList, "Third");
        System.out.println("Third能耗为：" + EVIM);
        System.out.println("调度完成！！！！！！！！！！！！！！！！");

        /*startDispatchWithESTCwithMQS(firstExList,secondExList);
        firstExList.addAll(secondExList);
        EVIM= CalculatePower.getCalculatePower(firstExList,vmwareList,"ESTC");
        System.out.println("ESTCMQS能耗为："+EVIM);
        System.out.println("调度完成！！！！！！！！！！！！！！！！");*/

        /*startDispatchWithETCWithMQS(firstExList,secondExList);
        firstExList.addAll(secondExList);
        EVIM= CalculatePower.getCalculatePower(firstExList,vmwareList,"ETC");
        System.out.println("ETCMQS能耗为："+EVIM);
        System.out.println("调度完成！！！！！！！！！！！！！！！！");*/


    }


    public static void startThird() {

        int minIndex = 0;
        while (taskList.size() > 0) {


            Task currentTask;//当前正在调度的任务
            Vmware currentVmware;//当前正在调度的虚拟机

            for (int i = 0; i < taskList.size(); i++) {
                boolean tag = false;//用来标记是否任务在空虚拟机上运行了

                currentTask = taskList.get(i);

                for (int j = 0; j < vmwareList.size(); j++) {

                    currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机

                    if (currentVmware.getMaxClock() > time) {//最小完成时间大于当前的时间，那么虚拟机就是在运行的
                        continue;
                    } else {
                        //手动输入任务需要取出下面的语句
                        currentTask.setStartTime(time + currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                        currentTask.setFinishTime(currentTask.getStartTime() + currentTask.getPeriod());
                        if (currentVmware.getMaxClock() < maxClock) {//获取全局最小的完成时间
                            maxClock = currentVmware.getMaxClock();
                            minIndex = j;
                        }//寻找最小的完成时间
                        BindTasksToVm.RunTaskWithFifo(currentTask, currentVmware);

                        taskListDone.add(currentTask);
                        taskList.remove(currentTask);

                        tag = true;

                        break;
                    }
                }
            /*
                如果tag为false就是虚拟机都满了，就开始下面的找最小利用率的开始执行

             */
                if (!tag) {


                    for (int k = 0; k < taskList.size(); k++) {

                        currentTask = taskList.get(k);

                        if (currentTask.getArrivalTime() > time) {

                            if (currentTask.getDeadLine() - (currentTask.getPeriod() + time) <= 5) {

                                int vmMinPowerIndex = 0;

                                int vmMinPower = vmwareList.get(0).getUsedUtilization();

                                for (int z = 0; z < vmwareList.size(); z++) {

                                    currentVmware = vmwareList.get(z);

                                    if (vmMinPower > currentVmware.getUsedUtilization()) {

                                        vmMinPower = currentVmware.getUsedUtilization();

                                        vmMinPowerIndex = z;

                                    }
                                }

                                currentTask.setStartTime(time + currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                                currentTask.setFinishTime(currentTask.getStartTime() + currentTask.getPeriod());
                                BindTasksToVm.RunTaskWithFifo(currentTask, vmwareList.get(vmMinPowerIndex));

                                taskListDone.add(currentTask);
                                taskList.remove(currentTask);


                            } else if (currentTask.getDeadLine() - (currentTask.getPeriod() + time) > 5 && (currentTask.getDeadLine() < (getMaxMinVm().getMaxClock() + currentTask.getPeriod()))) {

                                currentTask.setStartTime(getMaxMinVm().getMaxClock() - 5);
                                currentTask.setFinishTime(currentTask.getStartTime() + currentTask.getPeriod());
                                BindTasksToVm.RunTaskWithFifo(currentTask, getMaxMinVm());

                                taskListDone.add(currentTask);
                                taskList.remove(currentTask);

                            } else if (currentTask.getDeadLine() > (getMaxMinVm().getMaxClock() + currentTask.getPeriod())) {

                                currentTask.setStartTime(getMaxMinVm().getMaxClock());
                                currentTask.setFinishTime(currentTask.getStartTime() + currentTask.getPeriod());
                                BindTasksToVm.RunTaskWithFifo(currentTask, getMaxMinVm());

                                taskListDone.add(currentTask);
                                taskList.remove(currentTask);


                            }


                        }


                    }


                }
            }

            time++;
        }

    }


    public static void startDispatchWithESTC() {

        Task currentTask;//当前正在调度的任务
        Vmware currentVmware;//当前正在调度的虚拟机

        for (int i = 0; i < taskList.size(); i++) {
            boolean tag = false;//用来标记是否任务在空虚拟机上运行了

            currentTask = taskList.get(i);

            for (int j = 0; j < vmwareList.size(); j++) {
                currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机
                if (currentVmware.getMaxClock() > time) {//最小完成时间大于当前的时间，那么虚拟机就是在运行的
                    continue;
                } else {
                    //手动输入任务需要取出下面的语句
                    //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                    //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                    if (currentTask.getFinishTime() < maxClock) {
                        maxClock = currentTask.getFinishTime();
                    }//寻找最小的完成时间
                    BindTasksToVm.RunTaskWithFifo(currentTask, currentVmware);
                    tag = true;
                    break;
                }
            }
            /*
                如果tag为false就是虚拟机都满了，就开始下面的找最小利用率的开始执行

             */
            if (!tag) {
                double minUtli = vmwareList.get(0).getUsedUtilization();//把第一个虚拟机的利用率设为初始值
                int index = 0;//用来技术那个虚拟机最小
                for (int k = 0; k < vmwareList.size(); k++) {
                    if (minUtli < vmwareList.get(k).getUsedUtilization()) {
                        minUtli = vmwareList.get(k).getUsedUtilization();
                        index = k;
                    }
                }
                //手动输入任务需要取出下面的语句
                //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                if (currentTask.getFinishTime() < maxClock) {
                    maxClock = currentTask.getFinishTime();
                }//寻找最小的完成时间
                BindTasksToVm.RunTaskWithFifo(currentTask, vmwareList.get(index));
            }


        }
    }

    public static void startDispatchWithESTCwithMQS(List<Task> firstExList, List<Task> secondExList) {

        Task currentTask;//当前正在调度的任务
        Vmware currentVmware;//当前正在调度的虚拟机

        for (int i = 0; i < firstExList.size(); i++) {
            boolean tag = false;//用来标记是否任务在空虚拟机上运行了

            currentTask = firstExList.get(i);

            for (int j = 0; j < vmwareList.size(); j++) {
                currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机
                if (currentVmware.getMaxClock() > time) {//最小完成时间大于当前的时间，那么虚拟机就是在运行的
                    continue;
                } else {
                    //手动输入任务需要取出下面的语句
                    //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                    //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                    if (currentTask.getFinishTime() < maxClock) {
                        maxClock = currentTask.getFinishTime();
                    }//寻找最小的完成时间
                    BindTasksToVm.RunTaskWithFifo(currentTask, currentVmware);
                    tag = true;
                    break;
                }
            }
            /*
                如果tag为false就是虚拟机都满了，就开始下面的找最小利用率的开始执行

             */
            if (!tag) {
                double minUtli = vmwareList.get(0).getUsedUtilization();//把第一个虚拟机的利用率设为初始值
                int index = 0;//用来技术那个虚拟机最小
                for (int k = 0; k < vmwareList.size(); k++) {
                    if (minUtli < vmwareList.get(k).getUsedUtilization()) {
                        minUtli = vmwareList.get(k).getUsedUtilization();
                        index = k;
                    }
                }
                //手动输入任务需要取出下面的语句
                //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                if (currentTask.getFinishTime() < maxClock) {
                    maxClock = currentTask.getFinishTime();
                }//寻找最小的完成时间
                BindTasksToVm.RunTaskWithFifo(currentTask, vmwareList.get(index));
            }


        }


        for (int i = 0; i < secondExList.size(); i++) {
            boolean tag = false;//用来标记是否任务在空虚拟机上运行了

            currentTask = secondExList.get(i);

            for (int j = 0; j < vmwareList.size(); j++) {
                currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机
                if (currentVmware.getMaxClock() > time) {//最小完成时间大于当前的时间，那么虚拟机就是在运行的
                    continue;
                } else {
                    //手动输入任务需要取出下面的语句
                    currentTask.setStartTime(getMinutes() + currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                    currentTask.setFinishTime(currentTask.getStartTime() + currentTask.getPeriod());
                    if (currentTask.getFinishTime() < maxClock) {
                        maxClock = currentTask.getFinishTime();
                    }//寻找最小的完成时间
                    BindTasksToVm.RunTaskWithFifo(currentTask, currentVmware);
                    tag = true;
                    break;
                }
            }
            /*
                如果tag为false就是虚拟机都满了，就开始下面的找最小利用率的开始执行

             */
            if (!tag) {
                double minUtli = vmwareList.get(0).getUsedUtilization();//把第一个虚拟机的利用率设为初始值
                int index = 0;//用来技术那个虚拟机最小
                for (int k = 0; k < vmwareList.size(); k++) {
                    if (minUtli < vmwareList.get(k).getUsedUtilization()) {
                        minUtli = vmwareList.get(k).getUsedUtilization();
                        index = k;
                    }
                }
                //手动输入任务需要取出下面的语句
                currentTask.setStartTime(getMinutes() + currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                currentTask.setFinishTime(currentTask.getStartTime() + currentTask.getPeriod());
                if (currentTask.getFinishTime() < maxClock) {
                    maxClock = currentTask.getFinishTime();
                }//寻找最小的完成时间
                BindTasksToVm.RunTaskWithFifo(currentTask, vmwareList.get(index));
            }

        }
    }

    public static void startDispatchWithETCWithMQS(List<Task> firstExList, List<Task> secondExList) {


        Task currentTask;//当前正在调度的任务
        Vmware currentVmware;//当前正在调度的虚拟机

        for (int i = 0; i < firstExList.size(); i++) {
            boolean tag = false;//判断任务有没有被运行

            currentTask = firstExList.get(i);

            for (int j = 0; j < vmwareList.size(); j++) {
                currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机

                if (currentTask.getNeedUtilization() + currentVmware.getUsedUtilization() <= 70) {//如果当前虚拟机的利用率加上来的任务的利用率小于70，那就可以运行，就进行绑定
                    //手动输入任务需要取消下面的语句
                    //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                    //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                    if (currentTask.getFinishTime() < maxClock) {
                        maxClock = currentTask.getFinishTime();
                    }//寻找最小的完成时间
                    BindTasksToVm.RunTaskWithFifo(currentTask, currentVmware);//开始运行
                    tag = true;
                    break;//这个任务运行了，那就开始给下个任务找虚拟机

                } else {
                    continue;//这个任务没有运行，所以就继续查询下个虚拟机满不满足条件

                }

            }
            /*
                如果所有虚拟机都没运行，就找最小利用率的
             */
            if (!tag) {
                double minUtli = vmwareList.get(0).getUsedUtilization();//把第一个虚拟机的利用率设为初始值
                int index = 0;//用来技术那个虚拟机的利用率最小
                for (int k = 0; k < vmwareList.size(); k++) {
                    if (minUtli < vmwareList.get(k).getUsedUtilization()) {
                        minUtli = vmwareList.get(k).getUsedUtilization();
                        index = k;
                    }
                }
                //手动输入任务需要取出下面的语句
                //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                if (currentTask.getFinishTime() < maxClock) {
                    maxClock = currentTask.getFinishTime();
                }//寻找最小的完成时间
                BindTasksToVm.RunTaskWithFifo(currentTask, vmwareList.get(index));
            }
        }


        for (int i = 0; i < secondExList.size(); i++) {
            boolean tag = false;//判断任务有没有被运行

            currentTask = secondExList.get(i);

            for (int j = 0; j < vmwareList.size(); j++) {
                currentVmware = vmwareList.get(j);//从第一个开始挑虚拟机

                if (currentTask.getNeedUtilization() + currentVmware.getUsedUtilization() <= 70) {//如果当前虚拟机的利用率加上来的任务的利用率小于70，那就可以运行，就进行绑定
                    //手动输入任务需要取消下面的语句
                    //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                    //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                    if (currentTask.getFinishTime() < maxClock) {
                        maxClock = currentTask.getFinishTime();
                    }//寻找最小的完成时间
                    BindTasksToVm.RunTaskWithFifo(currentTask, currentVmware);//开始运行
                    tag = true;
                    break;//这个任务运行了，那就开始给下个任务找虚拟机

                } else {
                    continue;//这个任务没有运行，所以就继续查询下个虚拟机满不满足条件

                }

            }
            /*
                如果所有虚拟机都没运行，就找最小利用率的
             */
            if (!tag) {
                double minUtli = vmwareList.get(0).getUsedUtilization();//把第一个虚拟机的利用率设为初始值
                int index = 0;//用来技术那个虚拟机的利用率最小
                for (int k = 0; k < vmwareList.size(); k++) {
                    if (minUtli < vmwareList.get(k).getUsedUtilization()) {
                        minUtli = vmwareList.get(k).getUsedUtilization();
                        index = k;
                    }
                }
                //手动输入任务需要取出下面的语句
                //currentTask.setStartTime(getMinutes()+currentTask.getArrivalTime());//设置任务开始时间,就是当前的时间的分钟数
                //currentTask.setFinishTime(currentTask.getStartTime()+currentTask.getPeriod());
                if (currentTask.getFinishTime() < maxClock) {
                    maxClock = currentTask.getFinishTime();
                }//寻找最小的完成时间
                BindTasksToVm.RunTaskWithFifo(currentTask, vmwareList.get(index));
            }
        }
    }

    public static int getMinutes() {//获取当前时间分钟数
        return time;
    }

    public static Vmware getMaxMinVm() {//cha  zhao   vm  liebiao zhong de zuizaowanchengshijian


        int minIndex = 0;
        int minVmwareTime = vmwareList.get(0).getMaxClock();

        for (int j = 0; j < vmwareList.size(); j++) {

            if (minVmwareTime > vmwareList.get(j).getMaxClock()) {
                minVmwareTime = vmwareList.get(j).getMaxClock();
                minIndex = j;

            }


        }

        return vmwareList.get(minIndex);
    }


}
