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


/**
 * vo 类表示返回前端的对象
 */
@Setter
@Getter
public class viewProbelm {
    public int isAdmin;
    public Problem problem;
}
