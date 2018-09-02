import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.PriorityQueue;

public class SearchEngineMain
{

    public static boolean globHashProcessed = false;
    public static Long pgProcessing = 0L;

    public static void main (String args[]) throws Exception
    {
        String filename = "/home/ansh/IdeaProjects/wiki-search-small.xml", out_file = "InvertedIndex.txt";
        if (args.length > 1)
        {
            filename = args[0];
            out_file = args[1];
        }
        long start = System.nanoTime();


        FileWriter writer = new FileWriter(out_file);
        BufferedWriter bw=new BufferedWriter(writer);


        SAXwikiHandler saXwikiHandler = new SAXwikiHandler();
        saXwikiHandler.readDatafromXML(filename);

        for (int i=0; pgProcessing != 0; i++) // globHashProcessed == false ||
        {
            if (i%100==0)
                System.out.print(".");
        }

        PriorityQueue <String> pq = new PriorityQueue<String>();


        Long diff = (System.nanoTime() - start)/1000000000;
        System.out.print("\t Inverted index created in : " + diff.toString() + " sec.\n\t WRITING INTO FILE...");


        for (String s : GlobalHash.globH.keySet())
            pq.add(s);

        while (! pq.isEmpty())
        {
            String wrd = pq.remove();
            bw.write(wrd + ":");

            for (Long pg : GlobalHash.globH.get(wrd).keySet())
            {
                bw.write('d');
                bw.write(pg.toString());
                for (int i = 1; i <= 6 ; i++)
                {
                    Integer val = GlobalHash.globH.get(wrd).get(pg).get(i);
                    if (val!=0)
                    {
                        switch (i)
                        {
                            case 1:  bw.write('t');break;
                            case 2:  bw.write('b');break;
                            case 3:  bw.write('i');break;
                            case 4:  bw.write('c');break;
                            case 5:  bw.write('e');break;
                            case 6:  bw.write('r');break;
                        }
                        bw.write (val.toString());
                    }
                }
                bw.write('|');
            }
            bw.write('\n');
        }
        bw.close();
        System.out.println (".. " + out_file + " CREATED SUCCESSFULLY.");
    }
}