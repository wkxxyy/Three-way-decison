package com.hrbnu.cloudsim.util;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class OutPutFile {

    private WritableWorkbook book;
    private WritableSheet sheet;

    public OutPutFile(){
        File file=new File("E:/dataESTC");
        deleteFile(file);

    }

    public void writeFile(double[] message,int vmId,String tableName){


        File fileDir=new File("");

        try{

            File file =new File("E:/dataESTC/"+tableName+String.valueOf(vmId)+"号虚拟机每秒能耗.xls");

            if (!file.exists()){
                file.getParentFile().mkdir();
            }
            //if file doesnt exists, then create it
            if(!file.exists()){
                file.createNewFile();
                try {

                    // 打开文件
                    book = Workbook.createWorkbook(file);
                    // 生成名为“第一张工作表”的工作表，参数0表示这是第一页
                    sheet = book.createSheet("每秒的消耗", 0);
                    // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
                    // 以及单元格内容为baby
                    Label labelSecond=new Label(0,0,"秒数");
                    Label labelEnger = new Label(1, 0, "能源消耗");
                    // 将定义好的单元格添加到工作表中

                    sheet.addCell(labelSecond);
                    sheet.addCell(labelEnger);



                    for(int i=1;i<=message.length;i++){
                        jxl.write.Number id = new jxl.write.Number(0, i, i);
                        sheet.addCell(id);
                    }



                    for (int i = 1; i <=message.length; i++) {
                        jxl.write.Number Enger = new jxl.write.Number(1, i, message[i - 1]);
                        sheet.addCell(Enger);
                    }


                    // 生成一个保存数字的单元格，必须使用Number的完整包路径，否则有语法歧义。
                    //单元格位置是第二列，第一行，值为123
                    //写入数据并关闭
                    book.write();
                    book.close();
                    System.out.println("写入完成");

                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void deleteFile(File file) {
        if (file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
                    this.deleteFile(files[i]);//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        } else {
            System.out.println("所删除的文件不存在");
        }
    }


}
