import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class huffD {
    public static void main(String[] args) throws IOException {
        File encodingTxt = new File("Encoding.txt");
        Map<String, String> encoding = new HashMap<>();

        Scanner scanner = new Scanner(encodingTxt);
        while (scanner.hasNext()) {
            String[] current = scanner.nextLine().split(" ");
            encoding.put(current[1], current[0]);
        }
        scanner.close();

        File book = new File("encoded_book.txt");
        FileInputStream fileInputStream = new FileInputStream(book);
        // String bookString = "";
        // scanner = new Scanner(book);
        // while (scanner.hasNext()) {
        //     String current = scanner.nextLine();
        //     bookString += current;
        //     if (scanner.hasNext()) {
        //         bookString += "\n";
        //     }
        // }
        // scanner.close();
        // byte[] decodedData = Base64.getDecoder().decode(bookString);
        String decodedBinaryString = toBinaryString(fileInputStream.readAllBytes());
        fileInputStream.close();
        writeDecodedBook(decodedBinaryString, encoding);

    }

    private static void writeDecodedBook(String book, Map<String, String> encoding) {
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
        System.out.println(start);
        System.out.println(end);
        System.out.println(book.length());

        String[] toWriteFinal = Arrays.copyOfRange(decoded, 0, i);
        try {
            FileWriter myWriter = new FileWriter("decoded_book.txt");
            myWriter.write(String.join("", toWriteFinal));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
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
