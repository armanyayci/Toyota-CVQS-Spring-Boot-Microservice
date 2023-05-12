package com.toyota.backend.service.Concrete;

import com.toyota.backend.TestUtils;
import com.toyota.backend.dao.TerminalCategoryRepository;
import com.toyota.backend.domain.Terminal.TerminalCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link TerminalServiceImpl} class.
 */
@SpringBootTest
class TerminalServiceImplTest extends TestUtils {

    private TerminalCategoryRepository terminalCategoryRepository;
    private TerminalServiceImpl terminalService;


    @BeforeEach
    void setUp() {
        terminalCategoryRepository = mock(TerminalCategoryRepository.class);
        terminalService = new TerminalServiceImpl(terminalCategoryRepository);
    }

    @Test
    void getTerminals_whenCalledWithOptionalParametersAndFilterCategoryName_itShouldReturnListOfTerminalViewResponse() {

        int page = 0;
        int size = 10;
        String sortBy = "id";
        String filterCategoryName = "testName";
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        List<TerminalCategory> categories = generateListOfTerminalCategory();
        Page<TerminalCategory> pages = new PageImpl<>(categories);

        when(terminalCategoryRepository.findAllByname(filterCategoryName,paging)).thenReturn(pages);
        var result = terminalService.getTerminals(page,size,sortBy,filterCategoryName);
        assertEquals(result.size(),categories.size());
        assertEquals(result.get(0).getName(),filterCategoryName);
        assertEquals(result.get(1).getName(),filterCategoryName);
    }

    @Test
    void getTerminals_whenCalledWithOptionalParametersAndNullFilterCategoryName_itShouldReturnAllListOfTerminalViewResponse() {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String filterCategoryName = "";
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        List<TerminalCategory> categories = generateListOfTerminalCategory();
        Page<TerminalCategory> pages = new PageImpl<>(categories);

        when(terminalCategoryRepository.findAll(paging)).thenReturn(pages);
        var result = terminalService.getTerminals(page,size,sortBy,filterCategoryName);
        assertEquals(result.size(),categories.size());

    }

}