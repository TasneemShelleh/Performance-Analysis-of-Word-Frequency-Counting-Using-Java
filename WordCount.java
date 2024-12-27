package temp;

public class WordCount implements Comparable<WordCount> {
    String word;
    int count;

    WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    void increment() {
        this.count++;
    }

    @Override
    public int compareTo(WordCount other) {
        return this.word.compareTo(other.word);
    }
}
