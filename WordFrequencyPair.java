package temp;


public class WordFrequencyPair implements Comparable<WordFrequencyPair> {
    public String word;
    public int frequency;

    public WordFrequencyPair(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(WordFrequencyPair other) {
        int countCompare = Integer.compare(other.frequency, this.frequency);
        return countCompare != 0 ? countCompare : this.word.compareTo(other.word);
    }

    @Override
    public String toString() {
        return "WordFrequencyPair{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}