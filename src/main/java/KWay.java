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
        Integer k = Constants.filesToMergeAtATime;
        System.out.println("\t Now merging " + k.toString() + " files from " + level0dir + " into " + level1dir + " at a time...");

        File directory = new File(level0dir);
        File[] fList = directory.listFiles();
        LinkedList <Scanner> scList = new LinkedList<>();

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
            {
                readPair = getPair(scList.get(i+j));
                if (readPair != null)
                    llpair.add(readPair);
            }

            String writeTo = level1dir + (counter++).toString();
            writer = new FileWriter(writeTo);
            bw = new BufferedWriter(writer);

            do
            {
                Pair<Long, Pair<Scanner, String>> topEle = llpair.poll();

                Scanner writeFrom = topEle.getSecond().getFirst();
                String toWrite = topEle.getSecond().getSecond();

                readPair = getPair(writeFrom);
                if (readPair != null)
                    llpair.add(readPair);

                while (llpair.peek().getFirst() == topEle.getFirst())
                {

                }


                bw.write(toWrite + "\n");

            } while (llpair.size() != 0);
        }
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