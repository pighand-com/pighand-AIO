package com.pighand.aio.service.CMS.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import com.pighand.aio.domain.CMS.QuestionBankDomain;
import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.mapper.CMS.QuestionBankMapper;
import com.pighand.aio.service.CMS.QuestionAnswerService;
import com.pighand.aio.service.CMS.QuestionBankService;
import com.pighand.aio.service.CMS.QuestionSetService;
import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.service.IoT.DeviceTaskService;
import com.pighand.aio.vo.CMS.QuestionAnswerResultVO;
import com.pighand.aio.vo.CMS.QuestionBankVO;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.mybatisflex.core.query.QueryMethods.distinct;
import static com.mybatisflex.core.query.QueryMethods.notExists;
import static com.pighand.aio.domain.CMS.table.QuestionAnswerTableDef.QUESTION_ANSWER;
import static com.pighand.aio.domain.CMS.table.QuestionBankTableDef.QUESTION_BANK;
import static com.pighand.aio.domain.CMS.table.QuestionSetTableDef.QUESTION_SET;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Service
@AllArgsConstructor
public class QuestionBankServiceImpl extends BaseServiceImpl<QuestionBankMapper, QuestionBankDomain>
    implements QuestionBankService {

    private final DeviceService deviceService;
    private final DeviceTaskService deviceTaskService;
    private final QuestionSetService questionSetService;
    private final QuestionAnswerService questionAnswerService;

    /**
     * 创建
     *
     * @param questionBankVO
     * @return
     */
    @Override
    public QuestionBankVO create(QuestionBankVO questionBankVO) {
        super.mapper.insert(questionBankVO);

        return questionBankVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public QuestionBankDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param questionBankVO
     * @return PageOrList<QuestionBankVO>
     */
    @Override
    public PageOrList<QuestionBankVO> query(QuestionBankVO questionBankVO) {
        questionBankVO.setJoinTables(QUESTION_SET.getTableName(), QUESTION_SET.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(QUESTION_BANK.NAME.like(questionBankVO.getName(), VerifyUtils::isNotEmpty));
        queryWrapper.and(QUESTION_BANK.DESCRIPTION.like(questionBankVO.getDescription(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(QUESTION_BANK.SELECT_TYPE.eq(questionBankVO.getSelectType(), VerifyUtils::isNotEmpty));
        queryWrapper.and(QUESTION_BANK.RANDOM_NUMBER.eq(questionBankVO.getRandomNumber(), VerifyUtils::isNotEmpty));

        return super.mapper.query(questionBankVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param questionBankVO
     */
    @Override
    public void update(QuestionBankVO questionBankVO) {
        super.mapper.update(questionBankVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 获取题
     *
     * @param id
     * @return
     */
    @Override
    public List<QuestionSetDomain> getSet(Long id) {
        LoginUser loginUser = Context.getLoginUser();

        QuestionBankDomain questionBankDomain = this.getById(id);

        // 答题间隔
        this.checkFrequencyGap(questionBankDomain);

        // 随机抽题
        if (questionBankDomain.getSelectType() == 20) {
            // 查询所有已答题目的不重复id
            List<Long> answeredIds =
                questionAnswerService.queryChain().select(distinct(QUESTION_ANSWER.QUESTION_SET_ID))
                    .where(QUESTION_ANSWER.QUESTION_BANK_ID.eq(id))
                    .and(QUESTION_ANSWER.CREATOR_ID.eq(loginUser.getId())).groupBy(QUESTION_ANSWER.QUESTION_SET_ID)
                    .listAs(Long.class);

            // 查询总题目数。全部答过，清空
            long total = questionSetService.queryChain().where(QUESTION_SET.QUESTION_BANK_ID.eq(id)).count();
            if (total <= answeredIds.size()) {
                answeredIds.clear();
            }

            // 查询所有未答题目的id
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select(QUESTION_SET.ID);
            queryWrapper.and(QUESTION_SET.QUESTION_BANK_ID.eq(id));

            // 使用not exists，排除已答题目
            if (answeredIds.size() > 0) {
                QueryWrapper subQuery = QueryWrapper.create().select(QUESTION_SET.ID).from(QUESTION_ANSWER)
                    .and(QUESTION_SET.ID.in(answeredIds));

                queryWrapper.and(notExists(subQuery));
            }
            List<QuestionSetDomain> unAnswered = questionSetService.list(queryWrapper);

            // 随机抽题。在未答题中，抽取randomNumber个题目
            Integer randomNumber = questionBankDomain.getRandomNumber();

            List<QuestionSetDomain> randomSample = new ArrayList<>(randomNumber);
            Set<Integer> extractedIndices = new HashSet<>(randomNumber);

            // 随机抽取 sampleSize 个元素
            Random random = new Random();
            while (extractedIndices.size() < randomNumber) {
                int randomIndex = random.nextInt(unAnswered.size());
                if (!extractedIndices.contains(randomIndex)) {
                    extractedIndices.add(randomIndex);
                    randomSample.add(unAnswered.get(randomIndex));
                }
            }

            // 查询题目原信息
            return questionSetService.queryChain()
                .select(QUESTION_SET.ID, QUESTION_SET.QUESTION, QUESTION_SET.OPTIONS, QUESTION_SET.PROMPT).where(
                    QUESTION_SET.ID.in(
                        randomSample.stream().map(QuestionSetDomain::getId).collect(Collectors.toList()))).list();
        }

        return null;
    }

    /**
     * 答题
     * <p>
     * TODO：1. 校验间隔，增加失败答题间隔
     *      2. 抽象一层answer_bank。抽题是生成记录，记录答题时间；答题时修改最终结果（对数、错数），判断是否用错数-判断1步骤中的间隔；还可以防止为抽题答题
     *      （暂时单选1道题，没问题）
     *
     * @param deviceId
     * @param questionBandId
     * @param answer
     */
    @Override
    public QuestionAnswerResultVO answerSet(Long questionBandId, Long deviceId, List<QuestionAnswerDomain> answer) {
        Date now = new Date();
        LoginUser loginUser = Context.getLoginUser();

        // 检查答题间隙
        //        QuestionBankDomain questionBankDomain = this.getById(questionBandId);
        //        this.checkFrequencyGap(questionBankDomain);
        //
        QuestionAnswerResultVO result = new QuestionAnswerResultVO();
        //        result.setCoolingTime(System.currentTimeMillis() + questionBankDomain.getFrequencyGap() * 1000);

        // 查询题目答案
        List<Long> answerIds = answer.stream().map(QuestionAnswerDomain::getQuestionSetId).collect(Collectors.toList());
        List<QuestionSetDomain> questionSets =
            questionSetService.queryChain().select(QUESTION_SET.ID, QUESTION_SET.TYPE, QUESTION_SET.ANSWER)
                .where(QUESTION_SET.QUESTION_BANK_ID.eq(questionBandId)).and(QUESTION_SET.ID.in(answerIds)).list();

        if (questionSets.size() != answer.size()) {
            throw new ThrowPrompt("答案不完整");
        }

        Map<Long, QuestionSetDomain> questionSetMap = new HashMap<>(questionSets.size());
        questionSets.forEach(questionSet -> {
            questionSetMap.put(questionSet.getId(), questionSet);
        });

        // 成功id
        List<Long> correctIds = new ArrayList<>();
        // 失败id
        List<Long> incorrectIds = new ArrayList<>();

        answer.forEach(answerDomain -> {
            QuestionSetDomain questionSetDomain = questionSetMap.get(answerDomain.getQuestionSetId());

            List<Integer> answerOption = answerDomain.getAnswerOption();
            String answerText = answerDomain.getAnswerText();

            // 根据类型，校验答案
            if (10 == questionSetDomain.getType()) {
                // 文本回答
                if (VerifyUtils.isEmpty(answerText)) {
                    throw new ThrowPrompt("答案不完整");
                }
                answerDomain.setAnswerOption(null);

                correctIds.add(answerDomain.getQuestionSetId());
            } else if (20 == questionSetDomain.getType()) {
                // 单选
                // TODO: 改为VerifyUtils.isEmpty
                if (answerOption == null || answerOption.size() == 0) {
                    throw new ThrowPrompt("答案不完整");
                } else if (answerOption.size() > 1) {
                    throw new ThrowPrompt("答案只能是单选");
                }
                answerDomain.setAnswerText(null);

                if (answerOption.get(0) == questionSetDomain.getAnswer().get(0)) {
                    correctIds.add(answerDomain.getQuestionSetId());
                } else {
                    incorrectIds.add(answerDomain.getQuestionSetId());
                }
            } else if (21 == questionSetDomain.getType()) {
                // 多选
                if (VerifyUtils.isEmpty(answerOption)) {
                    throw new ThrowPrompt("答案不完整");
                }

                answerDomain.getAnswerOption().sort(Integer::compareTo);
                answerDomain.setAnswerText(null);

                if (answerOption.equals(questionSetDomain.getAnswer())) {
                    correctIds.add(answerDomain.getQuestionSetId());
                } else {
                    incorrectIds.add(answerDomain.getQuestionSetId());
                }
            }

            answerDomain.setQuestionBankId(questionBandId);
            answerDomain.setCreatedAt(now);
            answerDomain.setCreatorId(loginUser.getId());
        });

        questionAnswerService.saveBatch(answer);

        result.setCorrectIds(correctIds);
        result.setIncorrectIds(incorrectIds);

        // 全部成功，创建任务
        // TODO 失败不创建
        if (incorrectIds.size() == 0 || correctIds.size() == 0) {
            DeviceTaskVO deviceTaskVO = new DeviceTaskVO();
            deviceTaskVO.setDeviceId(deviceId);
            deviceTaskVO.setCreatedAt(now);
            deviceTaskVO.setCreatorId(loginUser.getId());
            deviceTaskVO.setRunningStatus(0);
            deviceTaskVO.setMessage("");

            DeviceDomain deviceDomain = deviceService.find(deviceId);
            if (deviceDomain != null && deviceDomain.getConfig() == null) {
                deviceTaskVO.setRunningStatus(10);
                deviceTaskVO.setMessage("AABBCC" + deviceDomain.getSn() + "030301FF");
            }

            deviceTaskService.create(deviceTaskVO);

            result.setDeviceTaskId(deviceTaskVO.getId());
        }

        return result;
    }

    /**
     * 校验答题间隙
     *
     * @param questionBankDomain
     */
    private void checkFrequencyGap(QuestionBankDomain questionBankDomain) {
        LoginUser loginUser = Context.getLoginUser();

        if (questionBankDomain.getFrequencyType() != 21) {
            // 最后一次答题时间
            QuestionAnswerDomain lastAnswer = questionAnswerService.queryChain().select(QUESTION_ANSWER.CREATED_AT)
                .where(QUESTION_ANSWER.QUESTION_BANK_ID.eq(questionBankDomain.getId()))
                .and(QUESTION_ANSWER.CREATOR_ID.eq(loginUser.getId())).orderBy(QUESTION_ANSWER.ID.desc()).limit(1)
                .one();

            if (lastAnswer == null) {
                return;
            }

            // 只能答题一次
            if (questionBankDomain.getFrequencyType() == 10 && lastAnswer != null) {
                throw new ThrowPrompt("已填写");
            }

            // 不限次数-有间隔。根据最后一次答题时间，判断间隔
            Long endCoolingTime = lastAnswer.getCreatedAt().getTime() + questionBankDomain.getFrequencyGap() * 1000;

            if (System.currentTimeMillis() < endCoolingTime) {
                //                throw new ThrowPrompt("冷却中", endCoolingTime);
            }
        }
    }
}
