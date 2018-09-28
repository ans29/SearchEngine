import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;


public class Search
{
    public static void main (String args[]) throws IOException, ClassNotFoundException
    {

        System.out.print("\n\t Loading please wait...");
        HashMap<String, Integer> map = TermIdMapper.readDictionary();
        HashMap<Long, String> id_titleMap = TermIdMapper.readIdTitleMap();
        TermIdMapper.loadN();

        new Constants();
        TermIdMapper.loadN();

        System.out.println("... Done");
        SearchHelper.printHelp();

        System.out.print("\n\n\t Enter search query : ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();


        while (!input.equals("#exit"))
        {
            if(input.equals("#help"))
            {
                SearchHelper.printHelp();
                input = sc.nextLine();
            }

            LinkedList <String> tokens = Tokenizer.tokenise(input);

            // check if query contains +/-/./: then save these details.. with positions..
            LinkedList <Character> qType = SearchHelper.parseInputForType (input);
            char queryType = 'n';
            if(qType != null)
            {
                queryType = qType.get(0);
                qType.remove(0);
                System.out.println("queryType : " + queryType + "\tq: " + qType);
            }


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
                        System.out.println("\t No documets found");
                    continue;
                }


                Integer fileno = tid / Constants.lines_per_splitFile;
                Integer lineno = tid % Constants.lines_per_splitFile;
                long offset = 0;

                File offset_file = new File(Constants.root + "offsets/" + fileno.toString());
                Scanner sc_line = new Scanner(offset_file);

                for (int j = 0; j < lineno && sc_line.hasNextLine(); j++)
                    offset = Long.parseLong(sc_line.nextLine());


                sc_line.close();

                RandomAccessFile raf = new RandomAccessFile(Constants.root + "split/" + fileno.toString(), "r");
                raf.seek(offset);
                String postingList = raf.readLine();
                postingList = postingList.substring (postingList.indexOf(":") + 1);

                String[] postings = postingList.split("\\|");

                tid_idf.put (tid ,Math.log10 ((double)Constants.No_of_pages / ((double) postings.length)));
                // idf of each term.. like idf of hello=2.33, world=9.7

                HashSet<Long> setOfDocId = new HashSet<>();

                for (int j = 0; j < postings.length; j++)
                {
                    // tId:dId<f>freq<X>val<X2>val<x3>val|<f>freq<X>val<X2>val<x3>val
                    //HANDLE FIELD QUERIES
                    if((queryType == 'f') )
                    {
                        if (!postings[j].contains (qType.get(0).toString()))
                            continue;
                    }



                    int f_pos = postings[j].indexOf('f');

                    int nextStopper_pos = SearchHelper.fetchPosForFreq (postings[j]);



                    Long dId_read = Long.parseLong (postings[j].substring (0, f_pos));
                    Long freq = Long.parseLong (postings[j].substring(f_pos+1, nextStopper_pos));

                    setOfDocId.add (dId_read);

                    HashMap<Integer, Long> tId_freq = null;
                    if (dId_tId_freq.containsKey(dId_read))
                        tId_freq = dId_tId_freq.get(dId_read);
                    else
                        tId_freq = new HashMap<>();

                    tId_freq.put(tid, freq);
                    dId_tId_freq.put(dId_read, tId_freq);
                }


                // HANDLE BOOLEAN QUERIES
                if (i==0)
                    finalSetOfDocId.addAll(setOfDocId);
                else
                {
                    if(queryType == 'n')
                        finalSetOfDocId.retainAll(setOfDocId);
                    else
                    {
                        if(qType.get(0) == '.')
                            finalSetOfDocId.retainAll(setOfDocId);
                        if(qType.get(0) == '+')
                            finalSetOfDocId.addAll(setOfDocId);
                        if(qType.get(0) == '-')
                            finalSetOfDocId.removeAll(setOfDocId);

                        qType.remove(0);
                    }
                }

                if(queryType == 'f' && qType != null && qType.size() != 0)
                    qType.remove(0);
            }

            // now we have all docId we need (INTERSECTION) : we'll now find tfidf and rank them
            // RANKING STAGE
            PriorityQueue < Pair <Double,String>> finalQueue = new PriorityQueue<>(new PQRANKComparator());

            for (Long did : finalSetOfDocId)
            {
                Double tfidf = 0.0;
                for (Integer tid : dId_tId_freq.get(did).keySet())
                    tfidf += dId_tId_freq.get(did).get(tid) * tid_idf.get(tid);

                String title = id_titleMap.get(did);
                finalQueue.add (new Pair(tfidf, title));
            }


            //PRINTING STAGE
            int page_count = 1, total_pgs = finalQueue.size()/Constants.outputentry_per_page;
            do
            {
                System.out.println("------- Page " + page_count++ + " of " + total_pgs + " ----------");
                for (int i=0; i<Constants.outputentry_per_page && finalQueue.size()>0; i++)
                {
                    Pair<Double, String> x = finalQueue.poll();
                    System.out.println (i+1 + ".\t" + x.getSecond());
                }

                if (finalQueue.size() > 0)
                    input = sc.nextLine();


            }while (finalQueue.size()>0 && input.equals("#next"));

            if (!input.equals("#exit"))
            {
                System.out.print("\n\t Enter search query : ");
                input = sc.nextLine();
            }
        }
    }
}
