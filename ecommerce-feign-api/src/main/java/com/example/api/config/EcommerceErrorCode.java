package com.example.api.config;

import com.example.common.exception.*;
import feign.Response;
import feign.codec.ErrorDecoder;


public class EcommerceErrorCode implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        Exception def = new Default().decode(methodKey, response);
        if(response.status() == 400) {
            return new BadRequestException(def.getMessage());
        } else if (response.status() == 401) {
            return new UnauthorizedException(def.getMessage());
        } else if (response.status() == 403) {
            return new UserException(403, def.getMessage());
        } else if (response.status() == 404) {
            return new NotFoundException(def.getMessage());
        }
        return def;
    }
}
