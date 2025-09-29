package com.todoapp.controller;

import com.todoapp.model.Task;
import com.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // Helper method to get the current username from Spring Security context
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // auth.getName() returns the username of the currently logged-in user
        return auth.getName();
    }

    // READ: Base handler (shows ALL tasks for the current user)
    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Uses findByUsername to get all tasks for the logged-in user
        model.addAttribute("listTasks", taskRepository.findByUsername(getCurrentUsername())); 
        model.addAttribute("task", new Task());
        model.addAttribute("activeFilter", "all");
        return "index"; 
    }

    // Filter: Show only Pending tasks for the current user
    @GetMapping("/pending")
    public String viewPendingTasks(Model model) {
        // Uses findByUsernameAndCompleted(username, false)
        model.addAttribute("listTasks", taskRepository.findByUsernameAndCompleted(getCurrentUsername(), false));
        model.addAttribute("task", new Task());
        model.addAttribute("activeFilter", "pending");
        return "index"; 
    }

    // Filter: Show only Completed tasks for the current user
    @GetMapping("/completed")
    public String viewCompletedTasks(Model model) {
        // Uses findByUsernameAndCompleted(username, true)
        model.addAttribute("listTasks", taskRepository.findByUsernameAndCompleted(getCurrentUsername(), true));
        model.addAttribute("task", new Task());
        model.addAttribute("activeFilter", "completed");
        return "index"; 
    }


    // CREATE: Handler for saving a new task (Assigns task to current user)
    @PostMapping("/saveTask")
    public String saveTask(@ModelAttribute("task") Task task) {
        task.setUsername(getCurrentUsername()); 
        taskRepository.save(task);
        return "redirect:/"; 
    }

    // UPDATE: Handler for marking a task complete/pending
    @GetMapping("/completeTask/{id}")
    public String completeTask(@PathVariable(value = "id") long id) {
        Task task = taskRepository.findById(id).orElse(null);
        // Security check: Only allow update if task belongs to the current user
        if (task != null && task.getUsername().equals(getCurrentUsername())) {
            task.setCompleted(!task.isCompleted());
            taskRepository.save(task);
        }
        return "redirect:/"; 
    }

    // DELETE: Handler for deleting a task
    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable(value = "id") long id) {
        Task task = taskRepository.findById(id).orElse(null);
        // Security check: Only allow deletion if task belongs to the current user
        if (task != null && task.getUsername().equals(getCurrentUsername())) {
            taskRepository.deleteById(id);
        }
        return "redirect:/"; 
    }
}