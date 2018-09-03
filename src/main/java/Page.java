import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.HashMap;

public class Page
{
    public String title, id, text;
    public String infobox, categories, ext_links, refs;

    public Page() { }

    public void setId (String id)
    {   this.id = id;    }
    public void setTitle (String title)
    {   this.title = title;    }
    public void setText (String text)
    {   this.text = text;    }


    public String getId()
    {   return id;    }
    public String getTitle()
    {   return title;   }
    public String getText()
    {   return text;    }
}