import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class FileHandler
{
    static String prefix = "out/level0/";
    static Integer lvl0 = 0;
    BufferedWriter bw;

    public void writeHash(TreeMap<Integer, HashMap<Long,Pair<Integer, LinkedList<Integer>>>> sortedHash) throws IOException
    {
        String out_file = prefix + (lvl0++).toString();
        FileWriter writer = new FileWriter(out_file);
        BufferedWriter bw = new BufferedWriter(writer);


        for (Integer tId : sortedHash.keySet())
        {
            bw.write(tId.toString() + ":");                                 // tId:

            for (Long dId : sortedHash.get(tId).keySet())
            {
                bw.write(dId.toString() + "f");                             // tId:dId<f>
                bw.write(sortedHash.get(tId).get(dId).getFirst().toString());   // tID:dId<f>freq
                for (int i = 1; i <= 6 ; i++)
                {
                    Integer val = sortedHash.get(tId).get(dId).getSecond().get(i);
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
                        }                                                       // tId:dId<f>freq<X>
                        bw.write (val.toString());                              // tId:dId<f>freq<X>val   -> tId:dId<f>freq<X>val<X2>val<x3>val
                    }
                }
                bw.write('|');                                               // tId:dId<f>freq<X>val<X2>val<x3>val|<f>freq<X>val<X2>val<x3>val
            }
            bw.write('\n');
        }
        bw.close();
    }
}