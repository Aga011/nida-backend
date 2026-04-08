package com.Aga.Agali.service;

import com.Aga.Agali.dto.ShuffledQuestionDto;
import com.Aga.Agali.entity.Question;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShuffleService {

    public ShuffledQuestionDto shuffle(Question question) {
        Map<String, String> original = new LinkedHashMap<>();
        original.put("A", question.getOptionA());
        original.put("B", question.getOptionB());
        original.put("C", question.getOptionC());
        original.put("D", question.getOptionD());

        List<Map.Entry<String, String>> entries = new ArrayList<>(original.entrySet());
        Collections.shuffle(entries);

        String[] keys = {"A", "B", "C", "D"};
        Map<String, String> shuffled = new LinkedHashMap<>();
        String newCorrectAnswer = "";
        StringBuilder shuffleMap = new StringBuilder();

        for (int i = 0; i < keys.length; i++) {
            String newKey = keys[i];
            String oldKey = entries.get(i).getKey();
            String value = entries.get(i).getValue();
            shuffled.put(newKey, value);
            shuffleMap.append(oldKey).append("->").append(newKey).append(";");

            if (oldKey.equals(question.getCorrectAnswer())) {
                newCorrectAnswer = newKey;
            }
        }

        return ShuffledQuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .subject(question.getSubject().name())
                .gradeLevel(question.getGradeLevel().name())
                .optionA(shuffled.get("A"))
                .optionB(shuffled.get("B"))
                .optionC(shuffled.get("C"))
                .optionD(shuffled.get("D"))
                .correctAnswer(newCorrectAnswer)
                .shuffleMap(shuffleMap.toString())
                .build();
    }
}