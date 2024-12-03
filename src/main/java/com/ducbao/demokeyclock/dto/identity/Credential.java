package com.ducbao.demokeyclock.dto.identity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Credential {
    String type;
    String value;
    boolean temporary;
}
