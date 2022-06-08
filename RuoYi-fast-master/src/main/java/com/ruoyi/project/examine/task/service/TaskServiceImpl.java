package com.ruoyi.project.examine.task.service;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.examine.task.domain.Task;
import com.ruoyi.project.examine.task.domain.TaskDetail;
import com.ruoyi.project.examine.task.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> selectTaskList(Task task) {

        return taskMapper.selectTaskList(task);
    }

    @Override
    public void executeSql(String sql) {
        taskMapper.executeSql(sql);
    }

    @Override
    public List<TaskDetail> selectTaskDetailList(TaskDetail taskDetail) {
        return taskMapper.selectTaskDetailList(taskDetail);
    }

    @Override
    public int insertTask(Task task) {
        return taskMapper.insertTask(task);
    }

    @Override
    public int insertTaskDetail(TaskDetail taskDetail) {
        return taskMapper.insertTaskDetail(taskDetail);
    }

    @Override
    public int updateTask(Task task) {
        return taskMapper.updateTask(task);
    }

    @Override
    public int updateTaskDetail(TaskDetail taskDetail) {
        return taskMapper.updateTaskDetail(taskDetail);
    }

    @Override
    public int deleteTaskDetailByTask(TaskDetail taskDetail) {
        return taskMapper.deleteTaskDetailByTask(taskDetail);
    }

    @Override
    public int deleteTaskDetailByIds(String ids) {
        Long[] taskDetailIds = Convert.toLongArray(ids);
        return taskMapper.deleteTaskDetailByIds(taskDetailIds);
    }
}
