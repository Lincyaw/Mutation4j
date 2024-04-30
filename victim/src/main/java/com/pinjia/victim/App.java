package com.pinjia.victim;
import java.util.Random;
import java.time.LocalDateTime; // 用于获取当前时间
import java.time.format.DateTimeFormatter; // 用于格式化时间

/**
 * 计算并每秒打印一次随机数的和，包含时间戳
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Random ran = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 时间格式

        while (true) {
            int a = ran.nextInt(10); // 生成0到99的随机整数
            int b = ran.nextInt(10);  // 生成0到49的随机整数
            int c = a + b;            // 计算和

            LocalDateTime now = LocalDateTime.now(); // 获取当前时间
            String timeStamp = now.format(formatter); // 格式化时间戳

            System.out.printf("%s:  %d + %d = %d\n", timeStamp, a, b, c); // 打印时间戳和计算结果

            try {
                Thread.sleep(1000); // 暂停1秒
            } catch (InterruptedException e) {
                System.err.println("线程被中断");
                break; // 如果线程被中断，跳出循环
            }
        }
    }
}
