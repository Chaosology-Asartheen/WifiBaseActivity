package chaos.n.wifibaseactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.activity_main);
        final EditText password = (EditText) findViewById (R.id.editpassword);



        Button bt = (Button) findViewById (R.id.button2);

        bt.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (password.getText ().toString ().equals ("12345678")) {
                    Intent myIntent = new Intent ();
                    myIntent.setComponent (new ComponentName (MainActivity.this, packet.class));
                    startActivity (myIntent);
                    //finish ();
                }
            }
        });
    }
}
