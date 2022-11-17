package com.example.demo.vo;

import com.example.demo.model.Problem;
import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-17
 * Time: 10:08
 *
 * @author 陈子豪
 */

@Setter
@Getter
public class IsAdminAndProblem {
    public int isAdmin;
    public Problem problem;
}
