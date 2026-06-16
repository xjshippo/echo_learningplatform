package com.jxau.mapper;

import com.jxau.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<ProblemSetsVO> seleteProblemSets();

    List<MultipleChoicePO> getQuestionChoiceBySetId(@Param("questionSetId") String questionSetId);

    List<TagVO> seleteProblemSetTagById(@Param("id")String id);

    List<String> seleteAnswerByQuestionSetId(@Param("questionSetId")String questionSetId);

    ProblemsetsUserPO seleteQuestionSetUserByQuestionIdAndUserId(@Param("questionSetId")String questionSetId,@Param("currentUserId") String currentUserId);

    void insertQuestionWithUser(ProblemsetsUserPO problemsetsUserPO);

    void updateQuestionWithUser(ProblemsetsUserPO problemsetsUserPO);

    List<String> getAnalysisBySetId(@Param("questionSetId")String questionSetId);

    List<String> getQuestionChoiceIdBySetId(@Param("questionSetId")String questionSetId);

    int insertQuestion(MultipleChoicePO multipleChoicePO);

    int insertProblemSets(ProblemSetsVO problemSetsVO,@Param("skillid") String skillid);

    int insertQuestionAndProblemSets(@Param("id") String id,@Param("qid") String qid,@Param("pid") String pid,@Param("type")String type);

    List<MultipleChoicePO> getQuestion(String[] qids);

    void insertUserNoteBook(MistakePO mistakePO);

    void insertUserMistake(MistakesUserPO mistakesUserPO);

    List<MistakePO> seleteUserMistakesByUserId(@Param("currentUserId") String currentUserId);

    int selectWrongQuestionByQuestionId(@Param("id") String id,@Param("userId") String userId);

    ProblemSetsVO seleteProblemSetsBySetId(@Param("questionSetId") String questionSetId);

    List<ProblemSetsVO> seleteProblemSetsByLevel(@Param("level")String level);

    void deleteUserMistake(@Param("questionId") String questionId);

    void deleteUserNoteBook(@Param("currentUserId")String currentUserId, @Param("questionId")String questionId);

    List<MultipleChoiceVO> getMistakeQuestionByUserId(@Param("currentUserId")String currentUserId);

    List<MultipleChoiceVO> getMistakeQuestionDetail(@Param("currentUserId")String currentUserId,@Param("questionId") String questionId);
}
