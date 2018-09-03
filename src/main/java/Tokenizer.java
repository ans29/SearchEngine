import java.util.LinkedList;

/*
    0. make table
    1. lower
    2. punctuatiuons from table replace by ' '
    3. split on ' '
    ==> tokens as array of wrd
    4. check if not srpwrd or ascii or len > 2
    5. then do stemming
*/


public class Tokenizer
{
    public static LinkedList<String> tokenise(String text)
    {
        text = text.replaceAll ( "\\p{Punct}", " ");
        text = text.replaceAll ( "\\p{Space}", " ");

        String[] tok = text.split(" ");  //optimazation opportunity : replace regex
        LinkedList <String> op = new LinkedList<String>();

        for (int i = 0; i < tok.length; i++)
        {
            String s = new String (tok[i].toLowerCase());
            if (s.matches("\\A\\p{ASCII}*\\z") && s.length()>2 && ! Constants.stopWords.contains(s)) {
                op.add (Stemmer.getStem(s));
            }
        }

        return op;
    }
}