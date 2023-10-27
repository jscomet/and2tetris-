package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    //push、pop语句进行分类
    private Map<String,String> ASegment;
    private Map<String,Function<String,String>> BSegment;

    CodeWriter(String outfile) throws IOException{
        File file=new File("D:\\深大生活\\作业\\java\\03 作业\\VMTranslator\\src\\resources\\"+outfile);
        fWriter=new FileWriter(file);
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
        ASegment=new HashMap<String,String>();
        ASegment.put("local", "LCL");
        ASegment.put("argument", "ARG");
        ASegment.put("this", "THIS");
        ASegment.put("that", "THAT");
        ASegment.put("temp", "5");
        BSegment=new HashMap<String,Function<String,String>>();
        String filename=file.getName();
        Function<String,String> staticvariable=i->filename.substring(0,filename.lastIndexOf(".")+1)+i;
        Function<String,String> pointervariable=i->i=i.equals("0")?"THIS":i.equals("1")?"THAT":null;
        BSegment.put("static", staticvariable);
        BSegment.put("pointer",pointervariable);
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
    void writePush(String segment,int index) throws IOException{
        if(segment.equals("constant")){
            writePushConstant(String.valueOf(index));
        }else if(ASegment.containsKey(segment)){
            String predefine=ASegment.get(segment);
            writePushBySegmentAndIndex(predefine,String.valueOf(index));
        }else if(BSegment.containsKey(segment)){
            Function<String,String> getVariable=BSegment.get(segment);
            String var=getVariable.apply(String.valueOf(index));
            // newline("-------push"+var);
            writePushByIndex(var);
            // newline("-------");
        }
    }
    void writePop(String segment,int index) throws IOException{
        if(ASegment.containsKey(segment)){
            String predefine=ASegment.get(segment);
            writePopBySegmentAndIndex(predefine,String.valueOf(index));
        }else if(BSegment.containsKey(segment)){
            Function<String,String> getVariable=BSegment.get(segment);
            String var=getVariable.apply(String.valueOf(index));
            // newline("-------pop"+var);
            writePopByIndex(var);
            // newline("-------");
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
    

    private void writePushConstant(String index) throws IOException{
         //*sp=var
        //D=i
        newline("@"+index+"\nD=A");
            //*sp=D
        newline("@SP\nA=M\nM=D");
        //sp++
        newline(SPADD1);
    }
    
    private void writePushBySegmentAndIndex(String predefine,String index) throws IOException{
        //addr=predefine+index
        newline("@"+index+"\nD=A");
        if(predefine.equals("5"))
            newline("@"+predefine+"\nD=D+A");
        else newline("@"+predefine+"\nD=D+M");
        newline("@addr\nM=D");
        //*SP=*addr
        newline("A=M\nD=M");
        newline("@SP\nA=M\nM=D");
        //SP++
        newline(SPADD1);
    }
    private void writePopBySegmentAndIndex(String predefine,String index)throws IOException{
        //addr=predefine+index
        newline("@"+index+"\nD=A");
        if(predefine.equals("5"))
            newline("@"+predefine+"\nD=D+A");
        else newline("@"+predefine+"\nD=D+M");
        newline("@addr\nM=D");
        //SP--
        newline(SPSUB1);
        //*addr =*p
        newline("A=M\nD=M");
        newline("@addr\nA=M\nM=D");
    }
    
    private void writePushByIndex(String variable) throws IOException{
        //*sp=var
        //D=variable
        newline("@"+variable+"\nD=M");
            //*sp=D
        newline("@SP\nA=M\nM=D");
        //sp++
        newline(SPADD1);
    }
    private void writePopByIndex(String variable)throws IOException{
        //SP--
        newline(SPSUB1);
        //var=*SP 
        newline("A=M\nD=M");
        newline("@"+variable+"\nM=D");
    }
   
}
