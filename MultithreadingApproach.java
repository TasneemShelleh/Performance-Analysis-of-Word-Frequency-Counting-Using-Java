package temp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


public class MultithreadingApproach {


    public static void main(String[] args) throws Exception {
        long totalStartTime = System.nanoTime();
        String fileContent = new String(Files.readAllBytes(Paths.get("text.txt")));
        String[] words = fileContent.split(" ");
        System.out.println("Total words: " + words.length);
        long start = System.currentTimeMillis();

        int numberOfThreads = 1;
        List<Future<TreeMap<String, Integer>>> futures = new ArrayList<>();
        String[][] arrayParts = splitArray(words, numberOfThreads);
        ExecutorService es = Executors.newFixedThreadPool(numberOfThreads);
        for(int i = 0;i < numberOfThreads;i++) {
            futures.add(es.submit(new MultithreadingApproachThread(arrayParts[i])));
        }

        List<WordFrequencyPair> threadsCombinedResults = new ArrayList<>();
        for (Future<TreeMap<String, Integer>> future : futures) {
            TreeMap<String, Integer> threadCounts = future.get();
            for (Map.Entry<String, Integer> entry : threadCounts.entrySet()) {
                threadsCombinedResults.add(new WordFrequencyPair(entry.getKey(), entry.getValue()));
            }
        }

        System.out.println("finished threads, in: " + (System.currentTimeMillis() - start) / 1000);

        threadsCombinedResults.sort(Comparator.comparing(o -> o.word));

        List<WordFrequencyPair> finalCombinedResults = new ArrayList<>();
        int index = 0;
        finalCombinedResults.add(threadsCombinedResults.get(0));
        for (int i = 1; i < threadsCombinedResults.size(); i++) {
            if (threadsCombinedResults.get(i).word.equals(finalCombinedResults.get(index).word)) {
                finalCombinedResults.get(index).frequency += threadsCombinedResults.get(i).frequency;
            } else {
                finalCombinedResults.add(threadsCombinedResults.get(i));
                index++;
            }
        }

        finalCombinedResults.sort((a, b) -> {
            return b.frequency - a.frequency;
        });

        List<WordFrequencyPair> mostFrequent = new ArrayList<>();
        for (int i = 0; i < Math.min(10, finalCombinedResults.size()); i++) {
            mostFrequent.add(finalCombinedResults.get(i));
        }
        for (WordFrequencyPair pair : mostFrequent) {
            System.out.println(pair.word + " " + pair.frequency);
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("Total execution time: " + (time / 1000));

        long totalEndTime = System.nanoTime() - totalStartTime;
        System.out.println("Full total execution time in nano: " + totalEndTime);
        es.shutdown();
    }

    public static String[][] splitArray(String[] array, int n) {
        int totalLength = array.length;
        int partSize = (int) Math.ceil((double) totalLength / n);

        String[][] parts = new String[n][];

        for (int i = 0; i < n; i++) {
            int start = i * partSize;
            int end = Math.min(start + partSize, totalLength);

            // Create sub-array for each part
            parts[i] = Arrays.copyOfRange(array, start, end);
        }

        return parts;
    }
}
