public class Solution{
    public int getLeastCommonMultiple(int a, int b){
        return 12;
    }
    public static void main(String[] args){
        Solution solution = new Solution();
        if(solution.getLeastCommonMultiple(5, 7) == 35){
            System.out.println("test1 ok!");
        }else{
            System.out.println("test1 fail!");
        }
        
        if(solution.getLeastCommonMultiple(5, 10) == 10){
            System.out.println("test2 ok!");
        }else{
            System.out.println("test2 fail!");
        }
        
        if(solution.getLeastCommonMultiple(12, 16) == 48){
            System.out.println("test3 ok!");
        }else{
            System.out.println("test3 fail!");
        }
        
        if(solution.getLeastCommonMultiple(14, 35) == 70){
            System.out.println("test4 ok!");
        }else{
            System.out.println("test4 fail!");
        }
        
        if(solution.getLeastCommonMultiple(8, 12) == 24){
            System.out.println("test5 ok!");
        }else{
            System.out.println("test5 fail!");
        }
        
        if(solution.getLeastCommonMultiple(5, 5) == 5){
            System.out.println("test6 ok!");
        }else{
            System.out.println("test6 fail!");
        }
        
        if(solution.getLeastCommonMultiple(25, 15) == 75){
            System.out.println("test7 ok!");
        }else{
            System.out.println("test7 fail!");
        }
        
        if(solution.getLeastCommonMultiple(13, 7) == 91){
            System.out.println("test8 ok!");
        }else{
            System.out.println("test8 fail!");
        }
        
        if(solution.getLeastCommonMultiple(5, 3) == 15){
            System.out.println("test9 ok!");
        }else{
            System.out.println("test9 fail!");
        }
        
        if(solution.getLeastCommonMultiple(15, 27) == 135){
            System.out.println("test10 ok!");
        }else{
            System.out.println("test10 fail!");
        }
    }
}