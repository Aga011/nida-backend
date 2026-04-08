package com.Aga.Agali.service;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.mapper.TestMapper;
import com.Aga.Agali.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final QuestionRepository questionRepository;
    private final QuestionAttemptRepository attemptRepository;
    private final TestSessionRepository sessionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TestMapper mapper;
    private final ShuffleService shuffleService;
    private final SpacedRepetitionService spacedRepetitionService;

    private static final String TEST_RESULT_TOPIC = "test-results";

    public TestSession startAssessment(String userEmail, String gradeLevel) {
        Optional<TestSession> existing = sessionRepository
                .findByUserEmailAndSessionTypeAndCompleted(
                        userEmail, SessionType.ASSESSMENT, false);
        if (existing.isPresent()) {
            return existing.get();
        }

        TestSession session = TestSession.builder()
                .userEmail(userEmail)
                .gradeLevel(GradeLevel.valueOf(gradeLevel))
                .sessionType(SessionType.ASSESSMENT)
                .totalQuestions(0)
                .correctAnswers(0)
                .lastQuestionIndex(0)
                .totalTimeSeconds(0L)
                .completed(false)
                .build();

        return sessionRepository.save(session);
    }

    public TestSession startTest(String userEmail,
                                 GradeLevel gradeLevel,
                                 SpecialtyGroup specialtyGroup) {
        Optional<TestSession> existing = sessionRepository
                .findByUserEmailAndGradeLevelAndSpecialtyGroupAndCompleted(
                        userEmail, gradeLevel, specialtyGroup, false);
        if (existing.isPresent()) {
            return existing.get();
        }

        TestSession session = TestSession.builder()
                .userEmail(userEmail)
                .gradeLevel(gradeLevel)
                .specialtyGroup(specialtyGroup)
                .sessionType(SessionType.TOPIC)
                .totalQuestions(0)
                .correctAnswers(0)
                .lastQuestionIndex(0)
                .totalTimeSeconds(0L)
                .completed(false)
                .build();

        return sessionRepository.save(session);
    }

    public List<ShuffledQuestionDto> getQuestions(String userEmail,
                                                  GradeLevel gradeLevel,
                                                  SpecialtyGroup specialtyGroup,
                                                  Long sessionId) {
        TestSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session tapılmadı"));

        List<Subject> subjects = getSubjectsForGroup(specialtyGroup);
        List<Question> allQuestions = new ArrayList<>();

        for (Subject subject : subjects) {
            allQuestions.addAll(
                    questionRepository.findBySubjectAndGradeLevel(subject, gradeLevel));
        }

        List<Question> remaining = allQuestions.stream()
                .skip(session.getLastQuestionIndex())
                .collect(Collectors.toList());

        return remaining.stream()
                .map(shuffleService::shuffle)
                .collect(Collectors.toList());
    }

    public List<ShuffledQuestionDto> getAssessmentQuestions(Long sessionId) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session tapılmadı"));

        List<Question> questions = questionRepository.findByInAssessmentTrue();

        return questions.stream()
                .map(shuffleService::shuffle)
                .collect(Collectors.toList());
    }

    public List<ShuffledQuestionDto> getSpacedQuestions(String userEmail) {
        List<QuestionAttempt> due =
                spacedRepetitionService.getDueForReview(userEmail);

        return due.stream()
                .map(attempt -> questionRepository.findById(attempt.getQuestionId())
                        .map(shuffleService::shuffle)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public QuestionAttempt submitAnswer(String userEmail,
                                        Long sessionId,
                                        SubmitAnswerRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Sual tapılmadı"));

        boolean correct = !request.isSkipped() &&
                question.getCorrectAnswer()
                        .equalsIgnoreCase(request.getSelectedAnswer());

        QuestionAttempt attempt = attemptRepository
                .findByUserEmailAndQuestionId(userEmail, question.getId())
                .orElse(QuestionAttempt.builder()
                        .userEmail(userEmail)
                        .questionId(question.getId())
                        .sessionId(sessionId)
                        .attemptCount(0)
                        .build());

        attempt.setSelectedAnswer(request.getSelectedAnswer());
        attempt.setCorrect(correct);
        attempt.setSkipped(request.isSkipped());
        attempt.setTimeSpentSeconds(request.getTimeSpentSeconds());

        spacedRepetitionService.updateReviewDate(attempt, correct);

        attemptRepository.save(attempt);

        TestSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session tapılmadı"));
        session.setTotalQuestions(session.getTotalQuestions() + 1);
        session.setLastQuestionIndex(session.getLastQuestionIndex() + 1);
        session.setTotalTimeSeconds(
                session.getTotalTimeSeconds() + request.getTimeSpentSeconds());
        if (correct) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        }
        sessionRepository.save(session);

        return attempt;
    }

    public TestResultDto finishTest(String userEmail, Long sessionId) {
        TestSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session tapılmadı"));

        session.setCompleted(true);
        session.setCompletedAt(LocalDateTime.now());
        sessionRepository.save(session);

        Map<String, Integer> subjectResults = calculateSubjectResults(sessionId);
        TestResultDto result = mapper.toTestResultDto(session, subjectResults);

        kafkaTemplate.send(TEST_RESULT_TOPIC, userEmail, result);

        return result;
    }

    private Map<String, Integer> calculateSubjectResults(Long sessionId) {
        List<QuestionAttempt> attempts =
                attemptRepository.findByUserEmailAndSessionId(null, sessionId);
        Map<String, Integer> results = new HashMap<>();

        for (QuestionAttempt attempt : attempts) {
            questionRepository.findById(attempt.getQuestionId()).ifPresent(q -> {
                String subject = q.getSubject().name();
                results.merge(subject, attempt.isCorrect() ? 1 : 0, Integer::sum);
            });
        }
        return results;
    }

    private List<Subject> getSubjectsForGroup(SpecialtyGroup group) {
        return switch (group) {
            case GROUP_1 -> List.of(Subject.KIMYA, Subject.RIYAZIYYAT,
                    Subject.FIZIKA, Subject.AZERBAYCAN_DILI, Subject.XARICI_DIL);
            case GROUP_2 -> List.of(Subject.COGRAFIYA, Subject.TARIX,
                    Subject.RIYAZIYYAT, Subject.AZERBAYCAN_DILI, Subject.XARICI_DIL);
            case GROUP_3 -> List.of(Subject.INGILIS_DILI, Subject.EDEBIYYAT,
                    Subject.TARIX, Subject.AZERBAYCAN_DILI, Subject.RIYAZIYYAT);
            case GROUP_4 -> List.of(Subject.KIMYA, Subject.FIZIKA,
                    Subject.BIOLOGIYA, Subject.AZERBAYCAN_DILI, Subject.XARICI_DIL);
            case GROUP_5 -> List.of(Subject.AZERBAYCAN_DILI,
                    Subject.RIYAZIYYAT, Subject.XARICI_DIL);
        };
    }
}