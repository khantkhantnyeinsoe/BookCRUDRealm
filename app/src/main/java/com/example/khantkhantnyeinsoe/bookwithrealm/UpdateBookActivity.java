package com.example.khantkhantnyeinsoe.bookwithrealm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khantkhantnyeinsoe.bookwithrealm.data.Book;
import com.example.khantkhantnyeinsoe.bookwithrealm.data.RBook;

import io.realm.Realm;
import io.realm.RealmResults;

public class UpdateBookActivity extends AppCompatActivity {
    final Realm realm = Realm.getDefaultInstance();
    EditText updateTitle;
    EditText updateAuthor;
    EditText updateImage;
    EditText description;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);


        String MY_PREFS_NAME = "MyPrefsFile";

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String title = prefs.getString("title", null);
        String author = prefs.getString("author", null);
        String image = prefs.getString("image", null);
        String desc = prefs.getString("description",null);
        final String bookid = prefs.getString("bookid",null);


        updateTitle = (EditText) findViewById(R.id.update_title);
        updateAuthor = (EditText) findViewById(R.id.update_author);
        updateImage = (EditText) findViewById(R.id.update_image);
        description = (EditText) findViewById(R.id.update_description);
        updateButton = (Button) findViewById(R.id.updateButton);

        updateTitle.setText(title);
        updateAuthor.setText(author);
        updateImage.setText(image);
        description.setText(desc);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UpdateBookActivity.this, "update", Toast.LENGTH_SHORT).show();

                Book insertBook = new Book();
                insertBook.setBook_id(String.valueOf(bookid));
                insertBook.setTitle(String.valueOf(updateTitle.getText()));
                insertBook.setDescription(String.valueOf(description.getText()));
                insertBook.setAuthor(String.valueOf(updateAuthor.getText()));
                insertBook.setImageUrl(String.valueOf(updateImage.getText()));


                RealmResults<RBook> results = realm.where(RBook.class)
                        .equalTo("book_id",String.valueOf(bookid))
                        .findAll();

                Log.d("result : ",results.toString());

                realm.beginTransaction();
                results.get(0).setTitle(insertBook.getTitle());
                results.get(0).setAuthor(insertBook.getAuthor());
                results.get(0).setDescription(insertBook.getDescription());
                results.get(0).setImageUrl(insertBook.getImageUrl());

                realm.commitTransaction();


                Intent intent = new Intent(UpdateBookActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
