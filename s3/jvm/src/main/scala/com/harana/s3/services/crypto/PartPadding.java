package com.harana.s3.services.crypto;

import javax.crypto.spec.IvParameterSpec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.jclouds.blobstore.domain.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartPadding {
    private static final Logger logger = LoggerFactory.getLogger(PartPadding.class);

    private String delimiter;
    private IvParameterSpec iv;
    private int part;
    private long size;
    private short version;

    public static PartPadding readPartPaddingFromBlob(Blob blob) throws IOException {
        PartPadding partPadding = new PartPadding();
        InputStream is = blob.getPayload().openStream();

        byte[] paddingBytes = IOUtils.toByteArray(is);
        ByteBuffer bb = ByteBuffer.wrap(paddingBytes);

        byte[] delimiterBytes = new byte[Constants.PADDING_DELIMITER_LENGTH];
        bb.get(delimiterBytes);
        partPadding.delimiter = new String(delimiterBytes, StandardCharsets.UTF_8);

        byte[] ivBytes = new byte[Constants.PADDING_IV_LENGTH];
        bb.get(ivBytes);
        partPadding.iv = new IvParameterSpec(ivBytes);

        partPadding.part = bb.getInt();
        partPadding.size = bb.getLong();
        partPadding.version = bb.getShort();

        logger.debug("delimiter {}", partPadding.delimiter);
        logger.debug("iv {}", Arrays.toString(ivBytes));
        logger.debug("part {}", partPadding.part);
        logger.debug("size {}", partPadding.size);
        logger.debug("version {}", partPadding.version);

        return partPadding;
    }

    public final String getDelimiter() {
        return delimiter;
    }

    public final IvParameterSpec getIv() {
        return iv;
    }

    public final int getPart() {
        return part;
    }

    public final long getSize() {
        return size;
    }
}