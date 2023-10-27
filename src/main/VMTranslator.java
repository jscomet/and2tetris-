package main;

import java.io.IOException;

public class VMTranslator {
    public static void main(String[] args) throws IOException {
        String inputfile="MemoryAccess/StaticTest/StaticTest.vm";
        String outputfile=inputfile.substring(0, inputfile.lastIndexOf(".")+1).concat("asm");
        Parser parser = new Parser(inputfile);
        CodeWriter codeWriter = new CodeWriter(outputfile);
        while (parser.hasMoreCommands()) {
            parser.adance();
            if(parser.commandTpye()==CommandTpye.push){
                codeWriter.writePush(parser.arg1(), parser.arg2());
            }else if(parser.commandTpye()==CommandTpye.pop){
                codeWriter.writePop( parser.arg1(), parser.arg2());
            }else if(parser.commandTpye()==CommandTpye.ArithemticOrLogical){
                codeWriter.writeArithemtic(parser.arg1());
            }
        }
        parser.close();
        codeWriter.close();
    }
}