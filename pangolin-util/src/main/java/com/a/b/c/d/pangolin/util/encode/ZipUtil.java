package com.a.b.c.d.pangolin.util.encode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private ZipUtil() {
    }

    public static byte[] compress(byte[] bytes) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(bytes.length);
            zos.putNextEntry(entry);
            zos.write(bytes);
            zos.flush();
            zos.finish();
            zos.closeEntry();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] bytes) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[1024];
            while (zis.getNextEntry() != null) {
                int n;
                while ((n = zis.read(buffer)) >= 0) {
                    baos.write(buffer, 0, n);
                }
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
