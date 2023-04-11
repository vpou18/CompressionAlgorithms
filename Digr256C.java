import java.io.*;
import java.nio.file.*;
import java.util.*;
public class Digr256C {
    public static void main(String[] args) throws Exception{
        File Digr256 = new File("Digr256.txt");
        if(Files.notExists(Paths.get(args[0])) || Files.notExists(Paths.get("Digr256.txt"))){
            System.out.println("Error: Issue with arguments!");
            System.exit(1);
        }

        Map<String, String> dict = new HashMap<>();

        Scanner reader = new Scanner(Digr256);
        while (reader.hasNext()) {
            String[] current = reader.nextLine().split("\t");
            if(current.length > 1){
                dict.put(current[1],current[0]);
            }
        }
        reader.close();
        File book = new File(args[0]);
        Scanner scanner = new Scanner(book);
        String bookString = "";
        while (scanner.hasNext()) {
            bookString += scanner.nextLine();
            if (scanner.hasNext()) {
                bookString += "\n";
            }
        }
        scanner.close();
        String name = args[0].substring(0,args[0].length()-4);
        String path = name.substring(0,5);
        name = name.substring(6,name.length());
        writeEncodedBook(bookString.toCharArray(), dict, name, path);

    }
    private static void writeEncodedBook(char[] book, Map<String, String> encoding, String name, String pathToOutput) {
        List<String> encoded = new ArrayList<>();

        for(int i =0;i< book.length;i++){
            char curr = book[i];
            String a = "" + curr;
            if(i+1 < book.length){
                char next = book[i+1];
                a += next;
            }
            if(encoding.get(a) != null){
                encoded.add(encoding.get(a));
            }
            else{
                if(encoding.get("" + curr) == null){
                    encoded.add(encoding.get("@"));
                }
                else{
                    encoded.add(encoding.get("" + curr));
                }
            }
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
                FileOutputStream fileOutputStream = new FileOutputStream(name + ".digr256");
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

