package com.TestProject.JuiceStock.builder;

import com.TestProject.JuiceStock.dto.JuiceDTO;
import com.TestProject.JuiceStock.enums.JuiceType;
import lombok.Builder;

@Builder
public class JuiceDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Love";

    @Builder.Default
    private String brand = "Juicers";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private JuiceType type = JuiceType.MORANGO;

    public JuiceDTO toJuiceDTO() {
        return new JuiceDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }
}
