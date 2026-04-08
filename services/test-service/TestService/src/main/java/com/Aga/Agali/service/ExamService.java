package com.Aga.Agali.service;

import com.Aga.Agali.client.PaymentServiceClient;
import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamSessionRepository examSessionRepository;
    private final ExamCommentRepository examCommentRepository;
    private final QuestionRepository questionRepository;
    private final PaymentServiceClient paymentServiceClient;


    public ExamResponse createExam(String teacherEmail, ExamRequest request) {
        Exam exam = Exam.builder()
                .teacherEmail(teacherEmail)
                .title(request.getTitle())
                .description(request.getDescription())
                .subject(Subject.valueOf(request.getSubject()))
                .gradeLevel(GradeLevel.valueOf(request.getGradeLevel()))
                .status(ExamStatus.DRAFT)
                .durationMinutes(request.getDurationMinutes())
                .paid(false)
                .groupId(request.getGroupId())
                .build();

        examRepository.save(exam);

        List<Question> questions = questionRepository
                .findBySubjectAndGradeLevel(
                        Subject.valueOf(request.getSubject()),
                        GradeLevel.valueOf(request.getGradeLevel()));

        int index = 0;
        for (Question question : questions) {
            ExamQuestion eq = ExamQuestion.builder()
                    .examId(exam.getId())
                    .questionId(question.getId())
                    .orderIndex(index++)
                    .build();
            examQuestionRepository.save(eq);
        }

        return toExamResponse(exam, questions.size());
    }


    public ExamResponse activateExam(Long examId, String teacherEmail) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("İmtahan tapılmadı"));

        if (!exam.getTeacherEmail().equals(teacherEmail)) {
            throw new RuntimeException("Bu imtahan sizə aid deyil");
        }

        if (!exam.isPaid()) {
            throw new RuntimeException(
                    "İmtahanı aktivləşdirmək üçün ödəniş etməlisiniz");
        }

        exam.setStatus(ExamStatus.ACTIVE);
        exam.setStartTime(LocalDateTime.now());
        exam.setEndTime(LocalDateTime.now()
                .plusMinutes(exam.getDurationMinutes()));

        return toExamResponse(examRepository.save(exam),
                examQuestionRepository.countByExamId(examId));
    }

    public ExamResponse payForExam(Long examId, String teacherEmail) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("İmtahan tapılmadı"));

        if (!exam.getTeacherEmail().equals(teacherEmail)) {
            throw new RuntimeException("Bu imtahan sizə aid deyil");
        }

        if (exam.isPaid()) {
            throw new RuntimeException("Bu imtahan artıq ödənilib");
        }


        boolean success = paymentServiceClient.payForExam(teacherEmail, 4.99);
        if (!success) {
            throw new RuntimeException(
                    "Ödəniş uğursuz oldu — balansınızı yoxlayın");
        }

        exam.setPaid(true);
        return toExamResponse(examRepository.save(exam),
                examQuestionRepository.countByExamId(examId));
    }


    public ExamSessionResponse startExam(Long examId, String studentEmail) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("İmtahan tapılmadı"));

        if (exam.getStatus() != ExamStatus.ACTIVE) {
            throw new RuntimeException("İmtahan aktiv deyil");
        }

        examSessionRepository.findByExamIdAndStudentEmail(examId, studentEmail)
                .ifPresent(s -> {
                    throw new RuntimeException("Siz artıq bu imtahana başlamısınız");
                });

        int questionCount = examQuestionRepository.countByExamId(examId);

        ExamSession session = ExamSession.builder()
                .examId(examId)
                .studentEmail(studentEmail)
                .totalQuestions(questionCount)
                .correctAnswers(0)
                .percentage(0.0)
                .completed(false)
                .build();

        return toSessionResponse(examSessionRepository.save(session), null);
    }

    public ExamSessionResponse finishExam(Long sessionId, String studentEmail) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session tapılmadı"));

        if (!session.getStudentEmail().equals(studentEmail)) {
            throw new RuntimeException("Bu session sizə aid deyil");
        }

        double percentage = session.getTotalQuestions() > 0
                ? (double) session.getCorrectAnswers()
                / session.getTotalQuestions() * 100
                : 0;

        session.setPercentage(percentage);
        session.setCompleted(true);
        session.setCompletedAt(LocalDateTime.now());

        return toSessionResponse(examSessionRepository.save(session), null);
    }

    public ExamSessionResponse addComment(Long sessionId,
                                          String teacherEmail,
                                          ExamCommentRequest request) {
        ExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session tapılmadı"));

        if (!session.isCompleted()) {
            throw new RuntimeException("İmtahan hələ bitməyib");
        }

        if (session.getCompletedAt() != null &&
                session.getCompletedAt().plusHours(24)
                        .isBefore(LocalDateTime.now())) {
            throw new RuntimeException(
                    "Şərh əlavə etmə müddəti bitib — 24 saat");
        }

        examCommentRepository.findByExamSessionId(sessionId)
                .ifPresent(c -> {
                    throw new RuntimeException("Bu sessiya üçün artıq şərh əlavə edilib");
                });

        ExamComment comment = ExamComment.builder()
                .examSessionId(sessionId)
                .teacherEmail(teacherEmail)
                .comment(request.getComment())
                .build();

        examCommentRepository.save(comment);

        return toSessionResponse(session, comment.getComment());
    }

    public List<ExamResponse> getTeacherExams(String teacherEmail) {
        return examRepository.findByTeacherEmail(teacherEmail)
                .stream()
                .map(e -> toExamResponse(e,
                        examQuestionRepository.countByExamId(e.getId())))
                .toList();
    }


    public List<ExamResponse> getGroupExams(Long groupId) {
        return examRepository.findByGroupId(groupId)
                .stream()
                .map(e -> toExamResponse(e,
                        examQuestionRepository.countByExamId(e.getId())))
                .toList();
    }


    public List<ExamSessionResponse> getExamResults(Long examId,
                                                    String teacherEmail) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("İmtahan tapılmadı"));

        if (!exam.getTeacherEmail().equals(teacherEmail)) {
            throw new RuntimeException("Bu imtahan sizə aid deyil");
        }

        return examSessionRepository.findByExamId(examId)
                .stream()
                .map(s -> {
                    String comment = examCommentRepository
                            .findByExamSessionId(s.getId())
                            .map(ExamComment::getComment)
                            .orElse(null);
                    return toSessionResponse(s, comment);
                })
                .toList();
    }

    public ExamSessionResponse getStudentResult(Long examId,
                                                String studentEmail) {
        ExamSession session = examSessionRepository
                .findByExamIdAndStudentEmail(examId, studentEmail)
                .orElseThrow(() -> new RuntimeException("Nəticə tapılmadı"));

        String comment = examCommentRepository
                .findByExamSessionId(session.getId())
                .map(ExamComment::getComment)
                .orElse(null);

        return toSessionResponse(session, comment);
    }

    private ExamResponse toExamResponse(Exam exam, int questionCount) {
        return ExamResponse.builder()
                .id(exam.getId())
                .teacherEmail(exam.getTeacherEmail())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .subject(exam.getSubject().name())
                .gradeLevel(exam.getGradeLevel().name())
                .status(exam.getStatus().name())
                .durationMinutes(exam.getDurationMinutes())
                .paid(exam.isPaid())
                .groupId(exam.getGroupId())
                .questionCount(questionCount)
                .createdAt(exam.getCreatedAt())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .build();
    }

    private ExamSessionResponse toSessionResponse(ExamSession session,
                                                  String comment) {
        return ExamSessionResponse.builder()
                .id(session.getId())
                .examId(session.getExamId())
                .studentEmail(session.getStudentEmail())
                .totalQuestions(session.getTotalQuestions())
                .correctAnswers(session.getCorrectAnswers())
                .percentage(session.getPercentage())
                .completed(session.isCompleted())
                .startedAt(session.getStartedAt())
                .completedAt(session.getCompletedAt())
                .comment(comment)
                .build();
    }
}