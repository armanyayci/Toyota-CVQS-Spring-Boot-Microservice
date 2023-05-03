package com.toyota.backend.service.Abstract;

import com.toyota.backend.dto.response.TerminalViewResponse;

import java.util.List;

public interface TerminalService {

    List<TerminalViewResponse> getTerminals(int page, int size, String sortBy, String filterCategory);

}
