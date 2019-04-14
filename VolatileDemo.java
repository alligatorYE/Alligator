package com.alligator.Interview.study.thread;

import java.util.concurrent.TimeUnit;

class MyData {
    volatile int number = 0;

    public void setNumber() {
        this.number = 60;
    }
//请注意，此时number前面是加了volatile关键字修饰的
    public void addPlusPlus() {
        number++;
    }
}

/**
 * @Description
 * 1.验证Volatile的可见性，
 *  1.1假设int number = 0 ；number变量之前没有添加volatile关键字修饰,没有可见性
 *  1.2添加了volatile可以解决可见性问题
 * 2验证volatile不保证原子性
 *  2.1什么是原子性：
 *      不可分割，完成性，业绩某个线程正在做某个业务时，中间不可以被加塞或者被分割。需要整体完成
 *      要么同时成功要么同时失败。
 *  2.2volatile不保证原子性的案例
 * @Param
 * @Return
 * @Author alligator YE
 * @Date 2019/4/13
 * @Time 18:41
 */
public class VolatileDemo {

    public static void main(String[] args) {
        MyData myData = new MyData();
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <1000; j++) {
                    myData.addPlusPlus();
                }

            }, String.valueOf(i)).start();
        }
//        需要等待上面的20个线程全部计算完后，再用main线程取得最终的结果值看是多少
        seeOkByVolatile();
    }

    // 验证Volatile的可见性 开始
    private static void seeOkByVolatile() {
        MyData myData = new MyData();//资源类

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.setNumber();
        }, "Thread A").start();
//        第二个线程就是main线程
        while (myData.number == 0) {
//            main线程一致在这里等待循环，直到number值不再为0
        }
        System.out.println(Thread.currentThread().getName() +
                "\t mission is over, main get number value: " + myData.number);
    }
    //验证Volatile的可见性 结束


}
