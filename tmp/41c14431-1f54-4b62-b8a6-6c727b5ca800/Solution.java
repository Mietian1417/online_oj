public class Solution{

    public int sub(int a, int b){
        return -1;
    }
    
public static void main(String[] args){
        Solution solution = new Solution();
        if(solution.sub(1,2) == -1){
            System.out.println("test1 ok!");
        }else{
            System.out.println("test1 fail!");
        }

        if(solution.sub(10,2) == 8){
            System.out.println("test2 ok!");
        }else{
            System.out.println("test2 fail!");
        }


    }
}