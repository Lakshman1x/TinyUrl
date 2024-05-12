package com.training.tinyurl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUrlDto {

    @NotBlank(message = "tinyUrl can not be blank")
    private String tinyUrl;

    @NotBlank(message = "long url can not be blank")
    @Pattern(regexp = "^(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*$", message = "Not a valid URL")
    private String longUrl;
}
