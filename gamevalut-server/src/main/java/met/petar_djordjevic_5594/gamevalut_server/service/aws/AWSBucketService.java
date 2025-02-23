package met.petar_djordjevic_5594.gamevalut_server.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AWSBucketService {
    @Autowired
    private AmazonS3 s3Client;

    public void createBucket(String bucketName) {
        s3Client.createBucket(bucketName);
    }
}
