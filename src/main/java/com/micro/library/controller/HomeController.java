package com.micro.library.controller;


import com.micro.library.entity.AppUser;
import com.micro.library.entity.LogInfo;
import com.micro.library.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;

@Controller
public class HomeController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserRepository appUserRepository;


    LogInfo logInfo=new LogInfo();

    @RequestMapping({"/","","/home"})
    public String home()
    {
        return "home";
    }

    @RequestMapping("/about")
    public String about()
    {
        return "about";
    }




    @GetMapping({"/signup"})
    public String signup(Model model)
    {
        model.addAttribute("appUser",new AppUser());
        return "signup";
    }

    @PostMapping("/signup/success")
    public String signSuccess(@Valid AppUser appUser)
    {
        if(appUserRepository.countAppUsersByUsername(appUser.getUsername())==1)
            return "signup";
        if (!appUser.getPassword().equals(appUser.getConfirmPassword())) {
            return "signup";
        }
        String pass = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(pass);
        appUser.setRole(AppUser.ROLE_USER);
        appUserRepository.save(appUser);
        return "login";
    }




    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @PostMapping("/login/success")
    public String loginSuccess(Principal principal)
    {
        logInfo.setLoginTime(new Date().toString());
        logInfo.setUser(principal.getName());
        return "/home";
    }

    @RequestMapping("/admin/hello")
    public void m()
    {
          System.out.println("  jaskjfksj"+logInfo.getLoginTime()+"    "+logInfo.getUser());

    }

    @RequestMapping("/logout")
    public String logout(@Valid AppUser appUser)
    {
        return "login";
    }

    @RequestMapping("/profile")
    public String profile(Principal principal, Model model)
    {
        AppUser appUser = appUserRepository.findById(principal.getName()).get();
        model.addAttribute("user_info", appUser);
        return "profile";
    }
    @RequestMapping("/profile/settings")
    public String profileSettings(Principal principal, Model model)
    {
        AppUser appUser = appUserRepository.findById(principal.getName()).get();
        model.addAttribute("appUser", appUser);
        return "profile_settings";
    }

    @RequestMapping("/profile/update")
    public String profileUpdate(@Valid AppUser appUser,Principal principal,Model model)
    {
        if(!principal.getName().equals(appUser.getUsername())) {
            if (appUserRepository.countAppUsersByUsername(appUser.getUsername()) == 1)
                model.addAttribute("name","username is already exist");
                return "profile_settings";
        }
        if (!appUser.getPassword().equals(appUser.getConfirmPassword())) {
            return "profile_settings";
        }
        String pass = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(pass);
        appUserRepository.save(appUser);
        return "home";
    }





}
