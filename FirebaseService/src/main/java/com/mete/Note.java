package com.mete;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class Note {

    private String subject;
    private String content;
    private Map<String, String> data;

}
