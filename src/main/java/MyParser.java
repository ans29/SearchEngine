/* 1    Title
   2    Body Text
   3    Infobox						{{Infobox disease ---}}
   4    Categories					[[Category:Autism| ]]
   5    External Links (outlinks)	==External links==
   6    References					==References==                  */

public class MyParser
{
    public static void extractOthers (Page pg)
    {
        int tag_counter;
        boolean is_this_line_tag_itself = false;

        GlobalHash.putHash (pg.title, Long.parseLong(pg.id), 1);

        String[] text_lines = pg.text.split("\n");
        tag_counter = 2;
        boolean css_flag = false;

        for (int i = 0; i < text_lines.length; i++)
        {
            text_lines[i].replaceAll("\\{\\{cite.*}}"," ");
            text_lines[i].replaceAll("\\{\\{vcite.*}}"," ");
            text_lines[i].replaceAll("[[File.*]]"," ");

            if (css_flag == true )
            {
                tag_counter = 0;
                if (text_lines[i].startsWith ("|}"))
                    css_flag = false;
            }
            else if("==External Links==".equals(text_lines[i]) || "== External Links ==".equals(text_lines[i]) || "==External Links ==".equals(text_lines[i]) )
            {
                tag_counter = 5;
                is_this_line_tag_itself = true;
            }
            else if ("==References==".equals(text_lines[i]) || "== References ==".equals(text_lines[i]) || "== References==".equals(text_lines[i]) || "==References ==".equals(text_lines[i]))
            {
                tag_counter = 6;
                is_this_line_tag_itself = true;
            }
            else if ( text_lines[i].startsWith ("\\[\\[Category:"))
            {
                tag_counter = 0;
                GlobalHash.putHash (text_lines[i].substring (11) + " ", Long.parseLong(pg.id), 4);
            }
            else if (text_lines[i].startsWith ("{|"))
            {
                css_flag = true;
                tag_counter = 0;
            }
            else if ( text_lines[i].startsWith ("\\{\\{Infobox") )
            {
                tag_counter = 3;
                is_this_line_tag_itself = true;
            }
            else if (tag_counter == 3 && "}}".equals(text_lines[i]))
                tag_counter = 0;

            switch (tag_counter)
            {
                case 0: tag_counter = 2;
                        break;
                case 2: GlobalHash.putHash (text_lines[i], Long.parseLong(pg.id), 2);
                        break;
                case 3: if (is_this_line_tag_itself == true)
                            GlobalHash.putHash (text_lines[i].substring (10) + " ", Long.parseLong(pg.id), 3);
                        else
                            GlobalHash.putHash (text_lines[i], Long.parseLong(pg.id), 3);
                        break;
                case 4: tag_counter = 2;
                        break;
                case 5 :if (is_this_line_tag_itself == false)
                            GlobalHash.putHash (text_lines[i], Long.parseLong(pg.id), 5);
                        break;
                case 6: if (is_this_line_tag_itself == false)
                            GlobalHash.putHash (text_lines[i], Long.parseLong(pg.id), 6);
                        break;
            }

            is_this_line_tag_itself = false;
        }
    }
}