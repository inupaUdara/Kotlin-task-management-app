package com.example.taskmanagementapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagementapp.databinding.TaskLayoutBinding
import com.example.taskmanagementapp.fragments.CompleteFragmentDirections
import com.example.taskmanagementapp.fragments.HomeFragmentDirections
import com.example.taskmanagementapp.fragments.ProgressFragmentDirections
import com.example.taskmanagementapp.fragments.TodoFragmentDirections
import com.example.taskmanagementapp.model.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(val itemBinding: TaskLayoutBinding): RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.taskTitle == newItem.taskTitle &&
                    oldItem.taskDesc == newItem.taskDesc &&
                    oldItem.deadline == newItem.deadline
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = differ.currentList[position]

        holder.itemBinding.taskTitle.text = currentTask.taskTitle
        holder.itemBinding.taskDescription.text = currentTask.taskDesc
        holder.itemBinding.taskDeadline.text = currentTask.deadline
        holder.itemBinding.status.text = currentTask.status

         holder.itemView.setOnClickListener {
             if (currentTask.status == "To-Do") {
                 val direction =
                     TodoFragmentDirections.actionTodoFragmentToEditNoteFragment(currentTask)
                 it.findNavController().navigate(direction)
             }else if (currentTask.status == "In-Progress") {
                 val direction =
                     ProgressFragmentDirections.actionProgressFragmentToEditNoteFragment(currentTask)
                 it.findNavController().navigate(direction)
             }else{
                 val direction =
                     CompleteFragmentDirections.actionCompleteFragmentToEditNoteFragment(currentTask)
                 it.findNavController().navigate(direction)
             }
         }
    }
}