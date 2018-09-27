import java.io.*;
import java.util.Scanner;

class Splitter
{
    static void split() throws IOException
    {

        String dir_from = Constants.MergedFile;
        String dir_to = Constants.SplitFiles;
        String offset_dir = Constants.offset_dir;
        long counter = 0L;

        File directory = new File(dir_from);
        File[] fList = directory.listFiles();
        File mrgd = null;
        if (fList != null)
            mrgd = new File(String.valueOf(fList[0]));

        FileWriter writer;
        BufferedWriter bw = null, bw_offset = null;

        Scanner sc = null;
        if (mrgd != null)
            sc = new Scanner(mrgd);


        Long offset_val = 0L;
        String file_to = null;
        for (Long i=0L; sc!=null && sc.hasNextLine(); i++)
        {
            if (i % Constants.lines_per_splitFile == 0)
            {
                file_to = dir_to + "/" + Long.toString(counter++);
                String offset_file = offset_dir + "/" + Long.toString(counter-1);
                writer = new FileWriter(file_to);
                bw = new BufferedWriter(writer);
                writer = new FileWriter(offset_file);
                bw_offset = new BufferedWriter (writer);
            }

            String line = sc.nextLine();
            if (bw != null)
            {
                File f_forOffset = new File (file_to);
                offset_val = f_forOffset.length();
                bw_offset.write (offset_val.toString()  + "\n");
                bw_offset.flush();

                bw.write(line + "\n" );
                bw.flush();
            }
        }
        if (bw != null)
            bw.close();
    }
}
