/*******************************************************************************
 * Copyright 2012 University of Southern California
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This code was developed by the Information Integration Group as part 
 * of the Karma project at the Information Sciences Institute of the 
 * University of Southern California.  For more information, publications, 
 * and related projects, please see: http://www.isi.edu/integration
 ******************************************************************************/

package edu.isi.karma.kr2rml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateTermSetBuilder {

	private static Logger logger = LoggerFactory.getLogger(TemplateTermSetBuilder.class);
	
	public static TemplateTermSet constructTemplateTermSetFromR2rmlTemplateString(
			String templStr) throws JSONException {
		TemplateTermSet termSet = new TemplateTermSet();
		
		Pattern p = Pattern.compile("\\{\\\".*?\\\"\\}");
	    Matcher matcher = p.matcher(templStr);
	    int startIndex = 0;
	    
	    if (matcher.find()) {
	    	matcher.reset();
	    	while (matcher.find()) {
		    	// We have a String template term before this column template term
		    	if (matcher.start() != startIndex) {
		    		String strTermVal = templStr.substring(startIndex, matcher.start());
		    		logger.debug("String templ term: " + strTermVal);
		    		termSet.addTemplateTermToSet(new StringTemplateTerm(strTermVal));
		    	}
		    	
		    	String colTermVal = removeR2rmlFormatting(matcher.group());
		    	logger.debug("Col name templ term: " + colTermVal);
		    	termSet.addTemplateTermToSet(new ColumnTemplateTerm(colTermVal));
		    	
		    	/**/
		    	
	    		startIndex = matcher.end();
		      }
	    } else {
	    	termSet.addTemplateTermToSet(new StringTemplateTerm(templStr));
	    }
	    
		return termSet;
	}
	
	public static TemplateTermSet constructTemplateTermSetFromR2rmlColumnString(
			String colTermVal) throws JSONException {
		TemplateTermSet termSet = new TemplateTermSet();
		
		logger.debug("Column" + removeR2rmlFormatting(colTermVal));
		termSet.addTemplateTermToSet(new ColumnTemplateTerm(colTermVal));
		
		return termSet;
	}
	
	private static String removeR2rmlFormatting(String r2rmlColName) {
		if (r2rmlColName.startsWith("{\"") && r2rmlColName.endsWith("\"}"))
			return r2rmlColName.substring(2, r2rmlColName.length()-2);
		else return r2rmlColName;
	}
}
