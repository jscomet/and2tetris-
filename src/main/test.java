package main;

public class test {
    public static void main(String[] args) {
        String commond1="";
        String commond2="999333";
        int a=Integer.valueOf(commond2);
        System.out.println(a);
        String[]arg1=commond1.trim().split("\\s+");
        String[]arg2=commond2.trim().split("\\s+");
        System.out.println("arg1:"+arg1.length);
        for (int i = 0; i < arg1.length; i++) {
            System.out.println(arg1[i]);
            
        }
        System.out.println("arg2:"+arg2.length);
        for (int i = 0; i < arg2.length; i++) {
            System.out.println(arg2[i]);
        }
    }
} 
