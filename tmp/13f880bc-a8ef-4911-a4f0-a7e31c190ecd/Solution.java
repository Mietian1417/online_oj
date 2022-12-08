public class Solution {
    
    public int powerOfTwo(int n) {
      
        return 1;
    }
    
public static void main(String[] args) {
        Solution solution = new Solution();
        if (solution.powerOfTwo(1) == (int) Math.pow(2, 1)) {
            System.out.println("test1 ok!");
        } else {
            System.out.println("test1 fail!");
        }

        if (solution.powerOfTwo(3) == (int) Math.pow(2, 3)) {
            System.out.println("test2 ok!");
        } else {
            System.out.println("test2 fail!");
        }

        if (solution.powerOfTwo(5) == (int) Math.pow(2, 5)) {
            System.out.println("test3 ok!");
        } else {
            System.out.println("test3 fail!");
        }

        if (solution.powerOfTwo(10) == (int) Math.pow(2, 10)) {
            System.out.println("test4 ok!");
        } else {
            System.out.println("test4 fail!");
        }

        if (solution.powerOfTwo(0) == (int) Math.pow(2, 0)) {
            System.out.println("test5 ok!");
        } else {
            System.out.println("test5 fail!");
        }

        if (solution.powerOfTwo(15) == (int) Math.pow(2, 15)) {
            System.out.println("test6 ok!");
        } else {
            System.out.println("test6 fail!");
        }

        if (solution.powerOfTwo(9) == (int) Math.pow(2, 9)) {
            System.out.println("test7 ok!");
        } else {
            System.out.println("test7 fail!");
        }

        if (solution.powerOfTwo(-1) == (int) Math.pow(2, -1)) {
            System.out.println("test8 ok!");
        } else {
            System.out.println("test8 fail!");
        }

        if (solution.powerOfTwo(7) == (int) Math.pow(2, 7)) {
            System.out.println("test9 ok!");
        } else {
            System.out.println("test9 fail!");
        }

        if (solution.powerOfTwo(-4) == (int) Math.pow(2, -4)) {
            System.out.println("test10 ok!");
        } else {
            System.out.println("test10 fail!");
        }
    }
                        
                    
}