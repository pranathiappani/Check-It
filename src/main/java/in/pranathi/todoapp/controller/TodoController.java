package in.pranathi.todoapp.controller;

import in.pranathi.todoapp.entity.TodoEntity;
import in.pranathi.todoapp.entity.UserEntity;
import in.pranathi.todoapp.repository.TodoRepository;
import in.pranathi.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @GetMapping({"", "/", "/home"})
    public String showHomePage(Model model, @AuthenticationPrincipal User user) {
        UserEntity currentUser = userRepository.findByUsername(user.getUsername()).get();
        model.addAttribute("todos", todoRepository.findByUser(currentUser));
        return "index";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title, @AuthenticationPrincipal User user) {
        UserEntity currentUser = userRepository.findByUsername(user.getUsername()).get();
        TodoEntity newTodo = TodoEntity.builder()
                .title(title)
                .completed(false)
                .user(currentUser)
                .build();
        todoRepository.save(newTodo);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, @AuthenticationPrincipal User user) {
        UserEntity currentUser = userRepository.findByUsername(user.getUsername()).get();
        TodoEntity existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found: "+id));
        if (!existingTodo.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to update this task");
        }
        existingTodo.setCompleted(true);
        todoRepository.save(existingTodo);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        UserEntity currentUser = userRepository.findByUsername(user.getUsername()).get();
        TodoEntity existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found: "+id));
        if (!existingTodo.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to delete this task");
        }
        todoRepository.delete(existingTodo);
        return "redirect:/";
    }

    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Signup page
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new UserEntity());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute UserEntity user) {
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }
}
