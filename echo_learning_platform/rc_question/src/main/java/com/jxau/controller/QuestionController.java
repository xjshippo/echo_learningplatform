package com.jxau.controller;

import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.*;
import com.jxau.service.QuestionService;
import com.jxau.util.ResultEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionController {


    @Resource
    private QuestionService questionService;

    /**
     * 获取题目信息
     * @param
     * @return
     */
    @GetMapping("/question/get/set/questions")
    public ResultEntity<List<MultipleChoicePO>> getQuestions(@RequestParam("QuestionSetId") String QuestionSetId){

        // 获取题组的id
        // String questionSetId = (String) map.get("QuestionSetId");
        return questionService.getQuestions(QuestionSetId);
    }

    /**
     * 改卷
     * @param map
     *
     * @return
     */
    @PostMapping("/question/user/correcting")
    public ResultEntity<ExaminationVO> userMarkingQuestion(@RequestBody Map map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取用户的id是为了关联用户的成绩，后面获取奖牌，统计成绩
        String currentUserId =(String) request.getHeader("currentUser");
        return questionService.MarkExamination(map,currentUserId);
    }

    /**
     * 获取题组的解析
     * @param map
     * @return
     */
    @PostMapping("/question/user/get/analysis")
    public ResultEntity<List<AnalysisVO>> getQuestionAnalysis(@RequestBody HashMap map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId =(String) request.getHeader("currentUser");
        map.put("userId",currentUserId);
        return questionService.getQuestionAnalysis(map);
    }

    @PostMapping("/question/add/one")
    public ResultEntity<String> addOneQuestion(@RequestBody HashMap<String,String> map){
        return questionService.addOneQuestion(map);
    }

    @PostMapping("/add/one/ProblemSets")
    public ResultEntity<String> addOneProblemSets(@RequestBody HashMap<String,String>map){
        return questionService.addOneProblemSets(map);
    }


    /**
     * 用户添加错题本
     * @param map
     * @return
     */
    @PostMapping("/question/user/save/notebook")
    public ResultEntity<String> saveUserNoteBook(@RequestBody HashMap map){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return questionService.saveQuestionNoteBook(map,currentUserId);
    }

    /**
     * 删除错题信息
     * @param questionId
     * @return
     */
    @GetMapping("/question/user/delete/notebook")
    public ResultEntity<String> userDeleteMistake(@RequestParam("questionId")String questionId){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return questionService.deleteMistake(currentUserId,questionId);
    }

    /**
     * 我的错题
     * @return
     */
    @PostMapping("/question/user/get/notebook")
    public ResultEntity<List<MistakePO>> getUserNoteBook(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return questionService.getQuestionNoteBook(currentUserId);
    }

    /**
     * 获取题组信息
     * @return
     */
    @GetMapping("/question/get/set/information")
    public ResultEntity<PageInfo<ProblemSetsVO>> getQuestionSetsLevel(@RequestParam("currentPage") Integer currentPage,
                                                                      @RequestParam("pageSize") Integer pageSize,
                                                                      @RequestParam(name = "key", defaultValue = "")String key){

        // 前端传 key=advance(进阶) / key=race(困难)，映射为中文 level
        String level = "";
        if ("advance".equals(key)) {
            level = "进阶";
        } else if ("race".equals(key)) {
            level = "困难";
        }
        return questionService.getQuestionSetBylevel(currentPage,pageSize,level);
    }



/*    *//**
     * 获取用户错题本错题列表
     * @return
     *//*
    @GetMapping("/question/get/user/mistakes")
    public ResultEntity<List<MultipleChoicePO>> getUserMistakes(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return questionService.getUserMistake(currentUserId);
    }*/

    /**
     * 获取用户错题本错题列表
     * @return
     */
    @GetMapping("/question/get/user/mistakes")
    public ResultEntity<List<AnalysisVO>> getUserMistakes(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return questionService.getUserMistake(currentUserId);
    }


    /**
     * 获取单个错题详细信息
     * @return
     */
    @GetMapping("/question/get/user/mistakes/detail")
    public ResultEntity<List<MultipleChoiceVO>> getUserMistakesDetail(@RequestParam("questionId")String  questionId){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String currentUserId = (String) request.getHeader("currentUser");
        return questionService.getUserMistakeDetail(currentUserId, questionId);
    }


}
