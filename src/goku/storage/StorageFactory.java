package goku.storage;

/*
 * Factory class to get concrete implementations of the Storage interface
 */
public class StorageFactory {
  /*
   * For CS2103T project, the default storage method is a text file on local
   * disk. This could possibly be expanded to include storage in the cloud, e.g.
   * Dropbox, Google Drive, as a form of backup.
   */
  public Storage getDefaultStorage() {
    return new LocalFileStorage();
  }
}
