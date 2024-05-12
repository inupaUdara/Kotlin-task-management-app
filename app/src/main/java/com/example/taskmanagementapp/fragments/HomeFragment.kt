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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.taskmanagementapp.HomeActivity
import com.example.taskmanagementapp.R
import com.example.taskmanagementapp.adapter.TaskAdapter
import com.example.taskmanagementapp.databinding.FragmentHomeBinding
import com.example.taskmanagementapp.model.Task
import com.example.taskmanagementapp.viewmodel.TaskViewModel

class HomeFragment : Fragment(R.layout.fragment_home), MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.todo.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTodoFragment()
            it.findNavController().navigate(action)
        }

        binding.progress.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProgressFragment()
            it.findNavController().navigate(action)
        }
        binding.complete.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_completeFragment)
        }

//        taskViewModel = (activity as HomeActivity).taskViewModel

        binding.addTaskFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }



    }


    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)
    }
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }


}