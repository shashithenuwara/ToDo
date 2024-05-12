package com.example.to_do

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.Adapter.ToDoAdapter
import com.example.to_do.Model.ToDoModel
import com.example.to_do.Utils.DataBaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), OnDialogCloseListener {

    private lateinit var recview: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var myDB: DataBaseHelper

    private var mList: List<ToDoModel> = listOf()
    private lateinit var adapter: ToDoAdapter



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recview = findViewById(R.id.recview)
        fab = findViewById(R.id.fab)
        myDB = DataBaseHelper(this@MainActivity)

        mList = ArrayList()
        adapter = ToDoAdapter(myDB, this@MainActivity)

        mList = myDB.getAllTasks().reversed()
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()

        recview.setHasFixedSize(true)
        recview.layoutManager = LinearLayoutManager(this)
        recview.adapter = adapter



        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }

        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(recview)


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDialogClose(dialogInterface: DialogInterface) {

        mList = myDB.getAllTasks().reversed()
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()

    }
}