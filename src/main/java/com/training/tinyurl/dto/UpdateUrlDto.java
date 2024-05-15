package com.training.tinyurl.dto;

import com.training.tinyurl.constants.RegexpPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUrlDto {

    @NotBlank(message = "tinyUrl can not be blank")
    @Size(max = 8, message = "tinyUrl length max length must be 8")
    @Pattern(regexp = RegexpPattern.BASE64_PATTERN, message = "Invalid tinyUrl")
    private String tinyUrl;

    @NotBlank(message = "long url can not be blank")
    @Pattern(regexp = RegexpPattern.URL_PATTERN, message = "Not a valid URL")
    private String longUrl;
}
