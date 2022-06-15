package ru.yandex.practicum.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.IncorrectCountException;

import java.util.Map;

@RestController
@RequestMapping("/cats")
public class CatsInteractionController {
    private int happiness = 0;

    @GetMapping("/converse")
    public Map<String, String> converse() {
        happiness++;
        return Map.of("talk", "Мяу");
    }

    @GetMapping("/pet")
    public Map<String, String> pet(@RequestParam(required = false) final Integer count) {
        if (count == null) {
            throw new IncorrectCountException("Параметр count равен null.");
        }
        if (count <= 0) {
            throw new IncorrectCountException("Параметр count имеет отрицательное значение.");
        }

        happiness += count;
        return Map.of("talk", "Муррр. ".repeat(count));
    }

    @GetMapping("/happiness")
    public Map<String, Integer> happiness() {
        return Map.of("happiness", happiness);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHappinessOverflow(final IncorrectCountException err) {
        return new ErrorResponse(
                "Ошибка с параметром count.", err.getMessage()
        );
    }

    public class ErrorResponse {
        // название ошибки
        String error;
        // подробное описание
        String description;

        public ErrorResponse(String error, String description) {
            this.error = error;
            this.description = description;
        }

        // геттеры необходимы, чтобы Spring Boot мог получить значения полей
        public String getError() {
            return error;
        }

        public String getDescription() {
            return description;
        }
    }
}