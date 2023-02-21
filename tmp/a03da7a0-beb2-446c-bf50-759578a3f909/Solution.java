import java.util.Stack;

public class Solution{
    public boolean chkParenthesis(String str){
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '('){
                stack.push(ch);
            }else if(ch == ')'){
                if(!stack.empty()){
                    stack.pop();
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return stack.empty();          
    }
    public static void main(String[] args){
        Solution solution = new Solution();
        if(solution.chkParenthesis("(())") == true){
            System.out.println("test1 ok!");
        }else{
            System.out.println("test1 fail!");
        }
        
        if(solution.chkParenthesis("(())a") == false){
            System.out.println("test2 ok!");
        }else{
            System.out.println("test2 fail!");
        }
        
        if(solution.chkParenthesis("(a())") == false){
            System.out.println("test3 ok!");
        }else{
            System.out.println("test3 fail!");
        }
        
        if(solution.chkParenthesis("((()))") == true){
            System.out.println("test4 ok!");
        }else{
            System.out.println("test4 fail!");
        }
        
        if(solution.chkParenthesis(")(())") == false){
            System.out.println("test5 ok!");
        }else{
            System.out.println("test5 fail!");
        }
        
        if(solution.chkParenthesis("(()))") == false){
            System.out.println("test6 ok!");
        }else{
            System.out.println("test6 fail!");
        }
        
        if(solution.chkParenthesis("((a))") == false){
            System.out.println("test7 ok!");
        }else{
            System.out.println("test7 fail!");
        }
        
        if(solution.chkParenthesis("(()()))") == false){
            System.out.println("test8 ok!");
        }else{
            System.out.println("test8 fail!");
        }
        
        if(solution.chkParenthesis("(()))a))") == false){
            System.out.println("test9 ok!");
        }else{
            System.out.println("test9 fail!");
        }
        
        if(solution.chkParenthesis("((()))()()") == true){
            System.out.println("test10 ok!");
        }else{
            System.out.println("test10 fail!");
        }
    }
}