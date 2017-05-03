package bbrains.su.firebaseandroidchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //пользовательский интерфейс
    private ListView messageListView;
    private EditText messageEitText;
    private Button buttonSend;

    //адптер
    private MessageAdapter messageAdapter;


    // Firebase - методы
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //связывание с UI
        messageListView = (ListView) findViewById(R.id.list_view_message);
        messageEitText = (EditText) findViewById(R.id.edit_text_message);
        buttonSend = (Button) findViewById(R.id.button_send);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("messages");

        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageAdapter.add(message);
                    messageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageAdapter.remove(message);
                    messageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        //устанавливаем слушатель базы данных
        databaseReference.addChildEventListener(childEventListener);

        //создаем лист где будет хранить сообщения
        List<Message> messages = new ArrayList<>();
        //адаптер для отображения листвью
        messageAdapter = new MessageAdapter(this, R.layout.item_message, messages);
        //устанавливаем адаптер
        messageListView.setAdapter(messageAdapter);

        //слушатель на кнопку
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(messageEitText.getText().toString());
                databaseReference.push().setValue(message);
                messageEitText.setText("");
            }
        });

        //устанвливаем слушатель текста на кнопку
        messageEitText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().length()>0){
                    buttonSend.setEnabled(true);
                }
                else {
                    buttonSend.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //удалять слушатель
    @Override
    protected void onDestroy() {
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }

        super.onDestroy();
    }
}
