package com.example.to_do

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.to_do.Model.ToDoModel
import com.example.to_do.Utils.DataBaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

class AddNewTask : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    // widgets

    private lateinit var editText: EditText
    private lateinit var btn_save: Button

    private lateinit var myDb: DataBaseHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //parameters can be nullable
        val v = inflater.inflate(R.layout.add_newtask, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.edittext)
        btn_save = view.findViewById(R.id.btn_save)

        myDb = DataBaseHelper(requireActivity())

        var isUpdate = false

        val bundle = arguments
        if (bundle != null) { // check whether bundle has data or not
            isUpdate = true
            val task = bundle.getString("task") // retrieving data from bundle
            editText.setText(task)

            if (!task.isNullOrEmpty()) { // save button will enable only if there is a text.
                btn_save.isEnabled = false
            }
        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    btn_save.isEnabled = false
                    btn_save.setBackgroundColor(Color.GRAY) // change the color of button when it is unable.
                } else {
                    btn_save.isEnabled = true
                    btn_save.setBackgroundColor(resources.getColor(R.color.yellow))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val finalIsUpdate = isUpdate
        btn_save.setOnClickListener {
            val text = editText.text.toString()

            if (finalIsUpdate) {
                arguments?.getInt("id")?.let { id -> // let is used for safe call and null check.
                    myDb.updateTask(id, text)
                }
            } else {
                val item = ToDoModel().apply {
                    task = text
                    status = 0
                }
                myDb.insertTask(item)
            }
            dismiss()
        }


    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = requireActivity()
        if (activity is OnDialogCloseListener) {
            (activity as OnDialogCloseListener).onDialogClose(dialog)
        }
    }


}