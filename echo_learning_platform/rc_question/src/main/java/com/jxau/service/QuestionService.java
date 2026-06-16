package com.jxau.service;

import com.github.pagehelper.PageInfo;
import com.jxau.pojo.*;
import com.jxau.util.ResultEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface QuestionService {
    /*ResultEntity<PageInfo<ProblemSetsVO>> getQuestionSet(Integer currentPage,Integer pageSize);*/

    ResultEntity<List<MultipleChoicePO>> getQuestions(String questionSetId);

    ResultEntity<ExaminationVO> MarkExamination(Map map, String currentUserId);

    ResultEntity<List<AnalysisVO>> getQuestionAnalysis(HashMap map);

    ResultEntity<String> addOneQuestion(HashMap<String, String> map);

    ResultEntity<String> addOneProblemSets(HashMap<String, String> map);

    ResultEntity<String> saveQuestionNoteBook(HashMap<String,String> map, String currentUserId);

    ResultEntity<List<MistakePO>> getQuestionNoteBook(String currentUserId);

    ResultEntity<PageInfo<ProblemSetsVO>> getQuestionSetBylevel(Integer currentPage, Integer pageSize, String level);

    ResultEntity<String> deleteMistake(String currentUserId, String questionId);

    ResultEntity<List<AnalysisVO>> getUserMistake(String currentUserId);

    ResultEntity<List<MultipleChoiceVO>> getUserMistakeDetail(String currentUserId, String questionId);
}
