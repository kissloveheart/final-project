package com.hiep.finalproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping(value="/list")
    public String list(){
        return "/management/listCampaign";
    }
}
