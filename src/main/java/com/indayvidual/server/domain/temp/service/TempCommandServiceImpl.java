package com.indayvidual.server.domain.temp.service;

import com.indayvidual.server.domain.temp.converter.TempConverter;
import com.indayvidual.server.domain.temp.dto.TempRequestDTO;
import com.indayvidual.server.domain.temp.entity.Temp;
import com.indayvidual.server.domain.temp.repository.TempRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.handler.TempHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TempCommandServiceImpl implements TempCommandService {

    private final TempRepository tempRepository;

    @Override
    public Long createTemp(TempRequestDTO.CreateTempDTO request) {
        Temp temp = TempConverter.toTemp(request);
        Temp savedTemp = tempRepository.save(temp);
        return savedTemp.getId();
    }

    @Override
    public void updateTemp(Long tempId, TempRequestDTO.UpdateTempDTO request) {
        Temp temp = tempRepository.findById(tempId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TEMP_NOT_FOUND));

        temp.updateDescription(request.getDescription());
        temp.updateStatus(request.getStatus());
    }

    @Override
    public void deleteTemp(Long tempId) {
        Temp temp = tempRepository.findById(tempId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TEMP_NOT_FOUND));

        tempRepository.delete(temp);
    }
}
