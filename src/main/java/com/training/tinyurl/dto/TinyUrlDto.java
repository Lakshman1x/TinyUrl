package com.training.tinyurl.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TinyUrlDto {
    @Pattern(regexp = "^(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*$", message = "Not a valid URL")
    private String longUrl;
}