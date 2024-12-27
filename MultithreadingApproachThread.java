package temp;

import java.util.*;
import java.util.concurrent.Callable;

public class MultithreadingApproachThread implements Callable<TreeMap<String, Integer>> {
    private String[] originalWords;

    public MultithreadingApproachThread(String[] originalWords) {
        this.originalWords = originalWords;

    }

    @Override
    public TreeMap<String, Integer> call() {
//        long startTime = System.nanoTime();
        String[][] chunks = splitWordsToChucks(originalWords, 20);
        TreeMap<String, Integer> localCounts = new TreeMap<>();
        for (String[] chunk : chunks) {
            List<WordCount> chunkFrequencyList = getFrequencyList(chunk);
            for (WordCount wc : chunkFrequencyList) {
                localCounts.putIfAbsent(wc.word, 0);
                localCounts.put(wc.word, localCounts.get(wc.word) + wc.count);
            }
        }
//        long endTime = System.nanoTime() - startTime;
//        System.out.println("Total time in thread: " + endTime);
        return localCounts;
    }

    private List<WordCount> getFrequencyList(String[] words) {
        List<WordCount> frequencyList = new ArrayList<>();
        for (String word : words) {
            boolean found = false;
            for (WordCount wc : frequencyList) {
                if (wc.word.equals(word)) {
                    wc.increment();
                    found = true;
                    break;
                }
            }
            if (!found) {
                frequencyList.add(new WordCount(word, 1));
            }
        }
        return frequencyList;
    }

    private String[][] splitWordsToChucks(String[] words, int numOfChunks) {
        String[][] chunks = new String[numOfChunks][];
        int chunkSize = words.length / numOfChunks;
        for (int i = 0; i < numOfChunks; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numOfChunks - 1) ? words.length : (i + 1) * chunkSize;
            chunks[i] = Arrays.copyOfRange(words, startIndex, endIndex);
        }
        return chunks;
    }

}

