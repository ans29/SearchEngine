import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class GlobalHash
{

    public static HashMap <String, HashMap <Long, LinkedList<Integer>> >  globH;
    public static HashSet <String> stpWrds;
    public GlobalHash()
    {
        globH = new HashMap <String, HashMap <Long, LinkedList<Integer>> >();
        String stpList[] = {"a", "about", "above", "across", "after", "again", "against", "all", "almost", "alone", "along", "already", "also", "although", "always", "among", "an", "and", "another", "any", "anybody", "anyone", "anything", "anywhere", "are", "area", "areas", "around", "as", "ask", "asked", "asking", "asks", "at", "away", "b", "back", "backed", "backing", "backs", "be", "became", "because", "become", "becomes", "been", "before", "began", "behind", "being", "beings", "best", "better", "between", "big", "both", "but", "by", "c", "came", "can", "cannot", "case", "cases", "certain", "certainly", "clear", "clearly", "come", "could", "d", "did", "differ", "different", "differently", "do", "does", "done", "down", "down", "downed", "downing", "downs", "during", "e", "each", "early", "either", "end", "ended", "ending", "ends", "enough", "even", "evenly", "ever", "every", "everybody", "everyone", "everything", "everywhere", "f", "face", "faces", "fact", "facts", "far", "felt", "few", "find", "finds", "first", "for", "four", "from", "full", "fully", "further", "furthered", "furthering", "furthers", "g", "gave", "general", "generally", "get", "gets", "give", "given", "gives", "go", "going", "good", "goods", "got", "great", "greater", "greatest", "group", "grouped", "grouping", "groups", "h", "had", "has", "have", "having", "he", "her", "here", "herself", "high", "high", "high", "higher", "highest", "him", "himself", "his", "how", "however", "i", "if", "important", "in", "interest", "interested", "interesting", "interests", "into", "is", "it", "its", "itself", "j", "just", "k", "keep", "keeps", "kind", "knew", "know", "known", "knows", "l", "large", "largely", "last", "later", "latest", "least", "less", "let", "lets", "like", "likely", "long", "longer", "longest", "m", "made", "make", "making", "man", "many", "may", "me", "member", "members", "men", "might", "more", "most", "mostly", "mr", "mrs", "much", "must", "my", "myself", "n", "necessary", "need", "needed", "needing", "needs", "never", "new", "new", "newer", "newest", "next", "no", "nobody", "non", "noone", "not", "nothing", "now", "nowhere", "number", "numbers", "o", "of", "off", "often", "old", "older", "oldest", "on", "once", "one", "only", "open", "opened", "opening", "opens", "or", "order", "ordered", "ordering", "orders", "other", "others", "our", "out", "over", "p", "part", "parted", "parting", "parts", "per", "perhaps", "place", "places", "point", "pointed", "pointing", "points", "possible", "present", "presented", "presenting", "presents", "problem", "problems", "put", "puts", "q", "quite", "r", "rather", "really", "right", "right", "room", "rooms", "s", "said", "same", "saw", "say", "says", "second", "seconds", "see", "seem", "seemed", "seeming", "seems", "sees", "several", "shall", "she", "should", "show", "showed", "showing", "shows", "side", "sides", "since", "small", "smaller", "smallest", "so", "some", "somebody", "someone", "something", "somewhere", "state", "states", "still", "still", "such", "sure", "t", "take", "taken", "than", "that", "the", "their", "them", "then", "there", "therefore", "these", "they", "thing", "things", "think", "thinks", "this", "those", "though", "thought", "thoughts", "three", "through", "thus", "to", "today", "together", "too", "took", "toward", "turn", "turned", "turning", "turns", "two", "u", "under", "until", "up", "upon", "us", "use", "used", "uses", "v", "very", "w", "want", "wanted", "wanting", "wants", "was", "way", "ways", "we", "well", "wells", "went", "were", "what", "when", "where", "whether", "which", "while", "who", "whole", "whose", "why", "will", "with", "within", "without", "work", "worked", "working", "works", "would", "x", "y", "year", "years", "yet", "you", "young", "younger", "youngest", "your", "yours"};
        stpWrds = new HashSet<String>();
        stpWrds.addAll (Arrays.asList(stpList));
    }

/*
    public static void putHash (String line, long id, int index) // redefine with try catch style.. that creates entries in exception
    {
        LinkedList<String> tokens = tokenise(line);

        for (int i = 0; i < tokens.size(); i++)
        {
            String token = tokens.get(i);
            HashMap<Long, LinkedList<Integer>> wrdVal = new HashMap<Long, LinkedList<Integer>>();
            LinkedList<Integer> pgVal = new LinkedList<Integer>();
            Integer count;

            if (globH.containsKey(token))
                wrdVal = globH.get(token);
            else
                globH.put(token, wrdVal);


            if (wrdVal.containsKey(id))
                wrdVal.put(id, pgVal);
            else
                pgVal = wrdVal.get(id);

            try
            {   count = pgVal.get(index);   }
            catch (NullPointerException e1)
            {
                pgVal = new LinkedList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
                count = 0;
            }
            catch (IndexOutOfBoundsException e2)
            {
                pgVal = new LinkedList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
                count = 0;
            }




            pgVal.add(index, count+1);
            wrdVal.put(id, pgVal);
            globH.put(token, wrdVal);
        }
    }
*/


    public static void putHash (String line, long id, int index) // redefine with try catch style.. that creates entries in exception
    {
        LinkedList<String> tokens = tokenise(line);

        for (int i = 0; i < tokens.size(); i++)
        {
            if (globH.containsKey(tokens.get(i)))
            {
                if (globH.get(tokens.get(i)).containsKey(id))
                {
                    if (globH.get(tokens.get(i)).get(id).contains(index))  //XCEPt
                    {
                        Integer count = globH.get(tokens.get(i)).get(id).get(index);
                        globH.get(tokens.get(i)).get(id).set(index, count+1);
                    }
                    else
                    {
                        LinkedList <Integer>tempList = new LinkedList<Integer>();
                        for (int j = 0; j < 6; j++)
                            tempList.add(0);
                        tempList.add(index, 1);

                        globH.get(tokens.get(i)).put(id, tempList);
                    }
                }
                else
                {
                    LinkedList <Integer>tempList = new LinkedList<Integer>();
                    for (int j = 0; j < 6; j++)
                        tempList.add(0);
                    tempList.add(index, 1);

                    globH.get(tokens.get(i)).put(id,tempList);
                }
            }
            else
            {
                LinkedList <Integer>tempList = new LinkedList<Integer>();
                for (int j = 0; j < 6; j++)
                    tempList.add(0);
                tempList.add(index, 1);

                HashMap <Long, LinkedList<Integer>> tempHash = new HashMap <Long, LinkedList<Integer>> ();
                tempHash.put(id,tempList);

                globH.put(tokens.get(i), tempHash);
            }
        }
    }


    public static LinkedList<String> tokenise(String text)
    {
        text = text.replaceAll ( "\\p{Punct}", " ");
        text = text.replaceAll ( "\\p{Space}", " ");

        String[] tok = text.split(" ");  //optimazation opportunity : replace regex
        LinkedList <String> op = new LinkedList<String>();

        for (int i = 0; i < tok.length; i++)
        {
            String s = new String (tok[i].toLowerCase());
            if (s.matches("\\A\\p{ASCII}*\\z") && s.length()>2 && !stpWrds.contains(s))
                op.add (Stemmer.getStem(s));
        }

        return op;
    }

        /*
       v 0. make table
       v 1. lower
       v 2. punctuatiuons from table replace by ' '
       v 3. split on ' '
        ==> tokens as array of wrd
       v 4. check if not srpwrd or ascii or len > 2
       V 5. then do stemming
     */
}