package com.Aga.Agali.controller;

import com.Aga.Agali.dto.ParentResultDto;
import com.Aga.Agali.dto.StatisticsResponse;
import com.Aga.Agali.service.ParentResultService;
import com.Aga.Agali.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ParentResultService parentResultService;

    @GetMapping("/me")
    public ResponseEntity<StatisticsResponse> getMyStatistics(
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(statisticsService.getStatistics(userEmail));
    }
    @GetMapping("/parent/child/{studentEmail}")
    public ResponseEntity<List<ParentResultDto>> getChildResults(
            @RequestHeader("X-User-Role") String role,
            @PathVariable String studentEmail) {
        if (!role.equals("PARENT")) {
            throw new RuntimeException("Yalnız valideynlər baxa bilər");
        }
        return ResponseEntity.ok(
                parentResultService.getChildResults(studentEmail));
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<StatisticsResponse> getStudentStatistics(
            @RequestHeader("X-User-Role") String role,
            @PathVariable String email) {
        if (!role.equals("TEACHER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Bu əməliyyat üçün icazəniz yoxdur");
        }
        return ResponseEntity.ok(statisticsService.getStatistics(email));
    }

    @GetMapping("/child/{email}")
    public ResponseEntity<StatisticsResponse> getChildStatistics(
            @RequestHeader("X-User-Role") String role,
            @PathVariable String email) {
        if (!role.equals("PARENT")) {
            throw new RuntimeException("Bu əməliyyat üçün icazəniz yoxdur");
        }
        return ResponseEntity.ok(statisticsService.getStatistics(email));
    }
}