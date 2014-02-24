package goku.storage;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocalFileStorageTest {
  private Storage storage;
  private static final String TEST_FILENAME = "LocalFileStorageTest.goku";

  @Before
  public void setup() {
    storage = new LocalFileStorage(TEST_FILENAME);
  }

  @After
  public void cleanup() {
    storage.delete();
  }

  @Test(expected = NullPointerException.class)
  public void save_NullStoreable_throwsException() throws IOException {
    storage.save(null);
  }

  @Test(expected = NullPointerException.class)
  public void save_NullStoreableArray_throwsException() throws IOException {
    storage.saveAll(null);
  }

  @Test
  public void save_EmptyStoreableArray() throws Exception {
    MockStoreable[] mockArray = {};
    storage.saveAll(mockArray);
    String expected = getStringRepresentation(mockArray);
    String actual = readAllFromFile();
    assertEquals(expected, actual);
  }

  @Test
  public void testSaveStoreable() throws IOException {
    MockStoreable mockUnit = makeMockStoreable();
    storage.save(mockUnit);
    String expected = getStringRepresentation(mockUnit);
    String actual = readAllFromFile();
    assertEquals(expected, actual);
  }

  @Test
  public void testSaveStoreableArray() throws FileNotFoundException,
      IOException {
    MockStoreable[] mockArray = makeMockStoreableArray();
    storage.saveAll(mockArray);
    String expected = getStringRepresentation(mockArray);
    String actual = readAllFromFile();
    assertEquals(expected, actual);
  }

  private MockStoreable makeMockStoreable() {
    return new MockStoreable(10, "Mock Storeable 1");
  }

  private String getStringRepresentation(MockStoreable mockUnit) {
    return mockUnit.toStringFormat() + System.lineSeparator();
  }

  private MockStoreable[] makeMockStoreableArray() {
    MockStoreable[] mockArray = { new MockStoreable(10, "Mock Storeable 1"),
        new MockStoreable(20, "Mock Storeable 2"),
        new MockStoreable(5, "Mock Storeable 3") };
    return mockArray;
  }

  private String getStringRepresentation(MockStoreable[] mockArray) {
    StringBuilder sb = new StringBuilder();
    for (MockStoreable s : mockArray) {
      sb.append(s.toStringFormat());
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }

  private String readAllFromFile() throws FileNotFoundException, IOException {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(new File(
        TEST_FILENAME)))) {
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
      }
    }
    return sb.toString();
  }

}
