package com.gridnode.pdip.base.security.helpers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cms.*;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cms.*;
import java.util.zip.Deflater;
/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */
//GNCMSCompressedDataGenerator

public class GNCMSCompressedDataGenerator
{
    public static final String  ZLIB    = "1.2.840.113549.1.9.16.3.8";

    /**
     * base constructor
     */
     public GNCMSCompressedDataGenerator()
    {
    }

    private DERObject makeObj(
        byte[]  encoding)
        throws IOException
    {
        if (encoding == null)
        {
            return null;
        }

        ByteArrayInputStream    bIn = new ByteArrayInputStream(encoding);
        ASN1InputStream         aIn = new ASN1InputStream(bIn);

        return aIn.readObject();
    }

    private AlgorithmIdentifier makeAlgId(
        String  oid,
        byte[]  params)
        throws IOException
    {
        if (params != null)
        {
            return new AlgorithmIdentifier(
                            new DERObjectIdentifier(oid), makeObj(params));
        }
        else
        {
            return new AlgorithmIdentifier(new DERObjectIdentifier(oid));
        }
    }

    /**
     * generate an object that contains an CMS Compressed Data
     */
    public CMSCompressedData generate(
        CMSProcessable  content,
        String          compressionOID)
        throws CMSException
    {
        AlgorithmIdentifier     comAlgId;
        ASN1OctetString         comOcts;

        try
        {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            DeflaterOutputStream  zOut = new DeflaterOutputStream(bOut);

            content.write(zOut);

            zOut.close();

            comAlgId = makeAlgId(compressionOID, null);
            comOcts = new BERConstructedOctetString(bOut.toByteArray());
        }
        catch (IOException e)
        {
            throw new CMSException("exception encoding data.", e);
        }

        ContentInfo     comContent = new ContentInfo(
                                    CMSObjectIdentifiers.data, comOcts);

        ContentInfo     contentInfo = new ContentInfo(
                                    CMSObjectIdentifiers.compressedData,
                                    new CompressedData(comAlgId, comContent));

        return new CMSCompressedData(contentInfo);
    }

    public CMSCompressedData generate(
        CMSProcessable  content,
        String          compressionOID,
        int             level)
        throws CMSException
    {
        AlgorithmIdentifier     comAlgId;
        ASN1OctetString         comOcts;

        try
        {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            Deflater de = new Deflater(level);
            DeflaterOutputStream  zOut = new DeflaterOutputStream(bOut, de);

            content.write(zOut);

            zOut.close();

            comAlgId = makeAlgId(compressionOID, null);
            comOcts = new BERConstructedOctetString(bOut.toByteArray());
        }
        catch (IOException e)
        {
            throw new CMSException("exception encoding data.", e);
        }

        ContentInfo     comContent = new ContentInfo(
                                    CMSObjectIdentifiers.data, comOcts);

        ContentInfo     contentInfo = new ContentInfo(
                                    CMSObjectIdentifiers.compressedData,
                                    new CompressedData(comAlgId, comContent));

        return new CMSCompressedData(contentInfo);
    }

}
