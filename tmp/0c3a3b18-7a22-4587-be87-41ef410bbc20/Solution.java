public class Solution{
    
    public int sub(int a, int b){
        return 1;
    }
public static void main(String[] args){
        Solution solution = new Solution();
        if(solution.sub(1,3) == -1){
            System.out.println("test1 ok!");
        }else{
            System.out.println("test1 fail!");
        }

        if(solution.sub(10,8) == 2){
            System.out.println("test2 ok!");
        }else{
            System.out.println("test2 fail!");
        }
    }
}