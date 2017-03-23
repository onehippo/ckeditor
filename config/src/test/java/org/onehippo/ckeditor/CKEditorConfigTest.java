/*
 * Copyright 2017 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.ckeditor;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CKEditorConfigTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    @Test
    public void defaultFormattedTextConfigIsValidJson() throws IOException {
        final JsonNode json = mapper.readTree(CKEditorConfig.DEFAULT_FORMATTED_TEXT_CONFIG);
        assertNotNull(json);
    }

    @Test
    public void defaultRichTextConfigIsValidJson() throws IOException {
        final JsonNode json = mapper.readTree(CKEditorConfig.DEFAULT_RICH_TEXT_CONFIG);
        assertNotNull(json);
    }

    @Test
    public void customStylesSetReplacesLanguageParameter() {
        final Locale zn_CN = new Locale("zh", "CN");
        final String stylesSet = CKEditorConfig.getStylesSet("mystyle_{language}:./mystyles.js", zn_CN);
        assertEquals("mystyle_zh:./mystyles.js", stylesSet);
    }

    @Test
    public void defaultStylesSetIncludesLanguage() {
        final Locale zn_CN = new Locale("zh", "CN");
        final String stylesSet = CKEditorConfig.getDefaultStylesSet(zn_CN);
        assertEquals("hippo_zh:./hippostyles.js", stylesSet);
    }
}