import java.io.*;
import java.nio.file.*;
import java.util.*;
public class Digr256C {
    public static void main(String[] args) throws Exception{
        File Digr256 = new File("Digr256.txt");
        if(Files.notExists(Paths.get(args[0])) || Files.notExists(Paths.get("Digr256.txt"))){
            System.out.println("Error: Issue with arguments! The first argument indicates the " +
            "size of the dictionary(must be >=96) and all arguments after must " +
            "be paths to txt files to be encoded.");
            System.exit(1);
        }

        BufferedReader br = new BufferedReader(new FileReader(Digr256));
        String st;
        Map<String, String> dict = new HashMap<>();
        // while ((st = br.readLine()) != null){
        //     String[] code = st.split("\t");
        //     if(code.length == 2){
        //         dict.put(code[1],code[0]);
        //         System.out.println(1);
        //     }
        //     System.out.println(1);
        // }
        Scanner reader = new Scanner(Digr256);
        StringBuilder bb = new StringBuilder();
        while (reader.hasNext()) {
            String[] current = reader.nextLine().split("\t");
            if(current.length > 1){
                dict.put(current[1],current[0]);
            }
            if (reader.hasNext()) {
                bb.append("\n");
            }
        }
        reader.close();
        File book = new File(args[0]);
        Scanner scanner = new Scanner(book);
        StringBuilder sb = new StringBuilder();
        String bookString = "";
        while (scanner.hasNext()) {
            bookString += scanner.nextLine();
            // System.out.println(current[0]);
            if (scanner.hasNext()) {
                sb.append("\n");
            }
        }
        bookString = sb.toString();
        scanner.close();
        String bitted = "";
        for(int i =0;i<bookString.length();i++){
            char curr = bookString.charAt(i);
            String a = null;
            if(i+1 < bookString.length()){
                char next = bookString.charAt(i+1);
                a = "" + curr + next;
            }
            if(dict.get(a) != null){
                bitted += dict.get(a);
            }
            else{
                bitted += dict.get("" + curr);
            }
        }
        String name = args[0].substring(0,args[0].length()-4);
        String path = name.substring(0,5);
        name = name.substring(6,name.length());
        writeEncodedBook(bitted.toCharArray(), dict, name, path);

    }
    private static void writeEncodedBook(char[] book, Map<String, String> encoding, String name, String pathToOutput) {
        String[] encoded = new String[book.length];
        int index = 0;
        for (char c : book) {
            String current = c + "";
            if (!(c > 32 && c < 128)) {
                current = String.valueOf(Integer.valueOf(c));
            }
            encoded[index] = encoding.get(current);
            index += 1;
        }
        String[] encodedJoined = String.join("", encoded).split("(?<=\\G.{" + 8 + "})");
        byte[] binaryData = toByteArray(String.join(" ", encodedJoined));
        try {
            if (!pathToOutput.isEmpty()) {
                File theDir = new File(pathToOutput);
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(pathToOutput + "//" + name + ".digr256");
                fileOutputStream.write(binaryData);
                fileOutputStream.close();
            } else {
                FileOutputStream fileOutputStream = new FileOutputStream(name + ".huff");
                fileOutputStream.write(binaryData);
                fileOutputStream.close();
            }
            System.out.printf("Successfully wrote the compressed %s file.\n", name);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(String binaryString) {
        String[] tokens = binaryString.split("\\s+");
        byte[] data = new byte[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = (byte) Integer.parseInt(tokens[i], 2);
        }
        return data;
    }
}

