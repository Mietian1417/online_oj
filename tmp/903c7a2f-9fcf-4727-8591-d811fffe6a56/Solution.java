public class Solution {
    
    public boolean isPalindromeNumber(int n) {
        String str = String.valueOf(n);
        if (str.length() == 1) {
            return true;
        }
        for (int i = 1; i <= str.length() / 2; i++) {
            char prev = str.charAt(i - 1);
            char next = str.charAt(str.length() - i);
            if (prev != next) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        if (solution.isPalindromeNumber(123)) {
            System.out.println("test1 fail! ");
        } else {
            System.out.println("test1 ok! ");
        }

        if (solution.isPalindromeNumber(12321)) {
            System.out.println("test2 ok! ");
        } else {
            System.out.println("test2 fail! ");
        }

        if (solution.isPalindromeNumber(123454321)) {
            System.out.println("test3 ok! ");
        } else {
            System.out.println("test3 fail! ");
        }

        if (solution.isPalindromeNumber(122332211)) {
            System.out.println("test4 file! ");
        } else {
            System.out.println("test4 ok! ");
        }

        if (solution.isPalindromeNumber(12300321)) {
            System.out.println("test5 ok! ");
        } else {
            System.out.println("test5 fail! ");
        }

        if (solution.isPalindromeNumber(1230321)) {
            System.out.println("test6 ok! ");
        } else {
            System.out.println("test6 fail! ");
        }

        if (solution.isPalindromeNumber(11223112)) {
            System.out.println("test7 fail! ");
        } else {
            System.out.println("test7 ok! ");
        }

        if (solution.isPalindromeNumber(102030201)) {
            System.out.println("test8 ok! ");
        } else {
            System.out.println("test8 fail! ");
        }

        if (solution.isPalindromeNumber(6325236)) {
            System.out.println("test9 ok! ");
        } else {
            System.out.println("test9 fail! ");
        }

        if (solution.isPalindromeNumber(123555321)) {
            System.out.println("test10 ok! ");
        } else {
            System.out.println("test10 fail! ");
        }

    }
                        
                    
}