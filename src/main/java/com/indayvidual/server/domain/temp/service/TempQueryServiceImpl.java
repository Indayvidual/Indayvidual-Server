package com.indayvidual.server.domain.temp.service;

import com.indayvidual.server.domain.temp.entity.Temp;
import com.indayvidual.server.domain.temp.entity.enums.TempStatus;
import com.indayvidual.server.domain.temp.repository.TempRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.handler.TempHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {

    private final TempRepository tempRepository;

    @Override
    public Temp findTempById(Long tempId) {
        return tempRepository.findById(tempId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TEMP_NOT_FOUND));
    }

    @Override
    public List<Temp> findTempsByStatus(TempStatus status) {
        return tempRepository.findByStatus(status);
    }

    @Override
    public List<Temp> findAllTemps() {
        return tempRepository.findAll();
    }

    @Override
    public List<Temp> findTempsByName(String name) {
        return tempRepository.findByNameContaining(name);
    }
}
