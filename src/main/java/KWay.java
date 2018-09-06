import java.io.*;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class KWay
{
    public static Integer counter = 0;
    public static FileWriter writer;
    public static BufferedWriter bw;

    public static void merge(String level0dir, String level1dir) throws IOException
    {
        File directory = new File(level0dir);
        File[] fList = directory.listFiles();
        LinkedList <Scanner> scList = new LinkedList<>();

        Integer k = Constants.filesToMergeAtATime;
        System.out.println("\t Now merging " + k.toString() + " files from " + fList.length + " files in " + level0dir + " into " + level1dir + " at a time...");


        for (File f: fList)
        {
            Scanner sc = new Scanner(f);
            scList.add(sc);
        }

        for (int i = 0; i < scList.size(); i += k)
        {
            PriorityQueue<Pair<Long, Pair<Scanner, String>>> llpair = new PriorityQueue<Pair<Long, Pair<Scanner, String>>>(k, new PQComparator());
            Pair<Long, Pair<Scanner, String>> readPair;

            for (int j = 0; j < k && i + j < scList.size(); j++)
                refill (scList.get(i+j), llpair);

            String writeTo = level1dir + (counter++).toString();
            writer = new FileWriter(writeTo);
            bw = new BufferedWriter(writer);

            Pair<Long, Pair<Scanner, String>> oldTop = llpair.poll() , newTop = null;
            String toWrite = null;
            boolean concatenated = false;
            refill (oldTop.getSecond().getFirst(), llpair);


            while (llpair.size() != 0)
            {
                if (concatenated == false)
                    toWrite = oldTop.getSecond().getSecond();

                newTop = llpair.poll();
                refill(newTop.getSecond().getFirst(), llpair);


                if (oldTop.getFirst() == newTop.getFirst())
                {
                    String line = newTop.getSecond().getSecond();
                    int index = line.indexOf(':');
                    String toAppend = line.substring(index+1);
                    toWrite = toWrite.concat(toAppend);
                    concatenated = true;
                }
                else
                {
                    bw.write(toWrite + "\n");
                    bw.flush();
                    toWrite = "";
                    concatenated = false;
                }

                oldTop = newTop;
            }
            bw.write(newTop.getSecond().getSecond() + "\n");
            bw.flush();

            for (int j = 0; j < k && i + j < scList.size(); j++)
                fList[i+j].delete();
        }
    }

    private static void refill(Scanner sc, PriorityQueue<Pair<Long, Pair<Scanner, String>>> llpair)
    {
        Pair<Long, Pair<Scanner, String>> readPair = getPair(sc);
        if (readPair != null)
            llpair.add(readPair);
    }

    public static Pair<Long, Pair<Scanner, String>> getPair(Scanner sc)
    {
        if (sc.hasNextLine())
        {
            String line = sc.nextLine();
            int index = line.indexOf(':');
            String tidStr = line.substring(0, index);
            Long tId = Long.parseLong(tidStr);

            Pair<Scanner, String> inner = new Pair(sc, line);
            Pair<Long, Pair<Scanner, String>> p = new Pair(tId, inner);
            return p;
        }
        return null;
    }
}