import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class TermHash
{

    public ConcurrentHashMap<Integer, HashMap<Long, Pair<Integer, LinkedList<Integer>>>> termHash;

    TermHash() // new for each file to be created
    {
        termHash = new ConcurrentHashMap<>();
    }

    public void putHash (String line, long doc_id, int index) // redefine with try catch style.. that creates entries in exception
    {
        LinkedList<String> tokens = Tokenizer.tokenise(line);

        for (int i = 0; i < tokens.size(); i++)
        {
            Integer termId = TermIdMapper.putTermId(tokens.get(i));

            if (termHash.containsKey (termId))
            {
                if (termHash.get(termId).containsKey(doc_id))
                {
                    Integer freq = termHash.get(termId).get(doc_id).getFirst();
                    Integer tag_count = termHash.get(termId).get(doc_id).getSecond().get(index);

                    termHash.get(termId).get(doc_id).setFirst(freq+1);
                    termHash.get(termId).get(doc_id).getSecond().set(index, tag_count+1);


//                    if (termHash.get(token).get(doc_id).contains(index))  //XCEPt
//                    {
//                        Integer count = globH.get(tokens.get(i)).get(doc_id).get(index);
//                        globH.get(tokens.get(i)).get(doc_id).set(index, count+1);
//                    }
//                    else
//                    {
//                        LinkedList <Integer>tempList = new LinkedList<Integer>();
//                        for (int j = 0; j < 6; j++)
//                            tempList.add(0);
//                        tempList.add(index, 1);
//
//                        globH.get(token).put(doc_id, tempList);
//                    }
                }
                else
                {
                    LinkedList <Integer>tempList = new LinkedList<Integer>();
                    for (int j = 0; j < 6; j++)
                        tempList.add(0);
                    tempList.add(index, 1);

                    Pair <Integer, LinkedList<Integer> > freq_ll = new Pair<>(1, tempList);

                    termHash.get(termId).put(doc_id,freq_ll);
                }
            }
            else
            {
                LinkedList <Integer>tempList = new LinkedList<Integer>();
                for (int j = 0; j < 6; j++)
                    tempList.add(0);
                tempList.add(index, 1);

                Pair <Integer, LinkedList<Integer> > freq_ll = new Pair<>(1, tempList);

                HashMap <Long, Pair <Integer, LinkedList<Integer> > > tempHash = new HashMap<>();
                tempHash.put(doc_id,freq_ll);

                termHash.put(termId, tempHash);
            }
        }
    }
}