package toyotabackend.toyotabackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toyotabackend.toyotabackend.domain.Terminal.Terminal;
import toyotabackend.toyotabackend.domain.Terminal.TerminalCategory;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalViewResponse {
    private String name;
    private List<String> terminals;
    public static TerminalViewResponse Convert(TerminalCategory terminalCategory)
    {
        return TerminalViewResponse.builder()
                .name(terminalCategory.getName())
                .terminals(terminalCategory.getTerminals().stream().map(Terminal::getName).collect(Collectors.toList()))
                .build();
    }
}
