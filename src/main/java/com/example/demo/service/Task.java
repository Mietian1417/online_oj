package com.example.demo.service;

import com.example.demo.util.FileUtil;
import com.example.demo.model.Answer;
import com.example.demo.model.Question;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-03-07
 * Time: 19:44
 */

/**
 * 这个类来真正的处理整个执行流程, 并返回最终的运行答案
 */
public class Task {
    // 每个执行代码都都有自己的目录, 以区所有提交的题目
    private String WORD_DIR;
    // 执行代码文件的类名
    private String CLASS;
    // 执行代码文件的文件名
    private String CODE_FILE;
    // 编译错误的文件名
    private String COMPILE_STDERR;
    // 运行异常的文件名
    private String RUN_STDERR;
    // 正常输出的文件名
    private String STDOUT;

    public Task() {
        this.WORD_DIR = "./tmp/" + UUID.randomUUID().toString() + "/";
        this.CLASS = "Solution";
        this.CODE_FILE = this.WORD_DIR + "Solution.java";
        this.COMPILE_STDERR = this.WORD_DIR + "compile_stderr.txt";
        this.RUN_STDERR = this.WORD_DIR + "run_stderr.txt";
        this.STDOUT = this.WORD_DIR + "stdout.txt";
    }

    /**
     * 代码执行流程
     *
     * @param question 整个题目的执行代码
     * @return 返回代码执行结果
     */
    public Answer compileAndRun(Question question) {
        // 创建目录
        Answer answer = new Answer();
        File workDir = new File(WORD_DIR);
        if (!workDir.exists()) {
            // 这个方法可以创建递归创建多级目录
            workDir.mkdirs();
        }

        // 校验代码的安全性
        if (!checkCodeSafe(question.getCode())) {
            System.out.println("用户提交危险代码! ");
            answer.setStatus(3);
            answer.setReason("您提交的代码可能存在危险操作, 禁止运行!");
            return answer;
        }

        // 得到整个完整代码, 并写入到 CODE_FILE 文件中
        FileUtil.writeFile(CODE_FILE, question.getCode());

        /*
            javac -encoding utf8 %s -d %s
            编译命令, 第一个参数为 编译的完整文件名(包括目录和后缀), 第二个参数为文件编译后产生 .class 字节码文件的所在位置
         */
        String compileCommand = String.format("javac -encoding utf8 %s -d %s", CODE_FILE, WORD_DIR);
        CompileAndRun.run(1, compileCommand, COMPILE_STDERR, null);
        // 根据编译后的 COMPILE_STDERR 文件是否为空, 来判断 编译是否出错
        String compileError = FileUtil.readFile(COMPILE_STDERR);
        if (!"".equals(compileError)) {
            // 前面约定了 1 为 编译错误
            answer.setStatus(1);
            answer.setReason(compileError);
            return answer;
        }

        /*
            java -classpath %s %s
            运行名利, 第一个参数为 字节码所在的目录, 第二个参数为 字节码文件的文件名(只是文件名, 不包括目录和后缀)
         */
        String runCommand = String.format("java -classpath %s %s", WORD_DIR, CLASS);
        CompileAndRun.run(2, runCommand, RUN_STDERR, STDOUT);

        // 根据编译后的 RUN_STDERR 文件是否为空, 来判断 运行是否出错
        String runError = FileUtil.readFile(RUN_STDERR);
        if (!"".equals(runError)) {
            // 前面约定了 2 为 运行异常
            answer.setStatus(2);
            answer.setReason(runError);
            return answer;
        }

        // 到这里, 表明没有出现问题, 返回运行结果, 前面约定了 0 为 正常输出
        answer.setStatus(0);
        answer.setStdout(FileUtil.readFile(STDOUT));
        return answer;
    }

    /**
     * 检查代码的安全性
     * @param code 用户提交的代码
     * @return 返回代码是否通过校验
     */
    private boolean checkCodeSafe(String code) {
        List<String> list = new ArrayList<>();
        list.add("Runtime");
        list.add("exec");
        list.add("java.io");
        list.add("java.net");

        for (String string : list) {
            int pos = code.indexOf(string);
            if (pos >= 0) {
                return false;
            }
        }
        return true;
    }
}
