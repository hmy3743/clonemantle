package myhan.clonemantle.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CursorPageRequest(
        String after,
        @Min(1)
        @Max(100)
        @NotNull
        int first
){
}
