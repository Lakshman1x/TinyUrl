package com.training.tinyurl.dto;

import com.training.tinyurl.constants.RegexpPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TinyUrlDto {
    @NotBlank(message = "long url can not be blank")
    @Pattern(regexp = RegexpPattern.URL_PATTERN, message = "Not a valid URL")
    private String longUrl;
}
