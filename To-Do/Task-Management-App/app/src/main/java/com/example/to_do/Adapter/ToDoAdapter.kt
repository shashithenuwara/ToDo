package com.example.to_do.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.AddNewTask
import com.example.to_do.MainActivity
import com.example.to_do.Model.ToDoModel
import com.example.to_do.R
import com.example.to_do.Utils.DataBaseHelper

class ToDoAdapter(private val myDB: DataBaseHelper, private val activity: MainActivity) : // constructor parameters are directly define within the class header

    RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var mList: List<ToDoModel> = listOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // this is a inner class define within the ToDoAdapter class
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.checkBox.text = item.task
        holder.checkBox.isChecked = toBoolean(item.status)
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                myDB.updateStatus(item.id, 1) // if clicked on check box , status is done.
            } else {
                myDB.updateStatus(item.id, 0) // else status is not done yet.
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(mList: List<ToDoModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun deletTask(position: Int) {
        val item = mList[position]
        myDB.deleteTask(item.id)
        val newList = mList.toMutableList()
        newList.removeAt(position)
        mList = newList
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = mList[position]

        val bundle = Bundle().apply {
            putInt("id", item.id)
            putString("task", item.task)
        }

        val task = AddNewTask()  // get Activity data to fragment
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.tag)
    }


    private fun toBoolean(num: Int): Boolean { // private function
        return num != 0
    }

    fun getContext(): Context {
        return activity
    }
}
