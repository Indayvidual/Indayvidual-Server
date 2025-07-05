package com.indayvidual.server.domain.temp.service;

import com.indayvidual.server.domain.temp.dto.TempRequestDTO;

public interface TempCommandService {
    Long createTemp(TempRequestDTO.CreateTempDTO request);
    void updateTemp(Long tempId, TempRequestDTO.UpdateTempDTO request);
    void deleteTemp(Long tempId);
}
