package com.example.demo.vo;

import com.example.demo.model.Problem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-17
 * Time: 10:06
 *
 * @author 陈子豪
 */

/**
 * vo 类表示返回前端的对象
 */
@Data
public class ViewProblems {
    public int isAdmin;
    public List<Problem> problemList;

    public int startPage;
    public int isLastPage;
}
