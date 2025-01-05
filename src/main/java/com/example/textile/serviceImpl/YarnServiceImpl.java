package com.example.textile.serviceImpl;

import com.example.textile.dto.YarnDto;
import com.example.textile.entity.Yarn;
import com.example.textile.repo.YarnRepository;
import com.example.textile.service.YarnService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class YarnServiceImpl implements YarnService {

    private YarnRepository yarnRepo;
    private ModelMapper modelMapper;

    @Override
    public List<YarnDto> findAll() {
        return yarnRepo.findAll()
                .stream().map(yarn -> modelMapper.map(yarn, YarnDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public YarnDto findById(Long id) {
        Yarn persistedYarn = yarnRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Yarn not found [id= " + id + "]"));
        return modelMapper.map(persistedYarn, YarnDto.class);
    }

    @Override
    public YarnDto save(YarnDto yarnDto) {
        Yarn yarn = modelMapper.map(yarnDto, Yarn.class);
        Yarn saved = yarnRepo.save(yarn);
        return modelMapper.map(saved, YarnDto.class);
    }

    @Override
    public YarnDto updateYarn(Long id, YarnDto yarnDto) {
        Yarn persistedYarn = yarnRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Yarn not found [id= " + id + "]"));

        yarnDto.setId(id);
        modelMapper.map(yarnDto, persistedYarn);
        persistedYarn = yarnRepo.save(persistedYarn);
        return modelMapper.map(persistedYarn, YarnDto.class);
    }

    @Override
    public void deleteYarn(Long id) {
        yarnRepo.deleteById(id);
    }

    @Override
    public YarnDto findByType(String type) {
        Yarn byType = yarnRepo.findByType(type);
        if (!Objects.nonNull(byType)) {
            return null;
        }
        return modelMapper.map(byType, YarnDto.class);
    }
}
