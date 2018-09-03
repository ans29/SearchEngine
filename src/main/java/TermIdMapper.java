import java.util.HashMap;

public class TermIdMapper
{
    static Integer tId;
    public static HashMap <String, Integer> map;

    public TermIdMapper ()
    {
        tId = -1;
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
}