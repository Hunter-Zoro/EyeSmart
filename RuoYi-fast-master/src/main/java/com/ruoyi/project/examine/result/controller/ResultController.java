package com.ruoyi.project.examine.result.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.examine.task.domain.Task;
import com.ruoyi.project.examine.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/examine/result")
public class ResultController extends BaseController {
    private String prefix = "examine/result";
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public String resultList() { return prefix + "/resultList"; }

    @PostMapping("/resultList")
    @ResponseBody
    public TableDataInfo list(Task task){
        startPage();
        task.setAiStatus(3);
        List<Task> list = taskService.selectTaskList(task);
        return getDataTable(list);
    }

}
