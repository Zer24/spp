package org.example.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {
    @GetMapping({"/", "/login"})     public String login()     { return "forward:/login.html"; }
    @GetMapping("/projects")         public String projects()  { return "forward:/projects.html"; }
    @GetMapping("/programmers")      public String programmers(){ return "forward:/programmers.html"; }
    @GetMapping("/users")            public String users()     { return "forward:/users.html"; }
}