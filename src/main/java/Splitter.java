import java.io.*;
import java.util.Scanner;

class Splitter
{
    static void split() throws IOException
    {

        String dir_from = Constants.MergedFile;
        String dir_to = Constants.SplitFiles;
        long counter = 0L;

        File directory = new File(dir_from);
        File[] fList = directory.listFiles();
        File mrgd = null;
        if (fList != null)
            mrgd = new File(String.valueOf(fList[0]));

        FileWriter writer;
        BufferedWriter bw = null;
        Scanner sc = null;
        if (mrgd != null)
            sc = new Scanner(mrgd);

        for (Long i=0L; sc!=null && sc.hasNextLine(); i++)
        {
            if (i % Constants.lines_per_splitFile == 0)
            {
                String file_to = dir_to + "/" + Long.toString(counter++);
                writer = new FileWriter(file_to);
                bw = new BufferedWriter(writer);
            }

            String line = sc.nextLine();
            if (bw != null)
            {
                bw.write(line + "\n" );
                bw.flush();
            }
        }
        if (bw != null)
            bw.close();
    }
}
