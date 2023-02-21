    public class Solution{
        public int getMinStep(int number){
            int f1 = 0;
            int f2 = 1;
            while(true){
                int f3 = f1 + f2;
                f1 = f2;
                f2 = f3;
                if(f2 > number){
                    break;
                }
            }
            int minStep = Math.min(f2 - number, number - f1);
            return minStep;
        }
        public static void main(String[] args){
        Solution solution = new Solution();
        if(solution.getMinStep(10) == 2){
            System.out.println("test1 ok!");
        }else{
            System.out.println("test1 fail!");
        }
        
        if(solution.getMinStep(11) == 2){
            System.out.println("test2 ok!");
        }else{
            System.out.println("test2 fail!");
        }
        
        if(solution.getMinStep(20) == 1){
            System.out.println("test3 ok!");
        }else{
            System.out.println("test3 fail!");
        }
        
        if(solution.getMinStep(25) == 4){
            System.out.println("test4 ok!");
        }else{
            System.out.println("test4 fail!");
        }
        
        if(solution.getMinStep(30) == 4){
            System.out.println("test5 ok!");
        }else{
            System.out.println("test5 fail!");
        }
        
        if(solution.getMinStep(40) == 6){
            System.out.println("test6 ok!");
        }else{
            System.out.println("test6 fail!");
        }
        
        if(solution.getMinStep(45) == 10){
            System.out.println("test7 ok!");
        }else{
            System.out.println("test7 fail!");
        }
        
        if(solution.getMinStep(50) == 5){
            System.out.println("test8 ok!");
        }else{
            System.out.println("test8 fail!");
        }
        
        if(solution.getMinStep(13) == 0){
            System.out.println("test9 ok!");
        }else{
            System.out.println("test9 fail!");
        }
        
        if(solution.getMinStep(34) == 0){
            System.out.println("test10 ok!");
        }else{
            System.out.println("test10 fail!");
        }
    }
}