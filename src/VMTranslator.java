public class VMTranslator {
    public static void main(String[] args) {
        Parser parser = new Parser("");
        CodeWriter codeWriter = new CodeWriter(null);

        parser.close();
        codeWriter.close();
    }
}
