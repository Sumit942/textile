package com.example.textile.repo;

import com.example.textile.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineRepo extends JpaRepository<Machine, Long> {
    List<Machine> findByMachineNo(String machineNo);
}
