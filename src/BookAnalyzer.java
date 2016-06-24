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


        Map<String, Integer> myWords = new HashMap<>(); //пары значений слово - кол-во повторений
        ArrayList<String[]> arraysOfWords = new ArrayList<>();
        ArrayList<String> words = new ArrayList<>(); // слова
        ArrayList<Integer> countWords = new ArrayList<>(); //количество повторений каждого слова

        System.out.println(book);

        int totalWords = 0; //общее колиечество слов во всём тексте
        try {

            BufferedReader myBook = new BufferedReader(new FileReader(book));

            while (myBook.ready()) {
                String s = myBook.readLine();           //считываем весь текст построчно
                if (!s.matches("\\s") && !s.isEmpty())
                    arraysOfWords.add(s.split(" "));
            }
        } catch (IOException fileNotFound) {
            System.out.println("File not found");
        }

        System.out.println(arraysOfWords.size() + " lines in the book");


        for (int i = 0; i < arraysOfWords.size(); i++) {//перебор массивов
            String[] currentLine = arraysOfWords.get(i);
            for (String word : currentLine) {//перебор элементов массива

                if (word.matches("[\\p{Punct}]{0,}[a-zA-Z]{1,}[-']{0,1}[a-zA-Z]{0,}[\\p{Punct}]{0,}")) {
                    word = word.toLowerCase();
                    word = word.replaceAll("[\\p{Punct}&&[^-']]", " ").trim();

                    if (!myWords.containsKey(word))        //каждое слово заносим в карту
                        myWords.put(word, 1);
                    else
                        myWords.put(word, myWords.get(word) + 1); //если слово уже есть в карте - увеличиваем его value на 1
                    totalWords++; //увеличиваем счетчик слов с каждым словом, добавленным в карту

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

        double mostUsableWordsCount = totalWords *  ((double)percent/100); //Находим слова, которые являются 60% всего текста.

        System.out.println("Следующие слова покрывают " + percent + "% текста:");
        try {
            FileOutputStream resultWriter = new FileOutputStream(new File(resultPathAndName));
            resultWriter.write("#\tкол-во\tслово\r\n\r\n".getBytes());
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

