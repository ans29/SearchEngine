import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.HashMap;

public class Page implements Runnable
{
    public String title, id, text;
    public String infobox, categories, ext_links, refs;
    public static Integer page_count = -1;

    TermHash hash;
    public static HashMap <Integer, Integer> threadGrpStartHash; // = new HashMap<>();
    public static HashMap <Integer, Integer> threadGrpEndHash;   // = new HashMap<>();

    public Page() { }

    public void setId (String id)
    {   this.id = id;    }
    public void setTitle (String title)
    {   this.title = title;    }
    public void setText (String text)
    {   this.text = text;    }


    public String getId()
    {   return id;    }
    public String getTitle()
    {   return title;   }
    public String getText()
    {   return text;    }


    // PROBLEM : SAXPareser is too fast!
    //           5th thead is going inside before 0th thread creates new hash for it,
    // SOLUTION: ???
    public void run()  //every kth thread run should create new hash to add entry, and every (k+1)th should wait for all buddy threads to finish and then write hash into file then destroy hash instance.
    {
        int counter = page_count++;
        Integer pgId = Integer.valueOf(this.id);
        Integer k = Constants.pages_per_file;

        System.out.println("new thread");

        if (counter % k == 0)
        {
            System.out.println("first thread");
            hash = new TermHash();
            threadGrpStartHash.put(counter/k, 0);
            threadGrpEndHash.put(counter/k, 0);
        }
        TermHash groupHash = hash;

        updateValBefore (k, counter);
        MyParser.extractOthers (this, groupHash);
        boolean last = updateValAfter (k, counter);

        if (last == true)  // last (k)th thread will write hash and finishes
        {
            TreeMap <Integer, HashMap<Long, Pair<Integer, LinkedList<Integer>>>> sortedHash = new TreeMap<>();
            sortedHash.putAll(groupHash.termHash);
            try
            {
                FileHandler level0 = new FileHandler();
                level0.writeHash(sortedHash);
            }
            catch (IOException e)
            {     e.printStackTrace();      }
        }
    }

    private void updateValBefore (int k, int counter)
    {
        Integer val = threadGrpStartHash.get (counter/k);
        Integer grp = new Integer(counter/k);
        threadGrpStartHash.put(grp, val+1); // put(counter/k, val+1);
    }

    private boolean updateValAfter(Integer k, int counter)
    {
        int key = counter/k;
        Integer val = threadGrpEndHash.get (key);
        threadGrpEndHash.put(key, val+1);

        if (val == k)
        {
            threadGrpEndHash.remove (key);
            threadGrpStartHash.remove (key);
            return true;
        }

        return false;
    }
}