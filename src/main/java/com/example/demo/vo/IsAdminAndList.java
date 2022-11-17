package com.example.demo.vo;

import com.example.demo.model.Problem;
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

@Setter
@Getter
public class IsAdminAndList {
    public int isAdmin;
    public List<Problem> problemList;
}
