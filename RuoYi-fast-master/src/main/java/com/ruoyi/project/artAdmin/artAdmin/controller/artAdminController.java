package com.ruoyi.project.artAdmin.artAdmin.controller;

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

import static com.ruoyi.common.utils.PageUtils.startPage;

@Controller
@RequestMapping("/artAdmin/artAdmin")
public class artAdminController extends BaseController {
    private String prefix = "artAdmin/artAdmin";
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public String artificialList() {
        return prefix + "/artAdminList";
    }

    @PostMapping("/artAdminList")
    @ResponseBody
    public TableDataInfo list(Task task) {
        startPage();
        List<Task> table = taskService.selectTaskList(task);
        return getDataTable(table);
    }
}
