package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeWriter {
    private FileWriter fWriter;
    private BufferedWriter bWriter;
    
    //常见的指令
    private final String SPADD1="@SP\nM=M+1";
    private final String SPSUB1="@SP\nM=M-1";
    private static long NumberOfComparisons=0;//比较次数，用来给标签做唯一标识
    private final String True="-1",False="0";
    //将命令分类
    private Map<String,String> BinocularOperators;//双目运算符
    private Map<String,String> MonadicOperators; ///单目运算符
    private Map<String,String> ComparisonOperators;//比较运算符

    CodeWriter(String outfile) throws IOException{
        File dir=new File("D:\\深大生活\\作业\\java\\03 作业\\VMTranslator\\src\\resources");
        fWriter=new FileWriter(dir+"\\"+outfile);
        bWriter=new BufferedWriter(fWriter);
        BinocularOperators=new HashMap<String,String>();
        BinocularOperators.put("add", "+");
        BinocularOperators.put("sub","-");
        BinocularOperators.put("and", "&");
        BinocularOperators.put("or", "|");
        MonadicOperators=new HashMap<String,String>();
        MonadicOperators.put("neg", "-");
        MonadicOperators.put("not", "!");
        ComparisonOperators=new HashMap<String,String>();
        ComparisonOperators.put("eq", "JEQ");
        ComparisonOperators.put("gt", "JGT");
        ComparisonOperators.put("lt", "JLT");
    }

    void writeArithemtic(String command) throws IOException{
        if(BinocularOperators.containsKey(command)){
            String operator=BinocularOperators.get(command);
            writeBinomialArithmeticStatements(operator);
        }else if(MonadicOperators.containsKey(command)){
            String operator=MonadicOperators.get(command);
            writeMonadicArithmeticStatements(operator);
        }else if(ComparisonOperators.containsKey(command)){
            String comparsion=ComparisonOperators.get(command);
            writeCompareStatements(comparsion);
        }
    }
    void writePushPop(CommandTpye command,String segment,int index) throws IOException{
        if(command==CommandTpye.push){
            if(segment.equals("constant")){
            //*sp=index
                //D=i 
                newline("@"+String.valueOf(index)+"\nD=A");
                //*sp=D
                newline("@SP\nA=M\nM=D");
            //sp++
                newline(SPADD1);
                
            }
        }
    }
    void close() throws IOException{
        bWriter.close();
        fWriter.close();
    }
    
    private void newline(String instruction) throws IOException{
        bWriter.write(instruction);
        bWriter.newLine();
    }
    
    private void writeBinomialArithmeticStatements(String operator) throws IOException{
        //sp--
        newline(SPSUB1);
        //D=*SP  D=y
        newline("A=M\nD=M");
        //SP-- 
        newline(SPSUB1);
        //X=X OP Y  M代表x,D存储Y的值 
        if(operator=="+")
            newline("A=M\nM=D+M");
        newline("A=M\nM=M"+operator+"D");
        //SP++
        newline(SPADD1);
    }
    private void writeMonadicArithmeticStatements(String operator) throws IOException{
         //SP--
        newline(SPSUB1);
        //*SP= OP *SP  Y= OP Y
        newline("A=M\nM="+operator+"M");
        //SP++
        newline(SPADD1);
    }
    private void writeCompareStatements(String Comparison) throws IOException{
        NumberOfComparisons++;
        String TRUE="TRUE"+String.valueOf(NumberOfComparisons);
        String DONE="DONE"+String.valueOf(NumberOfComparisons);
        //sp--
        newline(SPSUB1);
        //D=*sp  D=y
        newline("A=M\nD=M");
        //sp-- 
        newline(SPSUB1);
        //if(x COM Y)goto TRUE 
        newline("A=M\nD=M-D\n@"+TRUE+"\nD;"+Comparison);
        //*sp =false
        newline("@SP\nA=M\nM="+False);
        //goto done
        newline("@"+DONE+"\n0;JMP");
        //(TRUE)
        newline("("+TRUE+")");
        //*sp=true
        newline("@SP\nA=M\nM="+True);
        //(DONE)
        newline("("+DONE+")");
        //sp++
        newline(SPADD1);
    }
}
