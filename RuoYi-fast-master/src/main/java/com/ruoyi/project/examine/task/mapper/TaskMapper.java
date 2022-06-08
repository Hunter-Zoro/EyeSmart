package com.ruoyi.project.examine.task.mapper;

import com.ruoyi.project.examine.task.domain.Task;
import com.ruoyi.project.examine.task.domain.TaskDetail;

import java.util.List;

public interface TaskMapper {

    public List<Task> selectTaskList(Task task);

    public void executeSql(String sql);

    public List<TaskDetail> selectTaskDetailList(TaskDetail taskDetail);

    public int insertTask(Task task);

    public int insertTaskDetail(TaskDetail taskDetail);

    public int updateTask(Task task);

    public int updateTaskDetail(TaskDetail taskDetail);

    public int deleteTaskDetailByTask(TaskDetail taskDetail);

    public int deleteTaskDetailByIds(Long[] ids);
}
