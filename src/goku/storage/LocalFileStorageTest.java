//@author A0099903R
package goku.storage;

import static org.junit.Assert.assertEquals;
import goku.storage.MockStoreableList.MockStoreable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ZhiAn
 * 
 */
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

  @Test
  public void testSaveStoreableArray() throws FileNotFoundException,
      IOException {
    MockStoreableList list = new MockStoreableList();
    MockStoreable mock = list.new MockStoreable(1, "mock 1");
    list.add(mock);
    storage.saveAll(list);
    String all = readAllFromFile();
    assertEquals(all, mock.toStorageFormat() + System.lineSeparator());
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
