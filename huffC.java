import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class huffC {
    public static void main(String[] args) throws FileNotFoundException {
        File encodingTxt = new File("Encoding.txt");
        Map<String, String> encoding = new HashMap<>();

        Scanner scanner = new Scanner(encodingTxt);
        while (scanner.hasNext()) {
            String[] current = scanner.nextLine().split(" ");
            encoding.put(current[0], current[1]);
        }
        scanner.close();

        File book = new File("books\\The_Count_of_Monte_Cristo.txt");
        String bookString = "";
        scanner = new Scanner(book);
        while (scanner.hasNext()) {
            String current = scanner.nextLine();
            bookString += current;
            if (scanner.hasNext()) {
                bookString += "\n";
            }
        }
        scanner.close();
        writeEncodedBook(bookString.toCharArray(), encoding);
    }

    private static void writeEncodedBook(char[] book, Map<String, String> encoding) {
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
        // String encodedData = Base64.getEncoder().encodeToString(binaryData);
        try {
            // FileWriter fileWriter = new FileWriter("encoded_book.txt");
            // fileWriter.write(encodedData);
            // fileWriter.close();
            FileOutputStream fileOutputStream = new FileOutputStream("encoded_book.txt");
            fileOutputStream.write(binaryData);
            fileOutputStream.close();
            System.out.println("Successfully wrote to the file.");
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
