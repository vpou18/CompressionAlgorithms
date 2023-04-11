
import java.io.*;
import java.nio.file.*;
import java.util.*;
public class Digr256D {
    public static void main(String[] args) throws Exception{
        File Digr256 = new File("Digr256.txt");
        if(Files.notExists(Paths.get(args[0])) || Files.notExists(Paths.get("Digr256.txt"))){
            System.out.println("Error: Issue with arguments! The first argument indicates the " +
            "size of the dictionary(must be >=96) and all arguments after must " +
            "be paths to txt files to be encoded.");
            System.exit(1);
        }

        Map<String, String> dict = new HashMap<>();
        
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
        String name = args[0].substring(0,args[0].length()-8);
        String path = name.substring(0,5);
        name = name.substring(6,name.length());
        System.out.println(path);
        writeDecodedBook(bitted, dict, name, path);
    }
    private static void writeDecodedBook(String book, Map<String, String> encoding, String name, String pathToOutput) {
        String[] decoded = new String[book.length()];
        int i = 0;
        int start = 0;
        int end = 1;
        while (start < book.length() && end <= book.length()) {
            String curr = book.substring(start, end);
            if (encoding.containsKey(curr)) {
                String res = encoding.get(curr);
                if (res.length() > 1) {
                    res = "" + (char) Integer.parseInt(res);
                }
                decoded[i] = res;
                start = end;
                end = start + 1;
                i += 1;
            } else {
                end += 1;
            }
        }
        String[] toWriteFinal = Arrays.copyOfRange(decoded, 0, i);
        try {
            if (!pathToOutput.isEmpty()) {
                File theDir = new File(pathToOutput);
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }
            }
            FileWriter myWriter = new FileWriter(pathToOutput + "//" + name + ".txt");
            myWriter.write(String.join("", toWriteFinal));
            myWriter.close();
            System.out.printf("Successfully wrote the decompressed %s file.\n", name);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String toBinaryString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            builder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return builder.toString();
    }
}

    

