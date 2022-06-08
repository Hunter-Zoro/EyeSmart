package com.ruoyi.project.examine.task.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 任务表task
 */

public class Task extends BaseEntity {
    //ID
    private Long id;

    //提交时间
    private String content;

    //审核结果
    private String result;

    //ai审核状态
    private int aiStatus;

    //人工审核状态
    private int artStatus;

    //人工审核结果
    private String artResult;

    //提交时间
    private Date tjTime;

    //审核结束时间
    private Date endTime;

    //ai分析结束时间
    private Date aiTime;

    //人工审核结束时间
    private Date artTime;

    //文件路径
    private String filePath;

    //违规数量
    private int errorNum;

    public int getArtStatus() {
        return artStatus;
    }

    public void setArtStatus(int artStatus) {
        this.artStatus = artStatus;
    }

    public Date getTjTime() {
        return tjTime;
    }

    public void setTjTime(Date tjTime) {
        this.tjTime = tjTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getArtResult() {
        return artResult;
    }

    public void setArtResult(String artResult) {
        this.artResult = artResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getAiStatus() {
        return aiStatus;
    }

    public void setAiStatus(int aiStatus) {
        this.aiStatus = aiStatus;
    }

    public Date getAiTime() {
        return aiTime;
    }

    public void setAiTime(Date aiTime) {
        this.aiTime = aiTime;
    }

    public Date getArtTime() {
        return artTime;
    }

    public void setArtTime(Date artTime) {
        this.artTime = artTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }
}
