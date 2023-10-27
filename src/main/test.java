package main;

public class test {
    public static void main(String[] args) {
        String commond1="  push  const   124";
        String commond2="  add";
        String[]arg1=commond1.trim().split("\\s+");
        String[]arg2=commond2.trim().split("\s+");
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
