package test.furlenco.com;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadManager {


    public void startDownLoad(Context context, String sourceUrl, String destinationPath)
    {
        new DownLoadFileThread(context, sourceUrl, destinationPath).start();
    }

    private static class DownLoadFileThread extends Thread{

        Context context=null;
        String sourceUrl=null;
        String destinationPath=null;

        public DownLoadFileThread(Context context, String sourceUrl,String destinationPath)
        {
            this.context=context;
            this.sourceUrl=sourceUrl;
            this.destinationPath=destinationPath;
        }

        @Override
        public void run()
        {
            downLoadFileFromServer();
        }


        public boolean downLoadFileFromServer()
        {
            Log.v("Source Url", "sourceUrl: "+sourceUrl);
            Log.v("Destination Path", "destinationPath: "+destinationPath);

            InputStream urlInputStream=null;

            URLConnection urlConnection ;

            try
            {
                URL finalUrl =new URL(sourceUrl);

                urlConnection = finalUrl.openConnection();

                int contentLength=urlConnection.getContentLength();

                Log.d("Url","Streaming from "+sourceUrl+ "....");
                DataInputStream stream = new DataInputStream(finalUrl.openStream());

                Log.d("File","Buffering the received stream(size="+contentLength+") ...");
                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();
                Log.d("File Length","Buffered successfully(Buffer.length="+buffer.length+")....");

                if (buffer.length>0)
                {
                    try
                    {
                        Log.d("File Create","Creating the new file..");
                        FileOutputStream fos = context.openFileOutput(destinationPath, Context.MODE_PRIVATE);
                        Log.d("File Write","Writing from buffer to the new file..");
                        fos.write(buffer);
                        fos.flush();
                        fos.close();
                        Log.d("File Created","Created the new file & returning 'true'..");
                        return true;
                    }
                    catch (Exception e)
                    {
                        Log.e("Error Create File", "Could not create new file(Path="+destinationPath+") ! & returning 'false'.......");
                        e.printStackTrace();
                        return false;
                    }
                }
                else
                {

                    Log.e("Error:Buffer Size", "Buffer size is zero ! & returning 'false'.......");
                    return false;
                }
            }
            catch (Exception e)
            {
                Log.e("Error:Url", "Failed to open urlConnection/Stream the connection(From catch block) & returning 'false'..");
                System.out.println("Exception: " + e);
                e.printStackTrace();
                return false;
            }
            finally
            {
                try
                {
                    Log.d("Closing InputStream", "Closing urlInputStream... ");
                    if (urlInputStream != null) urlInputStream.close();

                }
                catch (Exception e)
                {
                    Log.e("Error:Close InputStream", "Failed to close urlInputStream(From finally block)..");
                }
            }
        }
    }

}
