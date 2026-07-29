package com.example.tasks.repository;


import com.example.tasks.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {


    @Query(value = "select p.permission_name from rolespermissions rp join permissions p on p.permission_id = rp.permission_id WHERE rp.role_id = ?1", nativeQuery = true)
    List<String> findPermissionByRoleId(Long roleId);


}
