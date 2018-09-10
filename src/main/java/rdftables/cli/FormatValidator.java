/**
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rdftables.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public class FormatValidator implements IParameterValidator {

    private static final List<String> PERMITTED_FORMATS = Arrays.asList("json-ld", "nt", "nq", "json-rdf", "xml-plain", "xml-pretty", "xml", "thrift", "trig", "trix", "ttl", "ttl-pretty");

    @Override
    public void validate(String name, String value) throws ParameterException {
        String val = value.toLowerCase();
        if (!PERMITTED_FORMATS.contains(val)) {
            throw new ParameterException("Parameter " + name + " and value " + value + " should be one of " + String.join(", ", PERMITTED_FORMATS) + ".");
        }

    }

}
