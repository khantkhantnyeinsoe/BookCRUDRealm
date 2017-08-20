package com.example.khantkhantnyeinsoe.bookwithrealm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khantkhantnyeinsoe.bookwithrealm.MainActivity;
import com.example.khantkhantnyeinsoe.bookwithrealm.R;
import com.example.khantkhantnyeinsoe.bookwithrealm.UpdateBookActivity;
import com.example.khantkhantnyeinsoe.bookwithrealm.data.Book;
import com.example.khantkhantnyeinsoe.bookwithrealm.data.RBook;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by khantkhantnyeinsoe on 8/6/17.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.BookRecyclerViewholder>{
    Context context;
    List<RBook> bookList;


    BookRecyclerAdapter bookRecyclerAdapter;
    RecyclerView bookRecycler;

    TextView updateTitle;
    TextView updateDescription;
    TextView updateAuthor;
    TextView updateImage;

    List<RBook> realmBookList;

    Realm realm = Realm.getDefaultInstance();

    public BookRecyclerAdapter(Context context, List<RBook> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public BookRecyclerViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card,parent,false);
        return new BookRecyclerViewholder(view);
    }

    @Override
    public void onBindViewHolder(BookRecyclerViewholder holder, int position) {
        Log.d("url : ",bookList.get(position).getImageUrl());
        Picasso.with(context).load(String.valueOf(bookList.get(position).getImageUrl())).into(holder.bookImage);
        holder.bookTitle.setText(bookList.get(position).getTitle());
        holder.bookDescription.setText(bookList.get(position).getDescription());
        holder.bookAuthor.setText(bookList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookRecyclerViewholder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookTitle;
        TextView bookDescription;
        TextView bookAuthor;

        public BookRecyclerViewholder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RBook getUpdateBook = bookList.get(getLayoutPosition());
                    String MY_PREFS_NAME = "MyPrefsFile";

                    SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString("title", getUpdateBook.getTitle());
                    editor.putString("author", getUpdateBook.getAuthor());
                    editor.putString("image", getUpdateBook.getImageUrl());
                    editor.putString("description",getUpdateBook.getDescription());
                    editor.putString("bookid",getUpdateBook.getBook_id());

                    editor.apply();

                    Log.d("update book",getUpdateBook.getTitle());

                    Intent intent = new Intent(context,UpdateBookActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    RBook getRBook = bookList.get(getLayoutPosition());


                    realm.beginTransaction();


                    Log.d("get id: ",getRBook.getTitle());



                    RealmResults<RBook> deleteBook = realm.where(RBook.class)
                            .equalTo("book_id",getRBook.getBook_id()).findAll();

                    Log.d("delete book size : ",String.valueOf(deleteBook.size()));

                    deleteBook.deleteAllFromRealm();

                    realm.commitTransaction();

                    Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                    return false;
                }
            });

            bookImage = (ImageView) itemView.findViewById(R.id.book_image);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookDescription = (TextView) itemView.findViewById(R.id.book_description);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);


        }
    }
    public interface BookClickListener {
        public void OnItemClick(Book itemClicked);
    }
}
