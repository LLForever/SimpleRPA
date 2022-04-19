package com.simplerpa.cloudservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月07日 21:34
 */

@RestController
@RequestMapping("/panel-task")
public class PanelTaskController {

    @GetMapping("/test")
    public String test(){
        return "test success";
    }
}
