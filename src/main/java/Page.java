public class Page implements Runnable
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


    public void run()
    {
        System.out.println(this.id + "<");
        MyParser.extractOthers (this);
    }
}