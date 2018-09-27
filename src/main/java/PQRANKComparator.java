import java.util.Comparator;

public class PQRANKComparator implements Comparator<Pair <Double,String>>
{
    @Override
    public int compare(Pair <Double,String> o1, Pair <Double,String> o2)
    {
        return o1.getFirst().compareTo(o2.getFirst()) * -1;
    }
}
