package com.example.taskmanagementapp.fragments

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
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.taskmanagementapp.HomeActivity
import com.example.taskmanagementapp.R
import com.example.taskmanagementapp.databinding.FragmentAddTaskBinding
import com.example.taskmanagementapp.model.Task
import com.example.taskmanagementapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment(R.layout.fragment_add_task), MenuProvider {

    private var addTaskBinding: FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var tasksViewModel: TaskViewModel
    private lateinit var addTaskView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addTaskBinding = FragmentAddTaskBinding.inflate(inflater, container, false)

        // Set the windowSoftInputMode attribute for the hosting Activity
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        tasksViewModel = (activity as HomeActivity).taskViewModel
        addTaskView = view

        binding.taskDeadline.setOnClickListener {
            showDateTimePicker()
        }

        binding.btnAddTask.setOnClickListener {
            saveTask(addTaskView)
        }
    }

    private fun saveTask(view: View){
        val taskTitle = binding.taskTitle.text.toString().trim()
        val taskDesc = binding.taskDescription.text.toString().trim()
        val deadline = binding.taskDeadline.text.toString().trim()

        if(taskTitle.isNotEmpty()){
            val task = Task(0, taskTitle, taskDesc, deadline)
            tasksViewModel.addTask(task)

            Toast.makeText(addTaskView.context, "Task Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment,false)
        }else{
            Toast.makeText(addTaskView.context, "Please enter task title", Toast.LENGTH_SHORT).show()
        }
    }




    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.add_task_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
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
                binding.taskDeadline.setText(formattedDateTime)
            }, hour, minute, false)
        timePickerDialog.show()
    }
}