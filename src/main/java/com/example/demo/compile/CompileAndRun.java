package com.example.demo.compile;

import com.example.demo.util.FileUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-03-07
 * Time: 19:20
 */

/**
 * 这个类来创建子进程, 来完成编译和运行, 并返回运行结果的状态
 * 1 表示编译, 2 表示运行, 以此来防止 运行阶段的运行代码介入到编译阶段
 */
public class CompileAndRun {

    /**
     * 该方法来完成编译和运行, 并返回运行结果的状态
     *
     * @param status  状态, 是编译还是运行, 约定 1 是编译, 2 是运行
     * @param command 执行命令(编译命令或执行命令)
     * @param stderr  出现错误, 将错误写入该文件
     * @param stdout  输出内容, 将输出的内容写入该文件
     * @return 返回代码最后的执行状态
     */
    public static void run(int status, String command, String stderr, String stdout) {
        try {
            // 获取 JVM 的执行环境, 并创建一个新的进程执行命令
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);

            // 运行阶段代码运行时间过长
            if (status == 2) {
                // 如果代码运行时间过长, 认为代码存在死循环, 无线递归等问题, 终止运行
                ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 如果等待 3 秒后再执行还存活, 说明出现了无线递归或死循环等情况
                        // 如果等待 3 秒后再执行未存活, 说明代码已经执行完了
                        if (process.isAlive()) {
                            FileUtil.writeFile(stderr, "代码运行超时, 请检查代码! ");
                            process.destroy();
                        }
                    }
                }, 3, TimeUnit.SECONDS);
            }

            // 1. 获取到标准错误, 并写到指定文件中
            if (stderr != null) {
                InputStream stderrFrom = process.getErrorStream();
                OutputStream stderrTo = new FileOutputStream(stderr, true);
                while (true) {
                    int ch = stderrFrom.read();
                    if (ch == -1) {
                        break;
                    }
                    stderrTo.write(ch);
                }
                stderrTo.close();
                stderrFrom.close();
            }

            // 2. 获取到标准输出, 并写到指定文件中
            if (stdout != null) {
                // 获取 process 子进程运行命令的输出流, 若命令还未运行完, 则阻塞等待
                InputStream stdoutFrom = process.getInputStream();
                OutputStream stdoutTo = new FileOutputStream(stdout);
                while (true) {
                    int ch = stdoutFrom.read();
                    if (ch == -1) {
                        break;
                    }
                    stdoutTo.write(ch);
                }
                stdoutTo.close();
                stdoutFrom.close();
            }
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
