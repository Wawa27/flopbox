package fil.coo.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.sshd.common.util.io.IoUtils;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Singleton
public class FtpClientService {

    public FtpClientService() {

    }

    public boolean uploadFileOrZip(FTPClient ftpClient, String fileName, InputStream inputStream, String path) throws IOException {
        if (FilenameUtils.isExtension(fileName, "zip", "7z")) {
            return uploadZip(ftpClient, inputStream, path);
        } else {
            return uploadFile(ftpClient, inputStream, path);
        }
    }

    private boolean uploadZip(FTPClient ftpClient, InputStream inputStream, String path) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry;
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    System.out.println("zipEntry is directroy= ");
                    this.makeDirectory(ftpClient, zipEntry.getName());
                } else {
                    System.out.println("not directory");
                    System.out.println(zipEntry.getName());
                    ftpClient.storeFile(path, zipIn);
                }
            }
        }
        return true;
    }

    private boolean uploadFile(FTPClient ftpClient, InputStream inputStream, String path) throws IOException {
        return ftpClient.storeFile(path, inputStream);
    }

    public InputStream download(FTPClient ftpClient, String path) throws IOException {
        FTPFile[] file = ftpClient.listFiles(path);
        if (file.length > 0) {
            return this.downloadFolderZip(ftpClient, path);
        } else {
            return this.downloadFile(ftpClient, path);
        }
    }

    private InputStream downloadFolderZip(FTPClient ftpClient, String path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("tmp.zip")) {
            try (ZipOutputStream zipOut = new ZipOutputStream(fos)) {
                List<String> filesRecursive = getFilesRecursive(ftpClient, path);
                for (String fullPath : filesRecursive) {
                    try (InputStream fileInputStream = ftpClient.retrieveFileStream(fullPath)) {
                        if (fileInputStream != null) {
                            ZipEntry zipEntry = new ZipEntry(fullPath);
                            zipOut.putNextEntry(zipEntry);
                            IoUtils.copy(fileInputStream, zipOut);
                        }
                    }
                }
            }
        }

        return new FileInputStream("tmp.zip");
    }

    private InputStream downloadFile(FTPClient ftpClient, String path) throws IOException {
        return ftpClient.retrieveFileStream(path);
    }

    public boolean rename(FTPClient ftpClient, String path, String pathName) throws IOException {
        return ftpClient.rename(path, pathName);
    }

    public boolean makeDirectory(FTPClient ftpClient, String path) throws IOException {
        return ftpClient.makeDirectory(path);
    }

    public boolean deleteFile(FTPClient ftpClient, String path) throws IOException {
        return ftpClient.deleteFile(path) || ftpClient.removeDirectory(path);
    }

    public String[] getFilenames(FTPClient ftpClient, String path) throws IOException {
        return ftpClient.listNames(path);
    }

    public List<String> getFilesRecursive(FTPClient ftpClient, String path) throws IOException {
        List<String> files = new ArrayList<>();

        ftpClient.enterLocalPassiveMode();
        for (FTPFile file : ftpClient.listFiles(path)) {
            if (file.isFile()) {
                files.add(path + "/" + file.getName());
            } else if (file.isDirectory()) {
                files.addAll(getFilesRecursive(ftpClient, path + "/" + file.getName()));
            } else if (file.isSymbolicLink()) {
                files.addAll(getFilesRecursive(ftpClient, path + "/" + file.getLink()));
            }
        }

        return files;
    }
}
