package com.example.root.mynoteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    List<Notes> list;
    DatabaseHelper db;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        list = new ArrayList<>();
        db = new DatabaseHelper(this);
        list.addAll(db.getAllNotes());

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showNoteDialog(false,null,-1);
            }
        });

        myAdapter = new MyAdapter(list,this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionDialog (position);
            }
        }));
    }

    private void showActionDialog (final int position) {
        CharSequence colors[] = new CharSequence[] {"Edit","Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    showNoteDialog(true,list.get(position),position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private ClickListener clickListener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener (Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            this.gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View v = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if (v != null && clickListener != null)
                        clickListener.onLongClick(v,recyclerView.getChildAdapterPosition(v));
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View v = rv.findChildViewUnder(e.getX(),e.getY());
            if (v != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(v,rv.getChildAdapterPosition(v));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick (View view, int position);
        public void onLongClick (View view, int position);
    }

    public void showNoteDialog (final boolean shouldUpdate, final Notes note, final int position) {
        View view = LayoutInflater.from (getApplicationContext()).inflate(R.layout.note_dialog,null);
        final EditText editText = view.findViewById(R.id.noteEditText);
        if (shouldUpdate && note != null) {
            editText.setText(note.getNote());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        builder
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && note != null) {
                    updateNote(editText.getText().toString(),position);
                } else {
                    createNote(editText.getText().toString());
                }
            }
        });
    }

    public void createNote (String note) {
        long id = db.insert(note);
        Notes n = db.getNotes(id);
        if (n != null) {
            list.add (0,n);
            myAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Empty Note" + String.valueOf(id), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNote (String note, int position) {
        Notes n = list.get(position);
        n.setNote(note);
        db.updateNote(n);
        list.set (position,n);
        myAdapter.notifyDataSetChanged();
    }

    public void deleteNote (int position) {
        db.deleteNote(list.get(position));
        list.remove (position);
        myAdapter.notifyDataSetChanged();
    }
}
