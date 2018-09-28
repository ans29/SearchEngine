import java.util.LinkedList;

public class SearchHelper
{


    public static void printHelp()
    {
        System.out.println (" \n\t\t  KEYWORDS: \t TASK: \n\t\t\t #help \t\t help page\n\t\t\t #next \t\t next page\n\t\t\t#exit \t\t exit ");
        System.out.println("\t\t \"ABC XYZ\" \t\t gives title of all docs containing both terms");
        System.out.println("\t\t\t\t + \t\t UNION \n\t\t\t\t . \t\t INTERSECTION\n\t\t\t\t - \t\t NEGATION");
        System.out.println("\t\t\t \"-ABC\" \t will be considered as \"ABC\"");
    }

    static int fetchPosForFreq(String posting)
    {
        int i = 0;

        // t,b,i,c,e,r
        i = posting.indexOf('t');
        if(i != -1)
            return i;

        i = posting.indexOf('b');
        if(i != -1)
            return i;

        i = posting.indexOf('i');
        if(i != -1)
            return i;

        i = posting.indexOf('c');
        if(i != -1)
            return i;

        i = posting.indexOf('e');
        if(i != -1)
            return i;

        i = posting.indexOf('r');
        if(i != -1)
            return i;

        return -1;
    }

    public static LinkedList<Character> parseInputForType(String input)
    {
        if (input.matches("^.+[.+\\-:].+$")) //\\.* \\[+ \\. - :] \\.+$"))  //Regex : ^.* [+ . - :] .+$
        {
            LinkedList <Character> seq = new LinkedList<>();
            if(input.contains(":"))
                seq.add('f');
            else
                seq.add('b');


            input = input.replaceAll("[\\+]", "@+");
            input = input.replaceAll("[.]", "@.");
            input = input.replaceAll("[-]", "@-");

            input = input.replaceAll("t:", "@t");
            input = input.replaceAll("b:", "@b");
            input = input.replaceAll("i:", "@i");
            input = input.replaceAll("c:", "@c");
            input = input.replaceAll("e:", "@e");
            input = input.replaceAll("r:", "@r");


            String[] tokens = input.split("@");

            for (int i = 1; i < tokens.length; i++)
                seq.add (tokens[i].charAt(0));
            return seq;
        }
        return null;
    }
}


// tf = log 10 (1+tf(t,d))
//idf = log 10 (N / df(t))

//score (q,d) = SUM (tf * idf) OVER ALL **TERMS**!
//            = hello(tf*idf) + world(tf*idf)
