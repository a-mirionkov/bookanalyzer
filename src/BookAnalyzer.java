import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class BookAnalyzer extends Thread {

    private String book;
    private int percent;


    public BookAnalyzer(String book, int percent) {
        this.book = book;
        this.percent = percent;
    }

    public BookAnalyzer(String book) {
        this.book = book;
        this.percent = 60;
    }

    public void run() {

        String resultPath = book.substring(0, book.lastIndexOf('/') + 1);
        String resultName = book.substring(book.lastIndexOf('/') + 1, book.lastIndexOf('.')) + "_words_" +percent+ ".txt";
        String resultPathAndName = resultPath + resultName;


        Map<String, Integer> myWords = new HashMap<>(); //couples - word-amount of reiterations
        ArrayList<String[]> arraysOfWords = new ArrayList<>();
        ArrayList<String> words = new ArrayList<>(); // words
        ArrayList<Integer> countWords = new ArrayList<>(); //amount of reiterations of each word

        System.out.println(book);

        int totalWords = 0; //amount of words in the whole file
        try {

            BufferedReader myBook = new BufferedReader(new FileReader(book));
            while (myBook.ready()) {
                String s = myBook.readLine();           //reading the whole file line by line
                if (!s.matches("\\s") && !s.isEmpty())
                    arraysOfWords.add(s.split(" "));
            }
        } catch (IOException fileNotFound) {
            System.out.println("File not found");
        }

        System.out.println(arraysOfWords.size() + " lines in the book");


        for (int i = 0; i < arraysOfWords.size(); i++) {
            String[] currentLine = arraysOfWords.get(i);
            for (String word : currentLine) {

                if (word.matches("[\\p{Punct}]{0,}[\\p{L}]{1,}[-']{0,1}[\\p{L}]{0,}[\\p{Punct}]{0,}")) {
                    word = word.toLowerCase();
                    word = word.replaceAll("[\\p{Punct}&&[^-']]", " ").trim();

                    if (!myWords.containsKey(word))
                        myWords.put(word, 1);
                    else
                        myWords.put(word, myWords.get(word) + 1); //if word is already exist in the map myWords
                                                                                        // increment its value
                    totalWords++; //increment counter with every added word

                }
            }
        }


        //**********************************************************************************


        for (Map.Entry<String, Integer> entry : myWords.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value != 1) {
                words.add(key);
                countWords.add(value);
            }
        }

        for (int j = 0; j < words.size() - 1; j++) {
            for (int b = 0; b < words.size() - 1; b++) {
                if (countWords.get(b) <= countWords.get(b + 1)) {
                    Collections.swap(countWords, b, b + 1);
                    Collections.swap(words, b, b + 1);

                }
            }
        }

        double mostUsableWordsCount = totalWords *  ((double)percent/100); //Finding words which covers 60% of the file

        System.out.println("Following words covers " + percent + "% of the file:");
        try {
            FileOutputStream resultWriter = new FileOutputStream(new File(resultPathAndName));
            resultWriter.write("#\tamount\tword\r\n\r\n".getBytes());
            int povtory = 0;
            int id = 1;
            for (int a = 0; a < countWords.size(); a++) {
                povtory += countWords.get(a);
                if (povtory >= mostUsableWordsCount)
                    break;
                String lineToPrint = id + "\t" + countWords.get(a) + "\t[" + words.get(a) + "]";
                resultWriter.write((lineToPrint+'\r'+'\n').getBytes());
                System.out.println(lineToPrint);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}