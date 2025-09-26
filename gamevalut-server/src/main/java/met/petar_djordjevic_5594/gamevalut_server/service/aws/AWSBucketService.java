package met.petar_djordjevic_5594.gamevalut_server.service.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AWSBucketService {
    @Autowired
    private AmazonS3 s3Client;

    public void postObjectIntoBucket(String bucketName, String objectName, File fileToUpload)throws AmazonServiceException {
        s3Client.putObject(bucketName,objectName,fileToUpload);
    }

    public void deleteObjectFromBucket(String bucketName, String objectName) throws AmazonServiceException {
        s3Client.deleteObject(bucketName, objectName);
    }
}
