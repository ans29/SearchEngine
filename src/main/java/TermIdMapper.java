import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class TermIdMapper
{
    static Integer tId;
    public static HashMap <String, Integer> map;
    public static HashMap <Long, String> tid_Title;

    public TermIdMapper ()
    {
        map = new HashMap<>();
        tid_Title = new HashMap<>();
        tId = 0;
    }

    static Integer putTermId (String term)
    {
        if (map.containsKey(term) == false)
        {
            map.put(term, tId++);
            return tId;
        }
        return  map.get(term);
    }

    public static HashMap<String, Integer> readDictionary() throws IOException, ClassNotFoundException
    {
        FileInputStream fileIn = new FileInputStream (Constants.vocab_dir + "map.bin");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        return (HashMap<String, Integer>) in.readObject();
    }

    public static void writeMap(Object ob, String name) throws IOException
    {
        FileOutputStream fileOut =  new FileOutputStream(Constants.vocab_dir + name);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(ob);
        out.close();
        fileOut.close();
    }

    public static HashMap<Long, String> readIdTitleMap() throws IOException, ClassNotFoundException
    {
        FileInputStream fileIn = new FileInputStream (Constants.vocab_dir + "idTitleMaper.bin");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        return (HashMap<Long, String>) in.readObject();
    }

    public static void saveN() throws IOException
    {
        File f = new File(Constants.vocab_dir + "pagesCount.bin");
        FileWriter fw = new FileWriter(f);
        fw.write(Constants.No_of_pages.toString());
        fw.flush();
        fw.close();
    }

    public static void loadN() throws FileNotFoundException
    {
        File f = new File (Constants.vocab_dir + "pagesCount.bin");
        Scanner sc = new Scanner(f);
        Constants.No_of_pages = sc.nextLong();
        sc.close();
    }
}