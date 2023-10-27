package main;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private FileReader fReader;
    private BufferedReader bReader;
    private String arg1;
    private int arg2;
    private CommandTpye commandType=null;
    
    Parser(String input) throws FileNotFoundException{
        File dir=new File("D:\\深大生活\\作业\\java\\03 作业\\VMTranslator\\src\\resources");
        fReader=new FileReader(dir+"\\"+input);
        bReader=new BufferedReader(fReader);
    }

    Boolean hasMoreCommands() throws IOException{
        return bReader.ready();
    }
    void adance() throws IOException{
        //预处理，找到最近的命令，并将将命令根据空白划分
        String arg[]=preprocess();
        if(arg.length==1){
            //当命令是算术或逻辑运算时，数组长度为1
            commandType=CommandTpye.ArithemticOrLogical;
            arg1=arg[0];
            arg2=-1;
        }else if(arg.length==3){
            //当命令是push或pop时，数组长度为3
            if(arg[0].equals("push")){
                commandType=CommandTpye.push;
            }else if(arg[0].equals("pop")){
                commandType=CommandTpye.pop;
            }
            arg1=arg[1];
            arg2=Integer.parseInt(arg[2]);
        }else{
            //不存在的情况
            commandType=null;
            arg1=null;
            arg2=-1;
        }
    }
    CommandTpye commandTpye(){
        return commandType;
    }
    String arg1(){
        return arg1;
    }
    int arg2(){
        return arg2;
    }
    void close() throws IOException{
        bReader.close();
        fReader.close();
    }
    
    private String[] preprocess() throws IOException{
         String input =bReader.readLine();

        //使用正则表达式匹配输入行中的空白行，注释行，便于后续跳过
        String p1 = "^\\s*//.*$|^\\s*$";
        Pattern regex = Pattern.compile(p1);
        Matcher matcher = regex.matcher(input);

        //如果是空白行，注释行则跳过
        while(matcher.find()){
            if(bReader.ready()){
                input=bReader.readLine();
            }else return null;
            matcher.reset(input);
        }

        //命令中去除注释行
        String command=input.replaceAll("//.*", "");
        //根据空白划分
        return command.trim().split("\\s+");
    }

    public String toString(){
        return "{"+commandType+","+arg1+","+arg2+"}";
    }
}
