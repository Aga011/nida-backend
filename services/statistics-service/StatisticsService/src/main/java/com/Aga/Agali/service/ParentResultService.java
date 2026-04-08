package com.Aga.Agali.service;

import com.Aga.Agali.dto.ParentResultDto;
import com.Aga.Agali.dto.TestResultDto;
import com.Aga.Agali.entity.ParentResult;
import com.Aga.Agali.repository.ParentResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ParentResultService {

    private final ParentResultRepository parentResultRepository;

    public void createParentResult(TestResultDto dto) {
        int delayHours = 48 + new Random().nextInt(25);

        String analysis = generateAnalysis(dto);

        ParentResult result = ParentResult.builder()
                .studentEmail(dto.getUserEmail())
                .sessionId(dto.getSessionId())
                .percentage(dto.getPercentage())
                .analysis(analysis)
                .visible(false)
                .visibleAt(LocalDateTime.now().plusHours(delayHours))
                .build();

        parentResultRepository.save(result);
    }

    @Scheduled(fixedRate = 3600000)
    public void makeResultsVisible() {
        List<ParentResult> due = parentResultRepository
                .findByVisibleFalseAndVisibleAtBefore(LocalDateTime.now());

        due.forEach(r -> {
            r.setVisible(true);
            parentResultRepository.save(r);
        });
    }

    public List<ParentResultDto> getChildResults(String studentEmail) {
        return parentResultRepository
                .findByStudentEmailAndVisibleTrue(studentEmail)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private String generateAnalysis(TestResultDto dto) {
        double percentage = dto.getPercentage();
        String trend = percentage >= 70 ? "yaxşı" :
                percentage >= 40 ? "orta" : "zəif";

        StringBuilder sb = new StringBuilder();
        sb.append("Ümumi nəticə: ").append(String.format("%.1f", percentage))
                .append("% — ").append(trend).append(" səviyyə. ");

        if (dto.getSubjectResults() != null) {
            dto.getSubjectResults().forEach((subject, correct) -> {
                double subjectRate = dto.getTotalQuestions() > 0
                        ? (double) correct / dto.getTotalQuestions() * 100 : 0;
                if (subjectRate < 40) {
                    sb.append(subject).append(" fənnində diqqət lazımdır. ");
                } else if (subjectRate >= 70) {
                    sb.append(subject).append(" fənnində güclüdür. ");
                }
            });
        }

        return sb.toString();
    }

    private ParentResultDto toDto(ParentResult result) {
        return ParentResultDto.builder()
                .id(result.getId())
                .studentEmail(result.getStudentEmail())
                .percentage(result.getPercentage())
                .analysis(result.getAnalysis())
                .createdAt(result.getCreatedAt())
                .visibleAt(result.getVisibleAt())
                .build();
    }
}