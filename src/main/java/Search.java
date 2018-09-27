import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;


public class Search
{
    public static void main (String args[]) throws IOException, ClassNotFoundException
    {
        HashMap<String, Integer> map = TermIdMapper.readDictionary();
        HashMap<Long, String> id_titleMap = TermIdMapper.readIdTitleMap();
        TermIdMapper.loadN();

        new Constants();
        TermIdMapper.loadN();

        System.out.println("\n\t Enter search query : (\"#exit\" to exit )");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();


        while (!input.equals("#exit"))  //change to <EXIT>
        {
            LinkedList <String> tokens = Tokenizer.tokenise(input);
//            LinkedList <Long> tf_list = new LinkedList<>();
            HashMap <Integer, Double> tid_idf = new HashMap<>();
            HashMap <Long, HashMap<Integer, Long>> dId_tId_freq = new HashMap<>();
            HashSet <Long> finalSetOfDocId = new HashSet<>();

            for (int i = 0; i < tokens.size(); i++)
            {
                String token = tokens.get(i);
                Integer tid;

                if (map.containsKey(token))
                    tid = map.get(token);
                else
                {
                    if (tokens.size() == 1)
                        System.out.println("\t Error : term not found");
                    continue;
                }


                System.out.print(" token : " + token + "\t tid : " + tid);

                Integer fileno = tid / Constants.lines_per_splitFile;
                Integer lineno = tid % Constants.lines_per_splitFile;
                long offset = 0;

                File offset_file = new File(Constants.root + "offsets/" + fileno.toString());
                Scanner sc_line = new Scanner(offset_file);

                System.out.print("\tfileno : " + fileno + "\tlineno : " + lineno);
                for (int j = 0; j < lineno && sc_line.hasNextLine(); j++)
                    offset = Long.parseLong(sc_line.nextLine());


                sc_line.close();

                System.out.print("\t offset = " + offset);

                RandomAccessFile raf = new RandomAccessFile(Constants.root + "split/" + fileno.toString(), "r");
                raf.seek(offset);
                String postingList = raf.readLine();
                System.out.println(postingList);
                postingList = postingList.substring (postingList.indexOf(":") + 1);

                String[] postings = postingList.split("\\|");
//                ArrayList<String> title_list = new ArrayList<>();

                tid_idf.put (tid ,Math.log10 ((double)Constants.No_of_pages / ((double) postings.length)));
                // idf of each term.. like idf of hello=2.33, world=9.7

                System.out.println("\tidf = " + tid_idf.get(tid));


                HashSet<Long> setOfDocId = new HashSet<>();

                for (int j = 0; j < postings.length; j++)
                {
                    int f_pos = postings[j].indexOf('f');
                    int nextStopper_pos = fetchPosForFreq (postings[j]);
                    Long dId_read = Long.parseLong (postings[j].substring (0, f_pos));
                    Long freq = Long.parseLong (postings[j].substring(f_pos+1, nextStopper_pos));

                    setOfDocId.add (dId_read);

                    HashMap<Integer, Long> tId_freq = null;
                    if (dId_tId_freq.containsKey(dId_read))
                        tId_freq = dId_tId_freq.get(dId_read);
                    else
                        tId_freq = new HashMap<>();

                    tId_freq.put(tid, freq);
                    dId_tId_freq.put(dId_read, tId_freq); // YAHAAAAA

                }

                if (i==0)
                    finalSetOfDocId.addAll(setOfDocId);
                else
                    finalSetOfDocId.retainAll(setOfDocId);

                System.out.println("set : " + finalSetOfDocId);
            }

            // now we have all docId we need (INTERSECTION) : we'll now find tfidf and rank them
            PriorityQueue < Pair <Double,String>> finalQueue = new PriorityQueue<>(new PQRANKComparator());

            for (Long did : finalSetOfDocId)
            {
                Double tfidf = 0.0;
                System.out.print( "dId : "  + did);
                for (Integer tid : dId_tId_freq.get(did).keySet())
                {
                    tfidf += dId_tId_freq.get(did).get(tid) * tid_idf.get(tid);
                    System.out.print("\t"  + tid + "=" + tfidf);
                }
                String title = id_titleMap.get(did);
                finalQueue.add (new Pair(tfidf, title));
            }

            for (int i=0; i<5 && finalQueue.size()>0; i++)
            {
                Pair<Double, String> x = finalQueue.poll();
                System.out.println (x.getFirst()  + " " + x.getSecond());
            }


            input = sc.nextLine();
        }
    }

    static int fetchPosForFreq(String posting)
    {
        int i = 0;

        // t,b,i,c,e,r
        i = posting.indexOf('t');
        if(i != -1)
           return i;

        i = posting.indexOf('b');
        if(i != -1)
            return i;

        i = posting.indexOf('i');
        if(i != -1)
            return i;

        i = posting.indexOf('c');
        if(i != -1)
            return i;

        i = posting.indexOf('e');
        if(i != -1)
        return i;

        i = posting.indexOf('r');
        if(i != -1)
        return i;

        return -1;
    }
}

// tf = log 10 (1+tf(t,d))
//idf = log 10 (N / df(t))

//score (q,d) = SUM (tf * idf) OVER ALL **TERMS**!
//            = hello(tf*idf) + world(tf*idf)

