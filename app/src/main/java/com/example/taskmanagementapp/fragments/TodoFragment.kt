package com.example.taskmanagementapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.taskmanagementapp.HomeActivity
import com.example.taskmanagementapp.R
import com.example.taskmanagementapp.adapter.TaskAdapter
import com.example.taskmanagementapp.databinding.FragmentTodoBinding
import com.example.taskmanagementapp.model.Task
import com.example.taskmanagementapp.viewmodel.TaskViewModel

class TodoFragment : Fragment(R.layout.fragment_todo), SearchView.OnQueryTextListener, MenuProvider {

    private var todoBinding: FragmentTodoBinding? = null

    private val binding get() = todoBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        todoBinding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        setupHomeRecycleView()


    }

    private  fun updateUI(task: List<Task>?){
        if (task != null){
            if (task.isNotEmpty()){
                binding.emptyTaskImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyTaskImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecycleView(){
        taskAdapter = TaskAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = taskAdapter
        }


            taskViewModel.getTodoTasks().observe(viewLifecycleOwner) { task ->
                taskAdapter.differ.submitList(task)
                updateUI(task)

        }
    }

    private  fun searchTask(query: String?){
        val searchQuery = "%$query"

        taskViewModel.searchTask(searchQuery).observe(this) {list ->
            taskAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchTask(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        todoBinding = null
    }
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.search_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }


}