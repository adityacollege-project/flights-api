package com.ardor.flights.config;

/**
 * Copyright (c) 2023, Ardor Technologies All rights reserved.
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

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * MessagesConfiguration is a Spring configuration class responsible for defining the message source
 * bean used for internationalization and localization of messages in the application. It specifies
 * the properties of the ReloadableResourceBundleMessageSource bean, such as the base name and
 * default encoding.
 * <p>
 * The configuration class is annotated with @Configuration, indicating that it contains
 * configuration bean definitions. The messageSource() method is annotated with @Bean, specifying
 * that it provides a MessageSource bean.
 */
@Configuration
public class MessagesConfiguration {

  /**
   * Provides a MessageSource bean for internationalization and localization of messages.
   *
   * @return The configured MessageSource bean.
   */
  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/messages/api_messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

}
