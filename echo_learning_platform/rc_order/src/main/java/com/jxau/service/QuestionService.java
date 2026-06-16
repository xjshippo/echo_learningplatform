package com.jxau.service;

import com.github.pagehelper.PageInfo;
import com.jxau.annotations.Authorize;
import com.jxau.pojo.*;
import com.jxau.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FeignClient("rc-question-service")
@Service
public interface QuestionService {

    @GetMapping("/question/get/set/questions")
    public ResultEntity<List<MultipleChoicePO>> getQuestions(@RequestParam("QuestionSetId") String QuestionSetId);

    @PostMapping(value = "/question/user/correcting", consumes = "application/json")
    public ResultEntity<ExaminationVO> userMarkingQuestion(@RequestBody Map map);

    @PostMapping(value = "/question/user/get/analysis", consumes = "application/json")
    public ResultEntity<List<AnalysisVO>> getQuestionAnalysis(@RequestBody HashMap map);

    @PostMapping(value = "/question/add/one", consumes = "application/json")
    public ResultEntity<String> addOneQuestion(@RequestBody HashMap<String,String> map);

    @PostMapping(value = "/add/one/ProblemSets", consumes = "application/json")
    public ResultEntity<String> addOneProblemSets(@RequestBody HashMap<String,String>map);

    @PostMapping(value = "/question/user/save/notebook", consumes = "application/json")
    public ResultEntity<String> saveUserNoteBook(@RequestBody HashMap map);

    @PostMapping("/question/user/get/notebook")
    public ResultEntity<List<MistakePO>> getUserNoteBook();

    @GetMapping("/question/get/set/information")
    public ResultEntity<PageInfo<ProblemSetsVO>> getQuestionSetsLevel(@RequestParam("currentPage") Integer currentPage,
                                                                      @RequestParam("pageSize") Integer pageSize,
                                                                      @RequestParam(name = "level" ,defaultValue = "")String level);

    @GetMapping("/question/user/delete/notebook")
    public ResultEntity<String> userDeleteMistake(@RequestParam("questionId")String questionId);


    /**
     * 获取用户错题本错题列表
     * @return
     */
    @GetMapping("/question/get/user/mistakes")
    public ResultEntity<List<AnalysisVO>> getUserMistakes();

    @GetMapping("/question/get/user/mistakes/detail")
    public ResultEntity<List<MultipleChoiceVO>> getUserMistakesDetail(@RequestParam("questionId")String  questionId);

}
