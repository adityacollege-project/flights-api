package com.ardor.flights.exception.handler;

import com.ardor.flights.exception.*;

import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Copyright (c) 2024, Ardor Technologies All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of Ardor Technologies. You shall
 * not disclose such Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with Ardor Technologies.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


  /**
   * Handles MethodArgumentNotValidException by creating a standardized error response.
   *
   * @param manve   The exception representing validation errors.
   * @param request The WebRequest associated with the current HTTP request.
   * @return A ResponseEntity containing a map with timestamp, request path, error message, and HTTP
   * status code (Bad Request).
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      final MethodArgumentNotValidException manve, WebRequest request) {
    log.error("Validation error ", manve);
    Map<String, Object> response = new LinkedHashMap<>();
    String path = ((ServletWebRequest) request).getRequest().getRequestURI();
    response.put("path", path);
    //To split and separate errorcode and error message.
    String[] tokens = manve.getFieldError().getDefaultMessage().split(":");
    tokens[0] = path.contains("priceCheck") ? tokens[0].replace("FSW0", "RPE0") : tokens[0];
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", manve.getStatusCode());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler({InvalidRequestException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException exception,
                                                              WebRequest request)  {
    log.error("Validation error ", exception);
    Map<String, Object> response = new LinkedHashMap<>();
    String path = ((ServletWebRequest) request).getRequest().getRequestURI();
    response.put("path", path);
    //To split and separate errorcode and error message.
    String[] tokens = exception.getMessage().split(":");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", HttpStatus.BAD_REQUEST);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(FlightSearchException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, Object>> handleFlightSearchExceptions(
      final FlightSearchException fse, WebRequest request) {
    log.error("Flight Search Exception in Supplier", fse);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    //To split and separate errorcode and error message.
    String[] tokens = fse.getMessage().split(":");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    return ResponseEntity.internalServerError().body(response);
  }

  @ExceptionHandler(SocketTimeoutException.class)
  @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
  public ResponseEntity<Map<String, Object>> handleSocketTimeOutException(
      SocketTimeoutException ste, WebRequest request) {
    log.error("Failed to get response from Supplier", ste);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
    response.put("status", HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase());
    response.put("errorCode", "FSE0002");
    response.put("errorMessage", ste.getMessage());
    return new ResponseEntity<>(response, HttpStatus.GATEWAY_TIMEOUT);
  }

  @ExceptionHandler(FlightRepriceException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ResponseEntity<Map<String, Object>> handleFlightRepriceExceptions(final FlightRepriceException fre, WebRequest request) {
    log.error("Flight Reprice Exception in Supplier", fre);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

    String[] tokens = fre.getMessage().split(":");
    tokens[0] = tokens[0].replace("FSW0", "RPE0");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
  }
  @ExceptionHandler(SeatMapException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ResponseEntity<Map<String, Object>> handleSeatMapExceptions(final SeatMapException fre, WebRequest request) {
    log.error("Flight Reprice Exception in Supplier", fre);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
    String[] tokens = fre.getMessage().split(":");
    tokens[0] = tokens[0].replace("FSW0", "RPE0");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler(FlightCancelException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ResponseEntity<Map<String, Object>> handleFlightCancelExceptions(final FlightCancelException fre, WebRequest request) {
    log.error("Flight Cancel Exception in Supplier", fre);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

    String[] tokens = fre.getMessage().split(":");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
  }  


  @ExceptionHandler(FlightReadPnrException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ResponseEntity<Map<String, Object>> handleFlightReadPnrExceptions(final FlightReadPnrException fre, WebRequest request) {
    log.error("Flight Read Exception in Supplier", fre);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

    String[] tokens = fre.getMessage().split(":");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
  }  


  @ExceptionHandler(FlightOrderTicketException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ResponseEntity<Map<String, Object>> handleFlightOrderTicketExceptions(final FlightOrderTicketException fre, WebRequest request) {
    log.error("Flight Read Exception in Supplier", fre);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

    String[] tokens = fre.getMessage().split(":");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
  }
  @ExceptionHandler(UserNotFoundExcpetion.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ResponseEntity<Map<String, Object>> handleUserNotFoundException(final UserNotFoundExcpetion unf, WebRequest request) {
    log.error("User not found with the API key", unf);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

    String[] tokens = unf.getMessage().split(":");
    response.put("errorMessage", tokens[0]);
    if (tokens.length == 2) {
      if (tokens[0] != null) {
        response.put("errorCode", tokens[0]);
      }
      if (tokens[1] != null) {
        response.put("errorMessage", tokens[1]);
      }
    }
    response.put("status", HttpStatus.BAD_GATEWAY.getReasonPhrase());
    return new ResponseEntity(response, HttpStatus.BAD_GATEWAY);
  }
  @ExceptionHandler(FlightNotFoundException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Map<String, Object>> handleNoFlightsAvailable(final FlightNotFoundException ex,
      WebRequest request) {
    log.error("Exception ", ex);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
    String [] tokens = ex.getMessage().split(":");
    if(tokens.length == 2)
    {
      if(tokens[0] != null) {
        response.put("errorCode",tokens[0]);
      }
      if(tokens[1]!=null) {
        response.put("errorMessage",tokens[1]);
      }
    }
    response.put("status", HttpStatus.OK);
    return ResponseEntity.ok(response);
  }
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, Object>> handleGenericExceptions(final Exception ex,
      WebRequest request) {
    log.error("Exception ", ex);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
    response.put("errorCode", "FSS0001");
    response.put("message", ex.getCause());
    if(null != ex.getMessage() && ex.getMessage().contains(":")) {
      String [] tokens = ex.getMessage().split(":");
      if(tokens.length == 2)
      {
        if(tokens[0] != null) {
          response.put("errorCode",tokens[0]);
        }
        if(tokens[1]!=null) {
          response.put("errorMessage",tokens[1]);
        }
      }
    }
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    return ResponseEntity.internalServerError().body(response);
  }
}
