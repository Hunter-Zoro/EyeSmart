package com.ruoyi.project.examine.task.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.common.CommonController;
import com.ruoyi.project.examine.task.domain.Task;
import com.ruoyi.project.examine.task.domain.TaskDetail;
import com.ruoyi.project.examine.task.service.TaskService;
import com.ruoyi.test.Test;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.ruoyi.common.utils.saveOffice.pdfSave;
import static com.ruoyi.common.utils.saveOffice.wordSave;


@Controller
@RequestMapping("/examine/task")
public class TaskController extends BaseController {
    private String prefix = "examine/task";

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    private static final String FILE_DELIMETER = ",";

    @Autowired
    private TaskService taskService;
    

    @GetMapping()
    public String taskList() {
        return prefix + "/taskList";
    }

    @PostMapping("/taskList")
    @ResponseBody
    public TableDataInfo list(Task task) {
        startPage();
        List<Task> list = taskService.selectTaskList(task);
        //System.out.println(HttpUtils.sendPost("http://textbook.videojj.com/textbookReview","{\"id\":\"asd2a\",\"dataUrl\":\"http://45fq062750.zicp.vip/1.txt\"}"));

        //遍历所有任务
        for (Task t : list
        ) {
            //当任务为待审核进行操作
            if (t.getAiStatus() == 1) {
                try {
                    JSONObject json = JSONObject.parseObject(HttpUtils.sendPost("http://textbook.videojj.com/textbookReview", "{\"id\":\"" + t.getId() + "\",\"dataUrl\":\""+serverConfig.getUrl()+t.getFilePath()+"\"}"));
                    JSONArray jsonArray = json.getJSONArray("illegalInfos");
                    // System.out.println(json.get("illegalInfos"));
                    Iterator<Object> iterator = jsonArray.iterator();

                    //遍历所有违规词
                    while (iterator.hasNext()) {
                        JSONObject object = (JSONObject) iterator.next();
                        String errorType = object.get("type").toString();
                        int pageNum = Integer.parseInt(object.get("page").toString());
                        String content="";
                        if(object.containsKey("msg")) {
                            content = object.get("msg").toString();
                        }
                        TaskDetail taskDetail = new TaskDetail();
                        taskDetail.setTaskId(t.getId());
                        taskDetail.setErrorType(errorType);
                        taskDetail.setContent(content);
                        taskDetail.setPageNum(pageNum);
                        taskService.insertTaskDetail(taskDetail);

                    }

                    //查询任务违规数量
                    TaskDetail taskDetail = new TaskDetail();
                    taskDetail.setTaskId(t.getId());
                    int count = taskService.selectTaskDetailList(taskDetail).size();
                    t.setAiStatus(3);
                    t.setErrorNum(count);
                    taskService.updateTask(t);

                    String filePath = t.getFilePath().replace("/profile",RuoYiConfig.getProfile());
                    String format = filePath.substring(filePath.lastIndexOf('.')+1);
                    if(format.equals("doc") || format.equals("docx")){
                        wordSave(filePath);
                    }else if(format.equals("pdf")){
                        pdfSave(filePath);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return getDataTable(list);
    }

    @GetMapping("/taskDetail/{taskId}")
    public String taskDetail(@PathVariable("taskId") Long id, ModelMap mmap) {
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId(id);

        mmap.put("taskDetail", taskDetail);
        return "examine/taskDetail/taskDetailList";
    }

    @PostMapping("/taskDetailList/{taskId}")
    @ResponseBody
    public TableDataInfo taskDetailList(@PathVariable("taskId") Long id) {
        startPage();
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId(id);
        Task task = new Task();
        List<TaskDetail> list = taskService.selectTaskDetailList(taskDetail);
        for (TaskDetail t: list
             ) {
            task.setId(id);
            t.setTaskName(taskService.selectTaskList(task).get(0).getContent());
        }
        return getDataTable(list);
    }

    @PostMapping("/uploads")
    @ResponseBody
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                // 上传并返回新文件名称

                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());

                Task task = new Task();
                task.setContent(file.getOriginalFilename());
                task.setTjTime(new Date());
                task.setFilePath(fileName);
                task.setAiStatus(1);
                task.setArtStatus(-1);
                taskService.insertTask(task);

            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    @PostMapping("/aiAgain/{taskId}")
    @ResponseBody
    public AjaxResult again(@PathVariable("taskId") Long id) {
        Task t = new Task();
        t.setId(id);
        t=taskService.selectTaskList(t).get(0);
        try {
            JSONObject json = JSONObject.parseObject(HttpUtils.sendPost("http://textbook.videojj.com/textbookReview", "{\"id\":\"" + t.getId() + "\",\"dataUrl\":\"http://45fq062750.zicp.vip/access.txt\"}"));
            JSONArray jsonArray = json.getJSONArray("illegalInfos");
            // System.out.println(json.get("illegalInfos"));
            Iterator<Object> iterator = jsonArray.iterator();
            TaskDetail delTaskDetail = new TaskDetail();
            delTaskDetail.setTaskId(id);
            taskService.deleteTaskDetailByTask(delTaskDetail);
            //遍历所有违规词
            while (iterator.hasNext()) {
                JSONObject object = (JSONObject) iterator.next();
                String errorType = object.get("type").toString();
                int pageNum = (int) object.get("page");
                String content = object.get("msg").toString();
                TaskDetail taskDetail = new TaskDetail();
                taskDetail.setTaskId(t.getId());
                taskDetail.setErrorType(errorType);
                taskDetail.setContent(content);
                taskDetail.setPageNum(pageNum);
                taskService.insertTaskDetail(taskDetail);

            }

            //查询任务违规数量
            TaskDetail taskDetail = new TaskDetail();
            taskDetail.setTaskId(t.getId());
            int count = taskService.selectTaskDetailList(taskDetail).size();
            t.setAiStatus(3);
            t.setErrorNum(count);
            taskService.updateTask(t);
        }catch (Exception e){
            System.out.println(e);
            return AjaxResult.error();

        }

        return AjaxResult.success();
    }

}
