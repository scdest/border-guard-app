package com.gov.border_guard.api;

import com.gov.border_guard.dtos.BorderRulesDto;
import com.gov.border_guard.mapper.DtoToEntityMapper;
import com.gov.border_guard.mapper.EntityToDtoMapper;
import com.gov.border_guard.services.BorderRulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BorderRulesController {

    private final DtoToEntityMapper dtoToEntityMapper;
    private final EntityToDtoMapper entityToDtoMapper;
    private final BorderRulesService borderRulesService;

    @PostMapping("/border-rules")
    public ResponseEntity<?> createBorderRules(@RequestBody BorderRulesDto rules) {
        borderRulesService.saveBorderRules(dtoToEntityMapper.toEntity(rules));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/border-rules")
    public ResponseEntity<BorderRulesDto> createBorderRules() {
        return ResponseEntity.ok(entityToDtoMapper.toDto(borderRulesService.getBorderRules()));
    }
}
