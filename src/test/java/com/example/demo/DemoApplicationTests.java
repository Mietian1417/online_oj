package com.example.demo;


import com.example.demo.model.Problem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void test() {
        String str = "./tmp/536b8613-3b2c-4b5c-8d0b-901fe4f4c1f2/Solution.java:5: error: missing return statement\n" +
                "    }\n" +
                "    ^\n" +
                "1 error";
        System.out.println(str.substring(str.indexOf("Solution")));
    }
}
