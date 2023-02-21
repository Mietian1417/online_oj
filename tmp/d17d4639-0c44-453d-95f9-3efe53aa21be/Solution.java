public class Solution{
    public int add(int A, int B){
        return 1;
    }
    public static void main(String[] args){
        Solution solution = new Solution();
        if(solution.add(1, 2) == 3){
            System.out.println("test1 ok!");
        }else{
            System.out.println("test1 fail!");
        }
        
        if(solution.add(1, 3) == 4){
            System.out.println("test2 ok!");
        }else{
            System.out.println("test2 fail!");
        }
        
        if(solution.add(1, 5) == 6){
            System.out.println("test3 ok!");
        }else{
            System.out.println("test3 fail!");
        }
        
        if(solution.add(2, 2) == 4){
            System.out.println("test4 ok!");
        }else{
            System.out.println("test4 fail!");
        }
        
        if(solution.add(7, 2) == 9){
            System.out.println("test5 ok!");
        }else{
            System.out.println("test5 fail!");
        }
        
        if(solution.add(10, 2) == 12){
            System.out.println("test6 ok!");
        }else{
            System.out.println("test6 fail!");
        }
        
        if(solution.add(11, 2) == 13){
            System.out.println("test7 ok!");
        }else{
            System.out.println("test7 fail!");
        }
        
        if(solution.add(14, 2) == 16){
            System.out.println("test8 ok!");
        }else{
            System.out.println("test8 fail!");
        }
        
        if(solution.add(5, 2) == 7){
            System.out.println("test9 ok!");
        }else{
            System.out.println("test9 fail!");
        }
        
        if(solution.add(10000, 2) == 10002){
            System.out.println("test10 ok!");
        }else{
            System.out.println("test10 fail!");
        }
    }
}