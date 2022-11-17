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

// 这个类来 真正的 处理整个流程, 并返回最终的运行答案
public class Task {
    private String WORD_DIR = null;
    private String CLASS = null;
    private String CODE = null;
    private String COMPILE_STDERR = null;
    private String RUN_STDERR = null;
    private String STDOUT = null;

    public Task(){
        this.WORD_DIR = "./tmp/" + UUID.randomUUID().toString() + "/";
        this.CLASS = "Solution";
        this.CODE = this.WORD_DIR + "Solution.java";
        this.COMPILE_STDERR = this.WORD_DIR + "compile_stderr.txt";
        this.RUN_STDERR = this.WORD_DIR + "run_stderr.txt";
        this.STDOUT = this.WORD_DIR + "stdout.txt";
    }

    public Answer compileAndRun(Question question){
        // 创建目录
        Answer answer = new Answer();
        File workDir = new File(WORD_DIR);
        if (!workDir.exists()) {
            // 这个方法可以创建递归创建多级目录
            workDir.mkdirs();
        }

        if(!checkCodeSafe(question.getCode())){
            System.out.println("用户提交危险代码! ");
            answer.setStatus(3);
            answer.setReason("您提交的代码可能存在危险操作, 禁止运行!");
            return answer;
        }

        // 得到整个完整代码, 并写入到 CODE 文件中
        FileUtil.writeFile(CODE,question.getCode());

        // 编译代码
        String compileCommand = String.format("javac -encoding utf8 %s -d %s",CODE, WORD_DIR);
        CompileAndRun.run(1, compileCommand, COMPILE_STDERR, null);
        // 根据编译后的 COMPILE_STDERR 文件是否为空, 来判断 编译是否出错
        String compileError = FileUtil.readFile(COMPILE_STDERR);
        if(!compileError.equals("")){
            // 该文件不为空, 表明出错
            answer.setStatus(1);
            answer.setReason(compileError);
            return answer;
        }

        // 运行代码
        String runCommand = String.format("java -classpath %s %s", WORD_DIR, CLASS);
        CompileAndRun.run(2, runCommand, RUN_STDERR, STDOUT);

        // 根据编译后的 RUN_STDERR 文件是否为空, 来判断 运行是否出错
        String runError = FileUtil.readFile(RUN_STDERR);
        if(!runError.equals("")){
            answer.setStatus(2);
            answer.setReason(runError);
            return answer;
        }

        // 到这里, 表明没有出现问题, 返回运行结果
        answer.setStatus(0);

        answer.setStdout(FileUtil.readFile(STDOUT));
        return answer;
    }

    private boolean checkCodeSafe(String code) {
        List<String> list = new ArrayList<>();
        list.add("Runtime");
        list.add("exec");
        list.add("java.io");
        list.add("java.net");

        for (String string: list){
            int pos = code.indexOf(string);
            if(pos >= 0){
                return false;
            }
        }
        return true;
    }

//    public static void main(String[] args) {
//        Question question = new Question();
//        question.setCode("public class Solution{\n" +
//                "    public static void main(String[] args){\n" +
//                "        System.out.println(new Solution().add(1 , 2));\n" +
//                "    }\n" +
//                "    \n" +
//                "    public int add(int ErrorAdvice, int b){\n" +
//                "        return ErrorAdvice + b;\n" +
//                "    }\n" +
//                "}");
//        System.out.println(new Task().compileAndRun(question));
//    }
}
