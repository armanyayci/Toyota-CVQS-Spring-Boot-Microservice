package com.toyota.backend.service.Concrete;

import com.toyota.backend.dao.RoleRepository;
import com.toyota.backend.dao.UserRepository;
import com.toyota.backend.service.Abstract.AdminService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.toyota.backend.dao.TerminalCategoryRepository;
import com.toyota.backend.domain.Terminal.TerminalCategory;
import com.toyota.backend.dto.response.TerminalViewResponse;
import com.toyota.backend.service.Abstract.TerminalService;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class implements the {@link TerminalService} interface and provides functionality for retrieving TerminalViewResponses.
 * It retrieves TerminalCategories and TerminalViewResponses from the TerminalCategoryRepository, with optional filtering
 * by category name.
 * The class uses constructor injection to inject instances of {@link TerminalCategoryRepository}
 * The class also logs information and error messages using {@link Logger} to facilitate debugging and error handling.
 */
@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {
    private static final Logger logger = Logger.getLogger(TerminalServiceImpl.class);
    private final TerminalCategoryRepository terminalCategoryRepository;
    /**
     * Retrieves a list of TerminalViewResponses based on given parameters.
     * @param page the page number to retrieve
     * @param size the number of items per page
     * @param sortBy the field to sort by
     * @param filterCategory the name of the category to filter by (optional)
     * @return a list of TerminalViewResponses
     */
    @Override
    public List<TerminalViewResponse> getTerminals(int page, int size, String sortBy, String filterCategory) {

        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));

        if (filterCategory.isEmpty()){
            logger.debug("filter is null");
            Page<TerminalCategory> categories = terminalCategoryRepository.findAll(paging);
            return categories.stream().map(TerminalViewResponse::Convert).collect(Collectors.toList());
        }
        return terminalCategoryRepository.findAllByname(filterCategory,paging)
                .stream().map(TerminalViewResponse::Convert).collect(Collectors.toList());}
}