package main;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private FileReader fReader;
    private BufferedReader bufferedReader;
    private String arg1=null;
    private String arg2=null;
    private CommandTpye commandType=null;
    
    Parser(String input) throws FileNotFoundException{
        File dir=new File("D:\\深大生活\\作业\\java\\03 作业\\VMTranslator\\src\\resources");
        fReader=new FileReader(dir+"\\"+input);
        bufferedReader=new BufferedReader(fReader);
    }

    Boolean hasMoreCommands() throws IOException{
        return bufferedReader.ready();
    }
    void adance() throws IOException{
         String input =bufferedReader.readLine();

        //使用正则表达式匹配输入行中的空白行，注释行，便于后续跳过
        String p1 = "^\\s*//.*$|^\\s*$";
        Pattern regex = Pattern.compile(p1);
        Matcher matcher = regex.matcher(input);

        //如果是空白行，注释行则跳过
        while(matcher.find()){
            if(bufferedReader.ready()){
                input=bufferedReader.readLine();
            }else return;
            matcher.reset(input);
        }

        //命令中去除注释行
        String command=input.replaceAll("//.*", "");
        
        //根据空白行划分
        String arg[]=command.trim().split("\\s+");
        if(arg.length==1){
            commandType=CommandTpye.ArithemticOrLogical;
            arg1=arg[0];
            arg2=null;
        }else if(arg.length==3){
            if(arg[0]=="push"){
                commandType=CommandTpye.push;
            }else if(arg[0]=="pop"){
                commandType=CommandTpye.pop;
            }
            arg1=arg[1];
            arg2=arg[2];
        }
    }
    CommandTpye commandTpye(){
        return commandType;
    }
    String arg1(){
        return arg1;
    }
    String arg2(){
        return arg2;
    }
    void close() throws IOException{
        fReader.close();
        bufferedReader.close();
    }
}
