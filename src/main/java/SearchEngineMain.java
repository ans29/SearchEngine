public class SearchEngineMain
{
    public static void main (String args[]) throws Exception
    {
        String filename = "/home/ansh/IdeaProjects/wiki-search-small.xml";
        if (args.length > 1)
            filename = args[0];

        long start = System.nanoTime();

        new Constants();

        SAXwikiHandler saXwikiHandler = new SAXwikiHandler();           // STEP 1
        saXwikiHandler.readDatafromXML(filename);

        Long diff = (System.nanoTime() - start)/1000000000;
        System.out.println("\t Inverted index Level0 files created in : " + diff.toString() + " sec.");

        KWay.autoMerge();
        FileChopper.split();


        TermIdMapper.writeMap(TermIdMapper.map, "map.bin");
        TermIdMapper.writeMap(TermIdMapper.tid_Title, "idTitleMaper.bin");
        TermIdMapper.saveN();
    }
}