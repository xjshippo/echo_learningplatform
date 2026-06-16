package com.jxau.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxau.mapper.QuestionMapper;
import com.jxau.pojo.*;
import com.jxau.service.QuestionService;
import com.jxau.util.ResultEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {


    @Resource
    private QuestionMapper questionMapper;



    @Override
    public ResultEntity<List<MultipleChoicePO>> getQuestions(String questionSetId) {
        try{
            /*
             * 整体逻辑：
             * 根据题组id，查询出所有的题目，再遍历每一个题目，把题目选项封装成数组
             * */
            // 根据题组id得到单选题或者多选
            List<MultipleChoicePO> list = questionMapper.getQuestionChoiceBySetId(questionSetId);

            for (MultipleChoicePO multipleChoicePO : list) {

                // 遍历每一个题目，封装选项数组，填充答案数组
                packageChoices(multipleChoicePO);

            }

            return ResultEntity.successWithData(list);

        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }


    @Override
    @Transactional
    public ResultEntity<ExaminationVO> MarkExamination(Map map, String currentUserId) {
        try {
            /*
             * 逻辑：根据题组的id查询出答案列表，在遍历map，循环比较,记录错误的答案
             * */
            List<String> faultQuestions = new ArrayList<>(20);
            Map map1 = (Map)map.get("answerObj");

            String questionSetId = (String)map.get("QuestionSetId");

            // 根据题组id得到单选题或者多选
            // 这里也可以直接拿出题目的id
            List<MultipleChoicePO> list = questionMapper.getQuestionChoiceBySetId(questionSetId);
            if(list == null) return ResultEntity.falseWithoutData("题组信息获取失败！");
            int wrongNumber = 0;// 错误的数量
            int rightNumber;// 正确的数量
            //List<String> answerList = questionMapper.seleteAnswerByQuestionSetId(questionSetId);
            List<String> answerList = new ArrayList<>();

            for(int i = 1;i <= list.size();i ++){

                MultipleChoicePO items = list.get(i - 1);
                // 兼容两种 key：UUID（如"q001"）和序号（如"1"）
                Object ans = map1.get(items.getUuid());
                if (ans == null) {
                    ans = map1.get(String.valueOf(i));
                }
                if(ans instanceof String){
                    // 判断单选
                    String answer = (String) ans;
                    String s = items.getOp_answer();
                    answerList.add(s);
                    // 把答案加入到错误题目的集合中
                    if(!answer.equals(s)){
                        faultQuestions.add(items.getUuid());
                        wrongNumber ++;
                    }
                }else if(ans instanceof List){
                    // 判断多选
                    List<String> answer = (List<String>) ans;
                    String s = items.getOp_answer();
                    answerList.add(s);// 加入答案列表
                    String[] split = s.split(",");
                    if(answer.size() != split.length){
                        // 把答案加入到错误题目的集合中
                        faultQuestions.add(items.getUuid());
                        wrongNumber ++;
                        continue;
                    }
                    // 循环答案数组，看是否在用户答案中
                    for (String s1 : split) {
                        boolean contains = answer.contains(s1);
                        // 如果不包含
                        if(!contains){
                            // 把答案加入到错误题目的集合中
                            faultQuestions.add(items.getUuid());
                            wrongNumber ++;
                            continue;
                        }

                    }
                }
            }

            int total = answerList.size();
            rightNumber = total - wrongNumber;
            // 统计正确率
            double passRate = total > 0 ? ((double)rightNumber / (double)total) * 100 : 0.0;
            if (Double.isNaN(passRate) || Double.isInfinite(passRate)) {
                passRate = 0.0;
            }
            // 组装题组信息
            ProblemSetsVO problemSet = questionMapper.seleteProblemSetsBySetId(questionSetId);
            List<TagVO> tagVOList = questionMapper.seleteProblemSetTagById(problemSet.getId());
            problemSet.setTagList(tagVOList);



            ExaminationVO examinationVO = new ExaminationVO(passRate,faultQuestions,answerList,rightNumber,wrongNumber,total,problemSet);

            // 把用户和题组挂钩
            String uuid = UUID.randomUUID().toString().replace("-", "");
            ProblemsetsUserPO problemsetsUserPO = new ProblemsetsUserPO(questionSetId, uuid, currentUserId, new Date(), passRate);
            // 插入之前，先检查有没有数据，有的话，在比对正确率，高的话就更新，没得话就插入
            ProblemsetsUserPO problemsetsUserPO1 = questionMapper.seleteQuestionSetUserByQuestionIdAndUserId(questionSetId,currentUserId);
            if(problemsetsUserPO1 == null){
                // 插入数据
                questionMapper.insertQuestionWithUser(problemsetsUserPO);
            }else if(problemsetsUserPO1 != null && problemsetsUserPO1.getPassRate() < passRate){
                // 更新数据
                questionMapper.updateQuestionWithUser(problemsetsUserPO);

            }

            return ResultEntity.successWithData(examinationVO);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<List<AnalysisVO>> getQuestionAnalysis(HashMap map) {
        try{
            String questionSetId = (String) map.get("QuestionSetId");
            String userId = (String) map.get("userId");
            //String answerObj = (String)map.get("answerObj");// 用户答案map
            //Map map1 = JSONObject.parseObject(answerObj, Map.class);
            Map map1 = (Map)map.get("answerObj");

/*            String faultQuestionsList = (String) map.get("faultQuestions");
            List<String> faultQuestions = JSONObject.parseObject(faultQuestionsList, List.class);*/

            List<String> faultQuestions = (ArrayList)map.get("faultQuestions");


            int i = 1;
            //List<MultipleChoicePO> questionChoiceBySetId = questionMapper.getQuestionChoiceBySetId(questionSetId);
            ResultEntity<List<MultipleChoicePO>> questions = this.getQuestions(questionSetId);
            // 声明结果数组
            List<AnalysisVO> analysisVOList = new ArrayList<>();

            if(ResultEntity.SUCCESS.equals(questions.getResult())){
                // 拿到题目信息列表
                List<MultipleChoicePO> data = questions.getData();

                for (MultipleChoicePO datum : data) {

                    // 取出了每个题目
                    String id = datum.getUuid();
                    String question_type = datum.getQuestion_type();
                    String content = datum.getContent();
                    String op_answer = datum.getOp_answer();
                    List<ChoicesVO> choices = datum.getChoices();
                    String analysis = datum.getAnalysis();
                    boolean hasAnalysis = true;
                    if("无解析".equals(analysis)) hasAnalysis = false;
                    // 判断对错逻辑：比对该题号是否在错误题目的列表中
                    boolean isCorrect = !faultQuestions.contains(id);
                    List userChoice = new ArrayList();
                    userChoice.add(map1.get(String.valueOf(i++)));

                    int wrongbook = questionMapper.selectWrongQuestionByQuestionId(id,userId);
                    boolean wrong = wrongbook  == 1 ? true : false;
                    // 组装实体类
                    AnalysisVO analysisVO = new AnalysisVO(id, question_type, content, isCorrect, userChoice, op_answer, choices, analysis, wrong, hasAnalysis);
                    analysisVOList.add(analysisVO);
                }

            }else{
                return ResultEntity.falseWithoutData("题目信息获取失败！");
            }

            return ResultEntity.successWithData(analysisVOList);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<String> addOneQuestion(HashMap<String, String> map) {
        MultipleChoicePO multipleChoicePO = new MultipleChoicePO(
                UUID.randomUUID().toString().replace("_", ""),
                map.get("content"),
                null,
                map.get("source"),
                map.get("analysis"),
                map.get("tags"),
                map.get("question_type"),
                map.get("op_one"),
                map.get("op_two"),
                map.get("op_three"),
                map.get("op_four"),
                map.get("op_answer")
        );
        try {
            int i = questionMapper.insertQuestion(multipleChoicePO);
            if (i == 1){
                return ResultEntity.successWithoutData();
            }else {
                return ResultEntity.falseWithoutData("插入失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<String> addOneProblemSets(HashMap<String, String> map) {
        String pid = UUID.randomUUID().toString().replace("_", "");
        ProblemSetsVO problemSetsVO = new ProblemSetsVO(
                pid,
                null,
                map.get("title"),
                Double.valueOf(map.get("passRate")),
                Integer.parseInt(map.get("number")),
                Integer.parseInt(map.get("passNumbers")),
                new Date(),
                map.get("level")
        );
        try {
            int i = questionMapper.insertProblemSets(problemSetsVO,map.get("skillid"));
            if (i == 1){
                String[] qids = map.get("qid").split(",");
                int j = 0;
                for (int k = 0; k < qids.length; k++) {
                    List<MultipleChoicePO> list = questionMapper.getQuestion(qids);
                    j +=  questionMapper.insertQuestionAndProblemSets(
                            UUID.randomUUID().toString().replace("_",""),
                            qids[k],
                            pid,
                            list.get(k).getQuestion_type()
                    );
                }
                if (j == qids.length - 1) {
                    return ResultEntity.successWithoutData();
                }else {
                    return ResultEntity.falseWithoutData("插入失败");
                }
            }else {
                return ResultEntity.falseWithoutData("插入失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultEntity<String> saveQuestionNoteBook(HashMap map, String currentUserId) {
        try{

            String questionId =(String) map.get("questionId");
            String type = (String)map.get("type");

            Object choices = map.get("userChoice");
            String userChoice;
            if(choices instanceof List){
                ArrayList arrayList = (ArrayList) map.get("userChoice");
                userChoice =arrayList.toString();
                userChoice.replace("[","");
                userChoice.replace("]","");
            }else{
                userChoice =(String) map.get("userChoice");
            }


            String analysis =(String) map.get("analysis");

            Object rchoice = map.get("rightChoice");
            String rightChoice;
            if(rchoice instanceof List){
                ArrayList arrayList = (ArrayList) map.get("userChoice");
                rightChoice =arrayList.toString();
            }else{
                rightChoice = (String)map.get("rightChoice");
            }


            String s = UUID.randomUUID().toString().replace("-","");
            MistakePO mistakePO = new MistakePO(s,questionId,new Date(),userChoice,type,analysis,rightChoice);
            questionMapper.insertUserNoteBook(mistakePO);

            // 插入中间表
            String s1 = UUID.randomUUID().toString().replace("-","");
            MistakesUserPO mistakesUserPO = new MistakesUserPO(s1,currentUserId,s);
            questionMapper.insertUserMistake(mistakesUserPO);

            return ResultEntity.successWithoutData();

        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }

    @Override
    public ResultEntity<List<MistakePO>> getQuestionNoteBook(String currentUserId) {
        try{
            List<MistakePO> list = questionMapper.seleteUserMistakesByUserId(currentUserId);
            return ResultEntity.successWithData(list);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    public ResultEntity<PageInfo<ProblemSetsVO>> getQuestionSetBylevel(Integer currentPage, Integer pageSize, String level) {
        try{
            // 开启分页
            PageHelper.startPage(currentPage,pageSize);
            List<ProblemSetsVO> list = questionMapper.seleteProblemSetsByLevel(level);
            for (ProblemSetsVO problemSetsVO : list) {
                List<TagVO> tagVOList = questionMapper.seleteProblemSetTagById(problemSetsVO.getId());
                problemSetsVO.setTagList(tagVOList);
            }
            PageInfo<ProblemSetsVO> pageInfo =new PageInfo<>(list);
            return ResultEntity.successWithData(pageInfo);
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultEntity<String> deleteMistake(String currentUserId, String questionId) {
        try{
            questionMapper.deleteUserNoteBook(currentUserId,questionId);
            questionMapper.deleteUserMistake(questionId);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }

    /*@Override
    public ResultEntity<List<MultipleChoicePO>> getUserMistake(String currentUserId) {

        try{
            *//*
             * 整体逻辑：
             * 根据题组id，查询出所有的题目，再遍历每一个题目，把题目选项封装成数组
             * *//*
            // 根据用户d得到单选题或者多选题
            List<MultipleChoicePO> list = questionMapper.getMistakeQuestionByUserId(currentUserId);

            for (MultipleChoicePO multipleChoice : list) {
                // 遍历每一个题目，封装选项数组，填充答案数组
                packageChoices(multipleChoice);

            }
            return ResultEntity.successWithData(list);

        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }*/


    @Override
    public ResultEntity<List<AnalysisVO>> getUserMistake(String currentUserId) {
        try{
            /*
             * 整体逻辑：
             * 根据题组id，查询出所有的题目，再遍历每一个题目，把题目选项封装成数组
             * */
            // 根据用户d得到单选题或者多选题

            List<MultipleChoiceVO> list = questionMapper.getMistakeQuestionByUserId(currentUserId);
            List<AnalysisVO> analysisVOList = new ArrayList<>(list.size());
            for (MultipleChoiceVO multipleChoice : list) {
                // 遍历每一个题目，封装选项数组，填充答案数组
                packageChoices(multipleChoice);
                boolean hasAnalysis = true;
                if("无解析".equals(multipleChoice.getAnalysis())) hasAnalysis = false;
                AnalysisVO analysisVO = new AnalysisVO();
                analysisVO.setId(multipleChoice.getUuid());
                analysisVO.setAnalysis_content(multipleChoice.getAnalysis());
                analysisVO.setChoices(multipleChoice.getChoices());
                analysisVO.setHasAnalysis(hasAnalysis);
                analysisVO.setIsCorrect(false);
                analysisVO.setRight_choice(multipleChoice.getOp_answer());
                List userChoice = new ArrayList();
                userChoice.add(multipleChoice.getError_choice());
                analysisVO.setUser_choice(userChoice);
                analysisVO.setQuestion_type(multipleChoice.getQuestion_type());
                analysisVO.setWithinWrongBook(true);
                analysisVO.setContent(multipleChoice.getContent());
                analysisVOList.add(analysisVO);
            }

            return ResultEntity.successWithData(analysisVOList);

        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }
    }


    @Override
    public ResultEntity<List<MultipleChoiceVO>> getUserMistakeDetail(String currentUserId, String questionId) {

        try{
            /*
             * 整体逻辑：
             * 根据题组id，查询出所有的题目，再遍历每一个题目，把题目选项封装成数组
             * */
            // 根据用户d得到单选题或者多选题
            List<MultipleChoiceVO> list = questionMapper.getMistakeQuestionDetail(currentUserId, questionId);

            for (MultipleChoiceVO multipleChoiceVO : list) {
                // 遍历每一个题目，封装选项数组，填充答案数组
                packageChoices(multipleChoiceVO);
            }
            return ResultEntity.successWithData(list);

        }catch (Exception e){
            return ResultEntity.falseWithoutData(e.getMessage());
        }

    }


    private void packageChoices(MultipleChoicePO multipleChoice){

        String[] choices = new String[5];
        choices[1] = "A";
        choices[2] = "B";
        choices[3] = "C";
        choices[4] = "D";

        List<ChoicesVO> chs = new ArrayList();
        ChoicesVO choicesVO;
        for (int i = 1;i <= 4;i ++) {
            if(i == 1){
                choicesVO = new ChoicesVO(choices[i],multipleChoice.getOp_one());
            }else if(i == 2){
                choicesVO = new ChoicesVO(choices[i],multipleChoice.getOp_two());
            }else if(i == 3){
                choicesVO = new ChoicesVO(choices[i],multipleChoice.getOp_three());
            }else{
                choicesVO = new ChoicesVO(choices[i],multipleChoice.getOp_four());
            }
            chs.add(choicesVO);
        }
        multipleChoice.setChoices(chs);

    }
}
