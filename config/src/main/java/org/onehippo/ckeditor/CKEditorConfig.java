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

import java.util.Locale;

/**
 * Default configuration of CKEditor fields in Hippo CMS.
 */
public class CKEditorConfig {

    private CKEditorConfig() {
    }

    /**
     * Various CKEDITOR.config property names.
     */
    public static final String CONTENTS_CSS = "contentsCss";
    public static final String CUSTOM_CONFIG = "customConfig";
    public static final String EXTRA_PLUGINS = "extraPlugins";
    public static final String KEYSTROKES = "keystrokes";
    public static final String LANGUAGE = "language";
    public static final String STYLES_SET = "stylesSet";

    /**
     * CKEDITOR constants for keyboard shortcuts
     */
    public static final int CTRL = 0x110000;
    public static final int SHIFT = 0x220000;
    public static final int ALT = 0x440000;

    /**
     * Default config for formatted text fields.
     */
    public static final String DEFAULT_FORMATTED_TEXT_CONFIG = "{"
            // do not html encode but utf-8 encode hence entities = false
            + "  entities: false,"
            // &gt; must not be replaced with > hence basicEntities = true
            + "  basicEntities: true,"
            + "  autoUpdateElement: false,"
            + "  contentsCss: ['ckeditor/hippocontents.css'],"
            + "  plugins: 'basicstyles,button,clipboard,contextmenu,divarea,enterkey,entities,floatingspace,floatpanel,htmlwriter,listblock,magicline,menu,menubutton,panel,panelbutton,removeformat,richcombo,stylescombo,tab,toolbar,undo',"
            + "  title: false,"
            + "  toolbar: ["
            + "    { name: 'styles', items: [ 'Styles' ] },"
            + "    { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', '-', 'RemoveFormat' ] },"
            + "    { name: 'clipboard', items: [ 'Undo', 'Redo' ] }"
            + "  ]"
            + "}";

    /**
     * Default config for rich text fields.
     */
    public static final String DEFAULT_RICH_TEXT_CONFIG = "{"
            // do not html encode but utf-8 encode hence entities = false
            + "  entities: false,"
            // &gt; must not be replaced with > hence basicEntities = true
            + "  basicEntities: true,"
            + "  autoUpdateElement: false,"
            + "  contentsCss: ['ckeditor/hippocontents.css'],"
            + "  dialog_buttonsOrder: 'ltr',"
            + "  dialog_noConfirmCancel: true,"
            + "  extraAllowedContent: 'embed[allowscriptaccess,height,src,type,width]; img[border,hspace,vspace]; object[align,data,height,id,title,type,width]; p[align]; param[name,value]; table[width]; td[valign,width]; th[valign,width];',"
		    // TODO: Temporary fix for CHANNELMGR-1237: the hippopicker plugin will be refactored to enable usage outside
			// of the CMS (e.g. channel-manager), until that is finished the configuration is hardcoded below.
            + "  hippopicker: { internalLink: {callbackUrl: 'TODO'}, image: {callbackUrl: 'TODO'}},"
			+ "  keystrokes: ["
            + "    [ 'Ctrl', 'm', 'maximize' ],"
            + "    [ 'Alt', 'b', 'showblocks' ]"
            + "  ],"
            + "  linkShowAdvancedTab: false,"
            + "  plugins: 'a11yhelp,basicstyles,button,clipboard,codemirror,contextmenu,dialog,dialogadvtab,dialogui,divarea,elementspath,enterkey,entities,floatingspace,floatpanel,hippopicker,htmlwriter,indent,indentblock,indentlist,justify,link,list,listblock,liststyle,magicline,maximize,menu,menubutton,panel,panelbutton,pastefromword,pastetext,popup,removeformat,resize,richcombo,showblocks,showborders,specialchar,stylescombo,tab,table,tableresize,tabletools,textselection,toolbar,undo,youtube',"
            + "  removeFormatAttributes: 'style,lang,width,height,align,hspace,valign',"
            + "  title: false,"
            + "  toolbarGroups: ["
            + "    { name: 'styles' },"
            + "    { name: 'basicstyles' },"
            + "    { name: 'undo' },"
            + "    { name: 'listindentalign',  groups: [ 'list', 'indent', 'align' ] },"
            + "    { name: 'links' },"
            + "    { name: 'insert' },"
            + "    { name: 'tools' },"
            + "    { name: 'mode' }"
            + "  ]"
            + "}";

    private static final String STYLES_SET_LANGUAGE_PARAM = "{language}";

    public static String getStylesSet(final String customStylesSet, final Locale locale) {
        return customStylesSet.replace(STYLES_SET_LANGUAGE_PARAM, locale.getLanguage());
    }

    public static String getDefaultStylesSet(final Locale locale) {
        final String styleName = "hippo_" + locale.getLanguage();
        return styleName + ":./hippostyles.js";
    }


}
