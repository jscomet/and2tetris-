package main;

import java.io.IOException;

public class VMTranslator {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("");
        CodeWriter codeWriter = new CodeWriter(null);

        parser.close();
        codeWriter.close();
    }
}