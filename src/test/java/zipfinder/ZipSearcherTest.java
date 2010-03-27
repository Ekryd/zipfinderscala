package zipfinder;

import java.io.IOException;
import java.util.zip.*;

import scala.List;

import java.util.*;

import junit.framework.TestCase;
import cheesymock.*;
import java.util.zip.ZipEntry;

public class ZipSearcherTest extends TestCase {

    private ZipFileEntries entries;
    private Recorder recorder;

    protected void setUp() throws Exception {
        entries = Cheesy.mock(ZipFileEntries.class, new Object() {
            @SuppressWarnings("unused")
            public List<ZipEntry> getEntries() {
            	// TODO: Bah!!!
                return null;
            }
        });
        recorder = new Recorder();
    }

    public void testMatchNormal() throws ZipException, IOException {
        // setup
        ZipSearcher search = new ZipSearcher("String");
        // expect
        recorder.expect(entries).getEntries();
        // test
        List<String> fileNames = search.findEntries(entries);
        recorder.check();
        // assert
        assertEquals(1, fileNames.size());
        assertEquals("java/lang/String.class", fileNames.first());
    }

    public void testMatchRegEx() throws ZipException, IOException {
        // setup
        ZipSearcher search = new ZipSearcher("java.lang.String");
        // expect
        recorder.expect(entries).getEntries();
        // test
        List<String> fileNames = search.findEntries(entries);
        recorder.check();
        // assert
        assertEquals(1, fileNames.size());
        assertEquals("java/lang/String.class", fileNames.first());
    }

    public void testNoMatchNormal() throws ZipException, IOException {
        // setup
        ZipSearcher search = new ZipSearcher("Long");
        // expect
        recorder.expect(entries).getEntries();
        // test
        List<String> fileNames = search.findEntries(entries);
        recorder.check();
        // assert
        assertEquals(0, fileNames.size());
    }

    public void testNoMatchRegExl() throws ZipException, IOException {
        // setup
        ZipSearcher search = new ZipSearcher("java.String");
        // expect
        recorder.expect(entries).getEntries();
        // test
        List<String> fileNames = search.findEntries(entries);
        recorder.check();
        // assert
        assertEquals(0, fileNames.size());
    }
}
