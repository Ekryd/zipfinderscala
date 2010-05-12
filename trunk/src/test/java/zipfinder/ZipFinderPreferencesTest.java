package zipfinder;

import java.util.LinkedList;
import java.util.prefs.Preferences;

import junit.framework.TestCase;
import zipfinder.testutil.ReflectionHelper;
import cheesymock.Cheesy;
import cheesymock.Recorder;
import scala.List;

@SuppressWarnings("unused")
public class ZipFinderPreferencesTest extends TestCase {
    private Preferences preferencesMock;
    private String store;
    private Recorder recorder;

    public final void testAddDirectoryEmpty() throws Exception {
        // Setup
        store = "";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        recorder.expect(preferencesMock).put("Directories", "pelle");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addDirectory("pelle");
        recorder.check();
        // assert
    }

    public final void testAddDirectoryExistingEntry() throws Exception {
        // Setup
        store = "anna|stina|olle";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        recorder.expect(preferencesMock).put("Directories", "stina|anna|olle");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addDirectory("stina");
        recorder.check();
        // assert
    }

    public final void testAddDirectoryOneEntry() throws Exception {
        // Setup
        store = "anna";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        recorder.expect(preferencesMock).put("Directories", "pelle|anna");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addDirectory("pelle");
        recorder.check();
        // assert
    }

    public final void testAddDirectoryThreeEntry() throws Exception {
        // Setup
        store = "anna|C:\\Documents and Settings\\Bjorn\\.saverbeans|olle";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        recorder.expect(preferencesMock).put("Directories",
                "C:\\Documents and Settings|anna|C:\\Documents and Settings\\Bjorn\\.saverbeans|olle");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addDirectory("C:\\Documents and Settings");
        recorder.check();
        // assert
    }

    public final void testAddDirectoryTooManyEntryEntry() throws Exception {
        // Setup
        store = "1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        recorder.expect(preferencesMock).put("Directories", "stina|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addDirectory("stina");
        recorder.check();
        // assert
    }

    public final void testAddStringToFindEmpty() throws Exception {
        // Setup
        store = "";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        recorder.expect(preferencesMock).put("StringsToFind", "pelle");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addStringToFind("pelle");
        recorder.check();
        // assert
    }

    public final void testAddStringToFindExistingEntry() throws Exception {
        // Setup
        store = "anna|stina|olle";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        recorder.expect(preferencesMock).put("StringsToFind", "stina|anna|olle");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addStringToFind("stina");
        recorder.check();
        // assert
    }

    public final void testAddStringToFindOneEntry() throws Exception {
        // Setup
        store = "anna";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        recorder.expect(preferencesMock).put("StringsToFind", "pelle|anna");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addStringToFind("pelle");
        recorder.check();
        // assert
    }

    public final void testAddStringToFindThreeEntry() throws Exception {
        // Setup
        store = "anna|stina|olle";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        recorder.expect(preferencesMock).put("StringsToFind", "pelle|anna|stina|olle");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addStringToFind("pelle");
        recorder.check();
        // assert
    }

    public final void testAddStringToFindTooManyEntryEntry() throws Exception {
        // Setup
        store = "1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        recorder.expect(preferencesMock).put("StringsToFind", "stina|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19");
        recorder.expect(preferencesMock).sync();
        // replay
        ZipFinderPreferences.addStringToFind("stina");
        recorder.check();
        // assert
    }

    public final void testGetRecentDirectoriesEmpty() {
        // Setup
        store = "";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        // replay
        final List<String> recentDirectories = ZipFinderPreferences.getRecentDirectories();
        recorder.check();
        // assert
        assertEquals(0, recentDirectories.size());
    }

    public final void testGetRecentDirectoriesManyEntry() {
        // Setup
        store = "pelle|C:\\Documents and Settings\\Bjorn\\.saverbeans|stina";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        // replay
        final List<String> recentDirectories = ZipFinderPreferences.getRecentDirectories();
        recorder.check();
        // assert
        assertEquals(3, recentDirectories.size());
        assertEquals("pelle", recentDirectories.apply(0));
        assertEquals("C:\\Documents and Settings\\Bjorn\\.saverbeans", recentDirectories.apply(1));
        assertEquals("stina", recentDirectories.apply(2));
    }

    public final void testGetRecentDirectoriesOneEntry() {
        // Setup
        store = "pelle";
        // expect
        recorder.expect(preferencesMock).get("Directories", "");
        // replay
        final List<String> recentDirectories = ZipFinderPreferences.getRecentDirectories();
        recorder.check();
        // assert
        assertEquals(1, recentDirectories.size());
        assertEquals("pelle", recentDirectories.first());
    }

    public final void testGetRecentStringsToFindEmpty() {
        // Setup
        store = "";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        // replay
        final List<String> recentDirectories = ZipFinderPreferences.getRecentStringsToFind();
        recorder.check();
        // assert
        assertEquals(0, recentDirectories.size());
    }

    public final void testGetRecentStringsToFindManyEntry() {
        // Setup
        store = "pelle|anna|stina";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        // replay
        final List<String> recentDirectories = ZipFinderPreferences.getRecentStringsToFind();
        recorder.check();
        // assert
        assertEquals(3, recentDirectories.size());
        assertEquals("pelle", recentDirectories.apply(0));
        assertEquals("anna", recentDirectories.apply(1));
        assertEquals("stina", recentDirectories.apply(2));
    }

    public final void testGetRecentStringsToFindOneEntry() {
        // Setup
        store = "pelle";
        // expect
        recorder.expect(preferencesMock).get("StringsToFind", "");
        // replay
        final List<String> recentDirectories = ZipFinderPreferences.getRecentStringsToFind();
        recorder.check();
        // assert
        assertEquals(1, recentDirectories.size());
        assertEquals("pelle", recentDirectories.first());
    }

    @Override
    protected void setUp() throws Exception {
        ZipFinderPreferences$ zipFinderPreferences = new ZipFinderPreferences$();
        preferencesMock = Cheesy.mock(Preferences.class, new Object() {
            public String get(final String key, final String def) {
                return store;
            }
        });
        recorder = new Recorder();
        new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);
    }
}
