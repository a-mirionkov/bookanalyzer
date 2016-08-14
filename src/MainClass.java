import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClass {
    public static void main(String[] args) throws InterruptedException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BookAnalyzer bookAnalyzer;
        System.out.println("Enter book path (like C:/Files/Books/book.txt) and percents (1-100).");
        Thread.sleep(100);
        System.out.println();

        while (true) {
            System.out.print("Book path: ");
            String bookName = reader.readLine();
            if (bookName.isEmpty() || bookName.equals(" ") || bookName.equals("")) {
                break;
            }
            System.out.println();
            System.out.print("Percents (skip for default value - 60): ");
            String percents = reader.readLine();
            if (!percents.isEmpty() && percents.matches("[0-9]{1,3}")) //percents validation
            {
                bookAnalyzer = new BookAnalyzer(bookName, Integer.parseInt(percents));
            } else {
                bookAnalyzer = new BookAnalyzer(bookName);
            }

            bookAnalyzer.run();
            bookAnalyzer.join();
        }
    }
}


