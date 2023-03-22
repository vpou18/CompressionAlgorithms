import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class GenerateHuffDict {
    public static void main(String[] args) throws FileNotFoundException {
        File dir = new File("books");
        File[] directoryListing = dir.listFiles();
        Map<Character, Integer> dictionary = new HashMap<>();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.out.println(child.getName());
                // Do something with child
                Scanner sc = new Scanner(child);

                while (sc.hasNextLine()) {
                    for (Character c : sc.nextLine().toCharArray()) {
                        if (!dictionary.containsKey(c)) {
                            dictionary.put(c, 0);
                        }
                        dictionary.put(c, dictionary.get(c) + 1);
                    }
                }
                sc.close();
            }
        }

        try {
            FileWriter myWriter = new FileWriter("HuffDict.txt");
            for (Map.Entry<Character, Integer> entry : dictionary.entrySet()) {
                Character key = entry.getKey();
                Integer val = entry.getValue();
                myWriter.write(String.format("%c %d\n", key, val));
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}