package com.example.khantkhantnyeinsoe.bookwithrealm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khantkhantnyeinsoe.bookwithrealm.adapter.BookRecyclerAdapter;
import com.example.khantkhantnyeinsoe.bookwithrealm.data.Book;
import com.example.khantkhantnyeinsoe.bookwithrealm.data.RBook;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity{
    final Realm realm = Realm.getDefaultInstance();
    List<Book> bookList = new ArrayList<>();
    BookRecyclerAdapter bookRecyclerAdapter;
    RecyclerView bookRecycler;

    TextView createTitle;
    TextView createDescription;
    TextView createAuthor;
    TextView createImage;
    List<RBook> realmBookList;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        bookRecycler = (RecyclerView) findViewById(R.id.book_recycler);
        bookRecycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        bookRecycler.setLayoutManager(linearLayoutManager);

        String MY_PREFS_NAME = "MyPrefsFile";

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String statusText = prefs.getString("status", null);
        if (statusText == null) {
            Toast.makeText(this, "null............", Toast.LENGTH_SHORT).show();

            Book book = new Book();
            book.setBook_id("one");
            book.setAuthor("Reto Meier");
            book.setTitle("Android 4 Application Development");
            book.setImageUrl("https://api.androidhive.info/images/realm/1.png");
            bookList.add(book);

            book = new Book();
            book.setBook_id("two");
            book.setAuthor("Itzik Ben-Gan");
            book.setTitle("Microsoft SQL Server 2012 T-SQL Fundamentals");
            book.setImageUrl("https://api.androidhive.info/images/realm/2.png");
            bookList.add(book);

            book = new Book();
            book.setBook_id("three");
            book.setAuthor("Magnus Lie Hetland");
            book.setTitle("Beginning Python: From Novice To Professional Paperback");
            book.setImageUrl("https://api.androidhive.info/images/realm/3.png");
            bookList.add(book);

            book = new Book();
            book.setBook_id("four");
            book.setAuthor("Chad Fowler");
            book.setTitle("The Passionate Programmer: Creating a Remarkable Career in Software Development");
            book.setImageUrl("https://api.androidhive.info/images/realm/4.png");
            bookList.add(book);

            book = new Book();
            book.setBook_id("five");
            book.setAuthor("Yashavant Kanetkar");
            book.setTitle("Written Test Questions In C Programming");
            book.setImageUrl("https://api.androidhive.info/images/realm/5.png");
            bookList.add(book);





            Log.d("book list size::", String.valueOf(bookList.size()));

            //to insert realm book obj

            realm.beginTransaction();


            for(Book boo : bookList){
                RBook reBook;
                reBook = realm.createObject(RBook.class);
                reBook.setTitle(boo.getTitle());
                reBook.setDescription(boo.getDescription());
                reBook.setAuthor(boo.getAuthor());
                reBook.setImageUrl(boo.getImageUrl());
                reBook.setBook_id(boo.getBook_id());
                realm.copyToRealm(reBook);
            }

            realm.commitTransaction();

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("status", "exist");
            editor.apply();
        }
        else {
            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
        }
        readRealmData();


    }
    public final void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_createbook, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        createTitle = (TextView) promptView.findViewById(R.id.create_title);
        createDescription = (TextView) promptView.findViewById(R.id.create_description);
        createAuthor = (TextView) promptView.findViewById(R.id.create_author);
        createImage = (TextView) promptView.findViewById(R.id.create_image);


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("title : ", String.valueOf(createTitle.getText()));
                        Log.d("description : ", String.valueOf(createDescription.getText()));
                        Log.d("author : ", String.valueOf(createAuthor.getText()));
                        Log.d("image : ", String.valueOf(createImage.getText()));

                        Book insertBook = new Book();
                        insertBook.setBook_id(String.valueOf(System.currentTimeMillis()));
                        insertBook.setTitle(String.valueOf(createTitle.getText()));
                        insertBook.setDescription(String.valueOf(createDescription.getText()));
                        insertBook.setAuthor(String.valueOf(createAuthor.getText()));
                        insertBook.setImageUrl(String.valueOf(createImage.getText()));

                        Toast.makeText(MainActivity.this, "insert book"+insertBook, Toast.LENGTH_SHORT).show();

                        realm.beginTransaction();

                        RBook reInsBook;
                        reInsBook = realm.createObject(RBook.class);
                        reInsBook.setBook_id(insertBook.getBook_id());
                        reInsBook.setTitle(insertBook.getTitle());
                        reInsBook.setDescription(insertBook.getDescription());
                        reInsBook.setAuthor(insertBook.getAuthor());
                        reInsBook.setImageUrl(insertBook.getImageUrl());
                        realm.copyToRealm(reInsBook);


                        realm.commitTransaction();


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void showUpdateDialog(Context c){
        Log.d("show","update dialog");
    }



    public void readRealmData(){
        //to get realm object list
        realm.beginTransaction();

        realmBookList = realm.where(RBook.class).findAll();



        realm.commitTransaction();
        bookRecyclerAdapter = new BookRecyclerAdapter(getApplicationContext(),realmBookList);
        bookRecycler.setAdapter(bookRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
