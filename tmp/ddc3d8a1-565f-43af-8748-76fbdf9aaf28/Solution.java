public class Solution{
    public int getMaxSubarraySum(int[] array){
        int sum = 0;
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < array.length; i++){
            sum = Math.max(sum + array[i], array[i]);
            if(sum >= max){
                max = sum;
            }
        }
        return max;
    }
    public static void main(String[] args){
        Solution solution = new Solution();
        if(solution.getMaxSubarraySum(new int[]{-1, 2, 1}) == 3){
            System.out.println("test1 ok!");
        }else{
            System.out.println("test1 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{2, 3, -1, 3, -4, 5, -1}) == 8){
            System.out.println("test2 ok!");
        }else{
            System.out.println("test2 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{2, -4, 1, 3, -2, 3}) == 5){
            System.out.println("test3 ok!");
        }else{
            System.out.println("test3 fail!");
        }
           
        if(solution.getMaxSubarraySum(new int[]{1, 2, 3, -1}) == 6){
            System.out.println("test4 ok!");
        }else{
            System.out.println("test4 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{1, 2, 3, -1, 2, -1}) == 7){
            System.out.println("test5 ok!");
        }else{
            System.out.println("test5 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{1, 2, -1, 3, -1}) == 5){
            System.out.println("test6 ok!");
        }else{
            System.out.println("test6 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{1, -2, -1, 3, -1}) == 3){
            System.out.println("test7 ok!");
        }else{
            System.out.println("test7 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{1, -2, 3, -1, 2}) == 4){
            System.out.println("test8 ok!");
        }else{
            System.out.println("test8 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{1, 2, -1, 3}) == 5){
            System.out.println("test9 ok!");
        }else{
            System.out.println("test9 fail!");
        }
        
        if(solution.getMaxSubarraySum(new int[]{1, 2, 3, 4, -1, 2, -4}) == 11){
            System.out.println("test10 ok!");
        }else{
            System.out.println("test10 fail!");
        }
    }
}