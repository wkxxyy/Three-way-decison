package com.hrbnu.cloudsim.util;

import com.hrbnu.cloudsim.Task;

import java.util.ArrayList;
import java.util.List;

public class getOrderList {

    List<Task> taskList;
    List<Task> smallTaskList=new ArrayList<>();
    List<Task> mediumTaskList=new ArrayList<>();
    List<Task> longTaskList=new ArrayList<>();

    List<Task> firstExList=new ArrayList<>();
    List<Task> secondExList=new ArrayList<>();

    int listSize=0;

    public getOrderList(List<Task> taskList){

        this.taskList=taskList;
        listSize=taskList.size();
        int smallIndex=0,mediumIndex=0,longIndex=0,firstIndex=0,secondIndex=0;

        smallIndex= (int)(listSize*0.4);
        mediumIndex=smallIndex*2;

        for (int i=0;i<smallIndex;i++){
            smallTaskList.add(taskList.get(i));
        }

        for (int j=smallIndex;j<mediumIndex;j++){
            mediumTaskList.add(taskList.get(j));
        }

        for (int k=mediumIndex;k<taskList.size();k++){
            longTaskList.add(taskList.get(k));
        }

        firstIndex=(int)(smallTaskList.size()*0.5);
        for (int i=0;i<firstIndex;i++){
            firstExList.add(smallTaskList.get(i));
        }

        firstIndex=(int)(mediumTaskList.size()*0.5);
        for (int i=0;i<firstIndex;i++){
            firstExList.add(mediumTaskList.get(i));
        }

        firstIndex=(int)(longTaskList.size()*0.5);
        for (int i=0;i<firstIndex;i++){
            firstExList.add(longTaskList.get(i));
        }

        firstIndex=(int)(smallTaskList.size()*0.5);
        for (int i=firstIndex;i<smallTaskList.size();i++){
            secondExList.add(smallTaskList.get(i));
        }

        firstIndex=(int)(mediumTaskList.size()*0.5);
        for (int i=firstIndex;i<mediumTaskList.size();i++){
            secondExList.add(mediumTaskList.get(i));
        }

        firstIndex=(int)(longTaskList.size()*0.5);
        for (int i=firstIndex;i<longTaskList.size();i++){
            secondExList.add(longTaskList.get(i));
        }


    }


    public List<Task> getFirstExList() {
        return firstExList;
    }

    public List<Task> getSecondExList() {
        return secondExList;
    }
}
