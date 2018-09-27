import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

class KWay
{
    private static Integer counter = 0, levelCount = 0;

    static void autoMerge() throws IOException
    {
        File directory = new File(Constants.parsedFiles);
        while (Objects.requireNonNull(directory.listFiles()).length > Constants.filesToMergeAtATime)
            merge(Constants.parsedFiles, Constants.parsedFiles);
        merge(Constants.parsedFiles, Constants.MergedFile);
    }

    private static void merge(String level0dir, String level1dir) throws IOException
    {
        File directory = new File(level0dir);
        File[] fList = directory.listFiles();
        AtomicReference<LinkedList<Scanner>> scList = new AtomicReference<>(new LinkedList<>());

        Integer k = Constants.filesToMergeAtATime;
        System.out.println("\t Now merging " + k.toString() + " files from " + (fList != null ? fList.length : 0) + " files in " + level0dir + " into " + level1dir + " at a time...");


        if (fList != null)
            for (File f : fList)
            {
                Scanner sc = new Scanner(f);
                scList.get().add(sc);
            }

        for (int i = 0; i < scList.get().size(); i += k)
        {
            PriorityQueue<Pair<Long, Pair<Scanner, String>>> llpair = new PriorityQueue<>(k, new PQComparator());

            for (int j = 0; j < k && i + j < scList.get().size(); j++)
                refill (scList.get().get(i+j), llpair);

            String writeTo = level1dir + levelCount + "_" + (counter++).toString();
            FileWriter writer = new FileWriter(writeTo);
            BufferedWriter bw = new BufferedWriter(writer);

            Pair<Long, Pair<Scanner, String>> oldTop = llpair.poll() , newTop = null;
            StringBuilder toWrite = null;
            boolean concatenated = false;
            if (oldTop != null) refill(oldTop.getSecond().getFirst(), llpair);


            while(llpair.size() != 0)
            {
                if (!concatenated && oldTop != null)
                    toWrite = new StringBuilder(oldTop.getSecond().getSecond());


                newTop = llpair.poll();
                if (newTop != null)
                {
                    refill(newTop.getSecond().getFirst(), llpair);
                }

                if (oldTop!=null && oldTop.getFirst().equals(newTop.getFirst()))
                {
                    String line = newTop.getSecond().getSecond();
                    int index = line.indexOf(':');
                    String toAppend = line.substring(index+1);
                    toWrite = toWrite.append(toAppend);
                    concatenated = true;
                    Constants.line_in_merged++;
                }
                else
                {
                    bw.write(toWrite + "\n");
                    bw.flush();
                    toWrite = new StringBuilder("");
                    concatenated = false;
                }

                oldTop = newTop;
            }
            if (newTop != null)
            {
                bw.write(newTop.getSecond().getSecond() + "\n");
                bw.flush();
            }

            for (int j = 0; j < k && i + j < scList.get().size(); j++)
                fList[i+j].delete();
        }
        counter = 0;
        levelCount++;
    }

    private static void refill(Scanner sc, PriorityQueue<Pair<Long, Pair<Scanner, String>>> llpair)
    {
        Pair<Long, Pair<Scanner, String>> readPair = getPair(sc);
        if (readPair != null)
            llpair.add(readPair);
    }

    private static Pair<Long, Pair<Scanner, String>> getPair(Scanner sc)
    {
        if (sc.hasNextLine())
        {
            String line = sc.nextLine();
            int index = line.indexOf(':');
            String tidStr = line.substring(0, index);
            Long tId = Long.parseLong(tidStr);
            Pair inner = new Pair(sc, line);
            return new Pair(tId, inner);
        }
        return null;
    }
}