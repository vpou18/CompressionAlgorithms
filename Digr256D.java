
import java.io.*;
import java.nio.file.*;
import java.util.*;
public class Digr256D {
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
                dict.put(current[0], current[1]);
            }
        }
        dict.put("00000000", "\n");
        reader.close();

        String name = args[0].substring(0,args[0].length()-8);
        String path = name.substring(0,5);
        name = name.substring(6,name.length());
        try {
            File book = new File(args[0]);
            FileInputStream fileInputStream = new FileInputStream(book);
            String decodedBinaryString = toBinaryString(fileInputStream.readAllBytes());
            fileInputStream.close();
            writeDecodedBook(decodedBinaryString, dict, name, path);
        } catch (IOException e) {
            try {
                File dir = new File(args[0]);

                FileInputStream fileInputStream = new FileInputStream(dir);
                String decodedBinaryString = toBinaryString(fileInputStream.readAllBytes());
                fileInputStream.close();
                System.out.println(decodedBinaryString);
                writeDecodedBook(decodedBinaryString, dict, name, path);

            } catch (FileNotFoundException f) {
                System.out.println(
                        "The file or directory you passed to be decompressed hasn't been found. Please make sure the file exists and that the path to the file is correct");
                System.exit(1);
            }
        }

    }

    private static String getNewCode(String book, int currentBit, int binarySize)
    {
        return book.substring(currentBit, currentBit + binarySize);
    }
    private static void writeDecodedBook(String book, Map<String, String> encoding, String name, String pathToOutput) {
        StringBuilder sb = new StringBuilder();
        int binarySize = 8;
        int currentBit = 0;
        while (currentBit + binarySize <= book.length())
        {
            String nextCode = getNewCode(book, currentBit, binarySize);
            currentBit += binarySize;

            sb.append(encoding.get(nextCode));
        }

        try {
            if (!pathToOutput.isEmpty()) {
                File theDir = new File(pathToOutput);
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }
            }
            FileWriter myWriter = new FileWriter(pathToOutput + "//" + name + ".txt");
            myWriter.write(sb.toString());
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



