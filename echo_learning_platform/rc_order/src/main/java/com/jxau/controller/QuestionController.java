package com.jxau.controller;

import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.*;
import com.jxau.service.QuestionService;
import com.jxau.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/get/set/questions")
    public ResultEntity<List<MultipleChoicePO>> getQuestions(@RequestParam("QuestionSetId") String QuestionSetId){
        try{
            return questionService.getQuestions(QuestionSetId);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/question/user/correcting")
    public ResultEntity<ExaminationVO> userMarkingQuestion(@RequestBody Map map){
        try{
            return questionService.userMarkingQuestion(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/question/user/get/analysis")
    public ResultEntity<List<AnalysisVO>> getQuestionAnalysis(@RequestBody HashMap map){
        try{
            return questionService.getQuestionAnalysis(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/question/add/one")
    public ResultEntity<String> addOneQuestion(@RequestBody HashMap<String,String> map){
        try{
            return questionService.addOneQuestion(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/add/one/ProblemSets")
    public ResultEntity<String> addOneProblemSets(@RequestBody HashMap<String,String>map){
        try{
            return questionService.addOneProblemSets(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @PostMapping("/question/user/save/notebook")
    @Authorize
    public ResultEntity<String> saveUserNoteBook(@RequestBody HashMap map){
        try{
            return questionService.saveUserNoteBook(map);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/question/user/delete/notebook")
    @Authorize
    public ResultEntity<String> userDeleteMistake(@RequestParam("questionId")String questionId){
        try{
            return questionService.userDeleteMistake(questionId);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }



    @PostMapping("/question/user/get/notebook")
    public ResultEntity<List<MistakePO>> getUserNoteBook(){
        try{
            return questionService.getUserNoteBook();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/question/get/set/information")
    public ResultEntity<PageInfo<ProblemSetsVO>> getQuestionSetsLevel(@RequestParam("currentPage") Integer currentPage,
                                                                      @RequestParam("pageSize") Integer pageSize,
                                                                      @RequestParam(name = "level" ,defaultValue = "")String level) {
        try{
            return questionService.getQuestionSetsLevel(currentPage,pageSize,level);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    /**
     * 获取用户错题本错题列表
     * @return
     */
    @GetMapping("/question/get/user/mistakes")
    @Authorize
    public ResultEntity<List<AnalysisVO>> getUserMistakes(){
        try{
            return questionService.getUserMistakes();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @GetMapping("/question/get/user/mistakes/detail")
    @Authorize
    public ResultEntity<List<MultipleChoiceVO>> getUserMistakesDetail(@RequestParam("questionId")String  questionId){
        try{
            return questionService.getUserMistakesDetail(questionId);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

}
