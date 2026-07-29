package com.example.tasks.service;

import com.example.tasks.domain.Role;
import com.example.tasks.repository.RoleRepository;
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
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getAllRoles()
    {
        return roleRepository.findAll();
    }



}
