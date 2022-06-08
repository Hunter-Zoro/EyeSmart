package com.ruoyi.project.artificial.artificial.controller;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.datasource.DynamicDataSourceContextHolder;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.examine.task.domain.Task;
import com.ruoyi.project.examine.task.domain.TaskDetail;
import com.ruoyi.project.examine.task.service.TaskService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/artificial/artificial")
public class ArtificialController extends BaseController {
    private String prefix = "artificial/artificial";
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public String artificialList() {
        return prefix + "/artList";
    }

    @PostMapping("/artList")
    @ResponseBody
    public TableDataInfo list(Task task) {
        startPage();
        task.setAiStatus(3);
        List<Task> table = taskService.selectTaskList(task);
        return getDataTable(table);
    }
    @GetMapping("/taskDetail/{taskId}")
    public String taskDetail(@PathVariable("taskId") Long id, ModelMap mmap) {
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId(id);

        mmap.put("taskDetail", taskDetail);
        return prefix+"/resultList";
    }
    @PostMapping("/test")
    @ResponseBody
    public String test(String a) {
        taskService.executeSql(a);
        return "success";
    }
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        if (ArrayUtils.contains(Convert.toLongArray(ids), getUserId())) {
            return error("当前数据不能删除");
        }

        Long[] taskDetailIds = Convert.toLongArray(ids);
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setId(taskDetailIds[0]);
        List<TaskDetail> taskDetails = taskService.selectTaskDetailList(taskDetail);
        Task task = new Task();
        task.setId(taskDetails.get(0).getTaskId());
        List<Task> tasks = taskService.selectTaskList(task);
        tasks.get(0).setErrorNum(tasks.get(0).getErrorNum()-taskDetailIds.length);
        int i = taskService.deleteTaskDetailByIds(ids);
        taskService.updateTask(tasks.get(0));
        return toAjax(i);
    }

}
