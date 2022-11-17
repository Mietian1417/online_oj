package com.example.demo.param;

import javax.validation.GroupSequence;


/**
 * @GroupSequence 定义一个字段上的多个注解的执行顺序
 * 从这个接口参数的左边依次向右边执行
 * 先执行 First, 再 Second,最后 Third
 */
@GroupSequence({GroupSeq.First.class, GroupSeq.Second.class, GroupSeq.Third.class, GroupSeq.Fourth.class, GroupSeq.Fifth.class})
public interface GroupSeq {
    interface First {
    }

    interface Second {
    }

    interface Third {

    }

    interface Fourth {

    }

    interface Fifth {

    }
}