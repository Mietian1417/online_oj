public class Solution {
              
    public String stringDeDuplication(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (!stringBuilder.toString().contains(ch + "")) {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }
              
    public static void main(String[] args) {
        Solution solution = new Solution();
        if (solution.stringDeDuplication("helloworld").equals("helowrd")) {
            System.out.println("test1 ok! ");
        } else {
            System.out.println("test1 fail!");
        }

        if (solution.stringDeDuplication("aabbc").equals("abc")) {
            System.out.println("test2 ok! ");
        } else {
            System.out.println("test2 fail!");
        }

        if (solution.stringDeDuplication("iamastudent").equals("iamstuden")) {
            System.out.println("test3 ok! ");
        } else {
            System.out.println("test3 fail!");
        }

        if (solution.stringDeDuplication("abcd").equals("abcd")) {
            System.out.println("test4 ok! ");
        } else {
            System.out.println("test4 fail!");
        }

        if (solution.stringDeDuplication("aabbbbbbc").equals("abc")) {
            System.out.println("test5 ok! ");
        } else {
            System.out.println("test5 fail!");
        }

        if (solution.stringDeDuplication("1234565427772").equals("1234567")) {
            System.out.println("test6 ok! ");
        } else {
            System.out.println("test6 fail!");
        }

        if (solution.stringDeDuplication("12asd222a444d").equals("12asd4")) {
            System.out.println("test7 ok! ");
        } else {
            System.out.println("test7 fail!");
        }

        if (solution.stringDeDuplication("ooo").equals("o")) {
            System.out.println("test8 ok! ");
        } else {
            System.out.println("test8 fail!");
        }

        if (solution.stringDeDuplication("beautiful").equals("beautifl")) {
            System.out.println("test9 ok! ");
        } else {
            System.out.println("test9 fail!");
        }

        if (solution.stringDeDuplication("111211321111411").equals("1234")) {
            System.out.println("test10 ok! ");
        } else {
            System.out.println("test10 fail!");
        }
    }
                        
                    
}