public class Solution {
             
    public int reverseOrder(int n) {
        return 1;
    }
          
    public static void main(String[] args) {
        Solution solution = new Solution();
        if (solution.reverseOrder(12) == 21) {
            System.out.println("test1 ok!");
        } else {
            System.out.println("test1 fail!");
        }

        if (solution.reverseOrder(120) == 21) {
            System.out.println("test2 ok!");
        } else {
            System.out.println("test2 fail!");
        }

        if (solution.reverseOrder(121) == 121) {
            System.out.println("test3 ok!");
        } else {
            System.out.println("test3 fail!");
        }

        if (solution.reverseOrder(4320) == 234) {
            System.out.println("test4 ok!");
        } else {
            System.out.println("test4 fail!");
        }

        if (solution.reverseOrder(4526) == 6254) {
            System.out.println("test5 ok!");
        } else {
            System.out.println("test5 fail!");
        }

        if (solution.reverseOrder(12340) == 4321) {
            System.out.println("test6 ok!");
        } else {
            System.out.println("test6 fail!");
        }

        if (solution.reverseOrder(11223) == 32211) {
            System.out.println("test7 ok!");
        } else {
            System.out.println("test7 fail!");
        }

        if (solution.reverseOrder(123012) == 210321) {
            System.out.println("test8 ok!");
        } else {
            System.out.println("test8 fail!");
        }

        if (solution.reverseOrder(50603) == 30605) {
            System.out.println("test9 ok!");
        } else {
            System.out.println("test9 fail!");
        }

        if (solution.reverseOrder(26500) == 562) {
            System.out.println("test10 ok!");
        } else {
            System.out.println("test10 fail!");
        }
    }
                        
                    
}