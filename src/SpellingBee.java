import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    public void makeWords(String part, String str)
    {
        words.add(part);
        if (str.length() == 0)
        {
            return;
        }

        for (int i = 0; i < str.length(); i++)
        {
            // 'a' is the first character of the new string
            String a = str.substring(i, i+1);
            // 'b' is the first portion of the string up until 'a'
            String b = str.substring(0, i);
            // 'c' is the second portion of the string
            String c = str.substring(i+1);
            makeWords(part + a, b + c);
        }

        System.out.println(words);
    }



    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        int low = 0;
        int high = words.size() - 1;
        sortWords(low, high);
    }
    public ArrayList<String> sortWords(ArrayList<String> toSort)
    {

        int numWords = toSort.size();
        if (numWords == 1)
        {
            return words;
        }

        else
        {

            ArrayList<String> arr1 = words.subList(0, numWords/2);
            ArrayList<String> arr2 = words.subList(numWords/2);
            ArrayList<String> sortedArr1 = sortWords(arr1);
            ArrayList<String> sortedArr2 = sortWords(arr2);
            return merge(sortedArr1, sortedArr2);
        }
        //ArrayList<String> arr1 = sortWords(0, numWords/2);
        //ArrayList<String> arr2 = sortWords((numWords/2) + 1, numWords - 1);
        //return merge(arr1, arr2);
        
        /* If the words are out of order
                if (current.compareTo(next) > 0)
                {
                    // Swap the two words
                    String temp = current;
                    current = next;
                }

        return words;
        */
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2)
    {

    }
    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size() - 1; i++)
        {
            if (!found(words.get(i), 0, DICTIONARY_SIZE - 1))
            {
                words.remove(words.get(i));
                i--;
            }
        }
    }

    public boolean found(String s, int low, int high)
    {
        if (low > high)
            return false;

        int med = (high + low) / 2;

        // If the word is found
        if (DICTIONARY[med].equals(s))
            return true;

        // If the word is in the 1st half of the dictionary
        else if (s.compareTo(DICTIONARY[med]) < 0)
            high = --med;
         // If the word is in the 2nd half of the dictionary
        else
        {
            low = ++med;
        }
        return found(s, low, high);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
