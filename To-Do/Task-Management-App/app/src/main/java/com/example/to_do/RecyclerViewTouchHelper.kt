package com.example.to_do

import android.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.Adapter.ToDoAdapter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat


class RecyclerViewTouchHelper(private val adapter: ToDoAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.RIGHT) {
            AlertDialog.Builder(adapter.getContext())
                .setTitle("Delete Task")
                .setMessage("Are You Sure ?")
                .setPositiveButton("Yes") { dialog, _ ->
                    adapter.deletTask(position)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    adapter.notifyItemChanged(position)
                    dialog.dismiss()
                }
                .show()
        } else {
            adapter.editItem(position)
        }
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        // Draw background color and icon for left swipe (update)
        if (dX < 0) {
            val itemView = viewHolder.itemView
            val background = ColorDrawable(Color.GREEN)
            background.setBounds(itemView.left, itemView.top, itemView.right + dX.toInt(), itemView.bottom)
            background.draw(c)

            // Draw icon
            val icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.edit)
            val iconMargin = (itemView.height - icon?.intrinsicHeight!!) / 2
            val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            val iconBottom = iconTop + icon.intrinsicHeight
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(c)
        }

        // Draw background color and icon for right swipe (delete)
        if (dX > 0) {
            val itemView = viewHolder.itemView
            val background = ColorDrawable(Color.RED)
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            // Draw icon
            val icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.delete)
            val iconMargin = (itemView.height - icon?.intrinsicHeight!!) / 2
            val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            val iconBottom = iconTop + icon.intrinsicHeight
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(c)
        }
    }




}
