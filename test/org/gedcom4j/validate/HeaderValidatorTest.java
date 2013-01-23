/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.io.Encoding;
import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.Submitter;
import org.junit.Test;

/**
 * Test case for {@link HeaderValidator}
 * 
 * @author mharrah
 * 
 */
public class HeaderValidatorTest extends AbstractValidatorTestCase {

    @Test
    public void testCharacterSet() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.submitters.put(s.xref, s);
        Header h = g.header;
        h.submitter = s;

        h.characterSet = null;

        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set");

        h.characterSet = new CharacterSet();
        rootValidator.validate();
        assertNoIssues();

        h.characterSet.characterSetName = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set", "name", "not", "defined");

        h.characterSet.characterSetName = new StringWithCustomTags("FRYINGPAN");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set", "not", "supported");

        h.characterSet.characterSetName = new StringWithCustomTags(Encoding.ASCII.getCharacterSetName());
        rootValidator.validate();
        assertNoIssues();

        h.characterSet.customTags = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "custom tag", "is null");

    }

    @Test
    public void testCopyrightData() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.submitters.put(s.xref, s);
        Header h = g.header;
        h.submitter = s;

        h.copyrightData = null;

        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "copyright");

        h.copyrightData = new ArrayList<String>();

        rootValidator.validate();
        assertNoIssues();

    }

    @Test
    public void testNoSubmitters() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;

        rootValidator.autorepair = false;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "submitter", "not specified");

        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.submitters.put(s.xref, s);
        Header h = g.header;
        h.submitter = s;

        rootValidator.validate();
        assertNoIssues();

    }
}
