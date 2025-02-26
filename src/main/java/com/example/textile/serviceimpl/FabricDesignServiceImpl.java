package com.example.textile.serviceimpl;

import com.example.textile.entity.FabricDesign;
import com.example.textile.repo.FabricDesignRepo;
import com.example.textile.service.FabricDesignService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FabricDesignServiceImpl implements FabricDesignService {
    FabricDesignRepo designRepo;

    @Override
    public FabricDesign findById(Long id) {
        return designRepo.findById(id).orElse(null);
    }

    @Override
    public FabricDesign saveOrUpdate(FabricDesign fabricDesign) {
        return designRepo.save(fabricDesign);
    }

    @Override
    public void deleteById(Long id) {
        designRepo.deleteById(id);
    }

    @Override
    public List<FabricDesign> findAll() {
        return designRepo.findAll();
    }

    @Override
    public List<FabricDesign> findByNameLike(String name) {
        return designRepo.findByNameLikeIgnoreCase(name);
    }
}
