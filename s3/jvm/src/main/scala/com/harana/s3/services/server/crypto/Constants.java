package com.harana.s3.services.server.crypto;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public final class Constants {
    public static final short VERSION = 1;
    public static final String AES_CIPHER = "AES/CFB/NoPadding";
    public static final String S3_ENC_SUFFIX = ".s3enc";
    public static final String MPU_FOLDER = ".mpu/";
    public static final Pattern MPU_ETAG_SUFFIX_PATTERN = Pattern.compile(".*-([0-9]+)");
    public static final String METADATA_ENCRYPTION_PARTS = "s3proxy_encryption_parts";
    public static final String METADATA_IS_ENCRYPTED_MULTIPART = "s3proxy_encryption_multipart";
    public static final String METADATA_MULTIPART_KEY = "s3proxy_mpu_key";
    public static final int AES_BLOCK_SIZE = 16;
    public static final int PADDING_BLOCK_SIZE = 64;
    public static final byte[] DELIMITER = "-S3-ENC-".getBytes(StandardCharsets.UTF_8);
    public static final int PADDING_DELIMITER_LENGTH = DELIMITER.length;
    public static final int PADDING_IV_LENGTH = 16;
    public static final int PADDING_PART_LENGTH = 4;
    public static final int PADDING_SIZE_LENGTH = 8;
    public static final int PADDING_VERSION_LENGTH = 2;

    private Constants() {
        throw new AssertionError("Cannot instantiate utility constructor");
    }
}