package com.example.demo.dao;

import com.example.demo.model.Problem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:59
 */

@Mapper
public interface ProblemMapper {
    List<Problem> getProblemList();

    Problem getProblemById(Integer problemId);

    String getReferenceAnswer(Integer problemId);

    int addProblem(String title, String level, String description,
                   String templateCode, String testCode, String referenceCode);

    Integer deleteProblemById(Integer problemId);
}
