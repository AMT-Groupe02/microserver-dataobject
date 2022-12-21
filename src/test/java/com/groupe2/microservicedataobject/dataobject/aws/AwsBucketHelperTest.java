package com.groupe2.microservicedataobject.dataobject.aws;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Disabled because AWS don't like creating buckets again and again...")
class AwsBucketHelperTest {

    static String BUCKET_NAME = "amt.team02.diduno.education";

    @AfterAll
    static void afterAll() {
        if (!AwsBucketHelper.bucketExists(BUCKET_NAME)) {
            AwsBucketHelper.createBucket(BUCKET_NAME);
        }
    }

    @BeforeEach
    void setUp() {
        if (AwsBucketHelper.bucketExists(BUCKET_NAME)) {
            AwsBucketHelper.deleteBucket(BUCKET_NAME);
        }
    }

    @AfterEach
    void tearDown() {
        if (AwsBucketHelper.bucketExists(BUCKET_NAME)) {
            AwsBucketHelper.deleteBucket(BUCKET_NAME);
        }
    }

    @Test
    void createBucket() {
        AwsBucketHelper.createBucket(BUCKET_NAME);
        assertTrue(AwsBucketHelper.bucketExists(BUCKET_NAME));
    }

    @Test
    void deleteBucket() {
        AwsBucketHelper.createBucket(BUCKET_NAME);
        AwsBucketHelper.deleteBucket(BUCKET_NAME);
        assertFalse(AwsBucketHelper.bucketExists(BUCKET_NAME));
    }

    @Test
    void bucketExists() {
        assertFalse(AwsBucketHelper.bucketExists(BUCKET_NAME));
        AwsBucketHelper.createBucket(BUCKET_NAME);
        assertTrue(AwsBucketHelper.bucketExists(BUCKET_NAME));
    }
}