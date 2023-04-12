package toyotabackend.toyotabackend.service.Abstract;

import toyotabackend.toyotabackend.domain.Terminal.TerminalCategory;
import toyotabackend.toyotabackend.dto.response.TerminalViewResponse;

import java.io.IOException;
import java.util.List;

public interface TerminalService {

    List<TerminalViewResponse> getTerminals(int page, int size, String sortBy, String filterCategory);

}
