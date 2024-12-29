package com.shopme.admin.brand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Brand no found")
public class BrandNotFoundRestException extends Exception{

}
