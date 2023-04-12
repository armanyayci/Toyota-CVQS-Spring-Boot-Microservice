package toyotabackend.toyotabackend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import toyotabackend.toyotabackend.dao.TerminalCategoryRepository;

import toyotabackend.toyotabackend.domain.Terminal.TerminalCategory;
import toyotabackend.toyotabackend.dto.response.TerminalViewResponse;
import toyotabackend.toyotabackend.service.Abstract.TerminalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {

    private static final Logger logger = Logger.getLogger(TerminalServiceImpl.class);

    private final TerminalCategoryRepository terminalCategoryRepository;

    @Override
    public List<TerminalViewResponse> getTerminals(int page, int size, String sortBy, String filterCategory) {

        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));

        if (filterCategory.isEmpty()){
            logger.debug("filter is null");
            Page<TerminalCategory> categories = terminalCategoryRepository.findAll(paging);
            return categories.stream().map(TerminalViewResponse::Convert).collect(Collectors.toList());}

        return terminalCategoryRepository.findAllByname(filterCategory,paging)
                .stream().map(TerminalViewResponse::Convert).collect(Collectors.toList());}

}





















