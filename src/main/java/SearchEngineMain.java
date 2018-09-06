public class SearchEngineMain
{

    public static boolean globHashProcessed = false;
    public static Long pgProcessing = 0L;

    public static void main (String args[]) throws Exception
    {
        String filename = "/home/ansh/IdeaProjects/wiki-search-small.xml", out_file = "InvertedIndex.txt";
        if (args.length > 1)
            filename = args[0];

        long start = System.nanoTime();

        new Constants();
/*
        SAXwikiHandler saXwikiHandler = new SAXwikiHandler();           // STEP 1
        saXwikiHandler.readDatafromXML(filename);

        Long diff = (System.nanoTime() - start)/1000000000;
        System.out.println("\t Inverted index Level0 files created in : " + diff.toString() + " sec.");
*/
        KWay.autoMerge();
    }
}