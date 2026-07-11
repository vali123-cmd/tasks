package com.example.tasks.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import com.example.tasks.domain.StatusType;
import com.example.tasks.dto.StatusTypeDTO;
import com.example.tasks.repository.StatusTypeRepository;
import com.example.tasks.mapper.StatusTypeMapper;



@Slf4j
@Service
@RequiredArgsConstructor
public class StatusTypeService {
    private final StatusTypeRepository statusTypeRepository;
    private final StatusTypeMapper statusMapper;

    public List<StatusTypeDTO> getAllStatuses()
    {
        log.info("Statuses retrieved");
        return statusTypeRepository.findAll().stream().map(statusMapper::toDto).toList();
    }

    @Transactional
    public StatusTypeDTO createStatus(@Valid StatusTypeDTO statusTypeDTO)
    {
        log.info("Status created: {}", statusTypeDTO);
        StatusType status = statusMapper.toEntity(statusTypeDTO);
        StatusType savedStatus = statusTypeRepository.save(status);
        return statusMapper.toDto(savedStatus);
    }


}
