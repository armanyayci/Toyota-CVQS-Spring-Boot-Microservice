package com.toyota.backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.toyota.backend.domain.Terminal.Terminal;
import com.toyota.backend.domain.Terminal.TerminalCategory;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Response Object for Terminal View.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalViewResponse {
    /**
     * The name of the terminal category.
     */
    private String name;
    /**
     * The list of the terminals.
     */
    private List<String> terminals;

    /**
     * Converts a TerminalCategory object to a TerminalViewResponse object.
     * @param terminalCategory the TerminalCategory object to be converted
     * @return the converted TerminalViewResponse object
     */
    public static TerminalViewResponse Convert(TerminalCategory terminalCategory)
    {
        return TerminalViewResponse.builder()
                .name(terminalCategory.getName())
                .terminals(terminalCategory.getTerminals().stream().map(Terminal::getName).collect(Collectors.toList()))
                .build();
    }
}
