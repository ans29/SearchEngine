import java.io.File;
import java.util.Comparator;
import java.util.Scanner;

public class PQComparator implements Comparator<Pair<Long, Pair <Scanner, String>>>
{
    @Override
    public int compare(Pair<Long, Pair<Scanner, String>> o1, Pair<Long, Pair<Scanner, String>> o2)
    {
        return o1.getFirst().compareTo(o2.getFirst());
    }
}