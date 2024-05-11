package com.example.taskmanagementapp.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskmanagementapp.HomeActivity
import com.example.taskmanagementapp.R
import com.example.taskmanagementapp.databinding.FragmentEditTaskBinding
import com.example.taskmanagementapp.model.Task
import com.example.taskmanagementapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EditTaskFragment : Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var tasksViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        tasksViewModel = (activity as HomeActivity).taskViewModel
        currentTask = args.task!!

        binding.updateTaskTitle.setText(currentTask.taskTitle)
        binding.updateTaskDescription.setText(currentTask.taskDesc)
        binding.updateTaskDeadline.setText(currentTask.deadline)

        binding.updateTaskDeadline.setOnClickListener {
            showDateTimePicker()
        }

        binding.btnUpdateTask.setOnClickListener {
            val taskTitle = binding.updateTaskTitle.text.toString().trim()
            val taskDesc = binding.updateTaskDescription.text.toString().trim()
            val deadline = binding.updateTaskDeadline.text.toString().trim()

            if(taskTitle.isNotEmpty()){
                val task = Task(currentTask.id, taskTitle, taskDesc, deadline)
                tasksViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment,false)
            }else{
                Toast.makeText(context, "Please enter task title", Toast.LENGTH_SHORT).show()
            }


        }



    }

    private fun deleteTask(){
        AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialogStyle).apply {
            setTitle("Delete Task")
            setMessage("Do you want to delete this task")
            setPositiveButton("Delete"){_,_ ->
                tasksViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.update_task_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteTask()
                true
            }else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                showTimePicker(year, monthOfYear, dayOfMonth)
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePicker(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedDateTime = Calendar.getInstance()
                selectedDateTime.set(year, month, day, hourOfDay, minute)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val formattedDateTime = sdf.format(selectedDateTime.time)
                binding.updateTaskDeadline.setText(formattedDateTime)
            }, hour, minute, false)
        timePickerDialog.show()
    }

}