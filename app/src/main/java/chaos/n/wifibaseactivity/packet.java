package chaos.n.wifibaseactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class packet extends AppCompatActivity {

    Button btnStart, btnSend;
    TextView textStatus;
    NetworkTask networktask;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_packet);
        btnStart = (Button) findViewById (R.id.btnStart);
        btnSend = (Button) findViewById (R.id.btnSend);
        textStatus = (TextView) findViewById (R.id.textStatus);
        btnStart.setOnClickListener (btnStartListener);
        btnSend.setOnClickListener (btnSendListener);
        networktask = new NetworkTask (); //Create initial instance so SendDataToNetwork doesn't throw an error.
    }

    private View.OnClickListener btnStartListener = new View.OnClickListener () {
        public void onClick (View v) {
            btnStart.setVisibility (View.INVISIBLE);
            networktask = new NetworkTask (); //New instance of NetworkTask
            networktask.execute ();
        }
    };

    private View.OnClickListener btnSendListener = new View.OnClickListener () {
        public void onClick (View v) {
            textStatus.setText ("Capturing DATA.");
            networktask.SendDataToNetwork ("GET / HTTP/1.1\r\n\r\n");

        }
    };

    public class NetworkTask extends AsyncTask<Void, byte[], Boolean> {
        Socket nsocket; //Network Socket
        InputStream nis; //Network Input Stream
        OutputStream nos; //Network Output Stream

        @Override
        protected void onPreExecute () {
            Log.i ("AsyncTask", "onPreExecute");
        }


        @Override
        protected Boolean doInBackground (Void... params) {
            boolean result = false;
            try {
                Log.i ("AsyncTask", "doInBackground: Creating socket");
                SocketAddress sockaddr = new InetSocketAddress ("192.168.1.1", 5000 );  //192.168.1.1 216.239.32.21
                Log.i (TAG, "doInBackground: CONNECTION SUCCESS");
                nsocket = new Socket ();
                nsocket.connect (sockaddr, 5000);
                if (nsocket.isConnected ()) {
                    nis = nsocket.getInputStream ();
                    nos = nsocket.getOutputStream ();
                    Log.i (TAG, "doInBackground: getting packet");
                    byte[] buffer = new byte[1024];
                    Log.i ("AsyncTask", "doInBackground: Waiting for  data...");
                    // Log.d (TAG, new S tring (buffer, "UTF-8"));
                    int read = nis.read (buffer, 0, 1024);

                    while (read != -1) {
                        byte[] tempdata = new byte[read];
                        System.arraycopy (buffer, 0, tempdata, 0, read);
                        Log.i("doInBackgroundloop", Arrays.toString(buffer));

                        //    Log.i (TAG, "doInBackground: ", tempdata  );
                        Log.d (TAG, new String (buffer, "UTF-8"));
                        //Log.i (TAG, new String (buffer, "UTF-8"));
                        publishProgress (tempdata);
                        read = nis.read (buffer, 0, 1024);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace ();
                Log.i ("AsyncTask", "doInBackground: IOException");
                result = true;
            } catch (Exception e) {
                e.printStackTrace ();
                Log.i ("AsyncTask", "doInBackground: Exception");
                result = true;
            } finally {
                try {
                    nis.close ();
                    nos.close ();
                    nsocket.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                Log.i ("AsyncTask", "doInBackground: Finished");
            }
            return result;
        }


        public void SendDataToNetwork (String cmd) { //You run this from th             e main thread.
            try {
                if (nsocket.isConnected ()) {
                    Log.i ("AsyncTask", "SendDataToNetwork: Writing received message to socket");
                    nos.write (cmd.getBytes ());
                } else {
                    Log.i ("AsyncTask", "SendDataToNetwork: Cannot send message. Socket is closed");
                }
            } catch (Exception e) {
                Log.i ("AsyncTask", "SendDataToNetwork: Message send failed. Caught an exception");
            }
        }



        @Override
        protected void onProgressUpdate (byte[]... values) {
            if (values.length > 0) {
                Log.i ("AsyncTask", "onProgressUpdate: " + values[0].length + " bytes received.");
                //Log.d (TAG, "onProgressUpdate() called with: values = [" + values + "]");
                textStatus.setText (new String (values[0]));
            }
        }

        @Override
        protected void onCancelled () {
            Log.i ("AsyncTask", "Cancelled.");
            btnStart.setVisibility (View.VISIBLE);
        }

        @Override
        protected void onPostExecute (Boolean result) {
            if (result) {
                Log.i ("AsyncTask", "onPostExecute: Completed with an Error.");
                textStatus.setText ("There was a connection error.");
            } else {
                Log.i ("AsyncTask", "onPostExecute: Completed.");
            }
            btnStart.setVisibility (View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy () {
        super.onDestroy ();
        networktask.cancel (true); //In case the task is currently running
    }
}
