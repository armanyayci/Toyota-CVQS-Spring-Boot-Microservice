package toyotabackend.toyotabackend.service.Concrete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import toyotabackend.toyotabackend.TestUtils;
import toyotabackend.toyotabackend.dao.TerminalCategoryRepository;
import toyotabackend.toyotabackend.domain.Terminal.TerminalCategory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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


















