package com.ardor.flights.common;

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

public class Constants {

  public static final int SEARCH_MAX_ALLOWED_DAYS = 333;
  public static final String SEARCH_CB_INSTANCE_NAME = "mondee-flight-search";
  public static final String DATE_PATTERN = "yyyy/MM/dd";
  public static final String DATE_PATTERN_D_M_Y = "dd/MM/yyyy";
  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String DATE_PATTERN_REGEX = "^(19|20)\\d{2}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$";
  public static final String ALPHABET_PATTERN_REGEX = "^[a-zA-Z]+$";
  public static final String DOB_PATTERN = "^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/(19|20)\\d{2}$";
  public static final String EXPIRY_DATE_PATTERN = "^(0[1-9]|1[012])/(20)\\d{2}$";

}
