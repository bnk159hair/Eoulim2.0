const { getSignedUrl } = require('@aws-sdk/s3-request-presigner');
const { S3Client, GetObjectCommand } = require('@aws-sdk/client-s3');

// 환경 변수 로드
const { VITE_AWS_ACCESS_KEY, VITE_AWS_SECRET_KEY, VITE_AWS_REGION, VITE_AWS_ANIMON_BUCKET } =
  import.meta.env;
console.log(VITE_AWS_ACCESS_KEY, VITE_AWS_SECRET_KEY, VITE_AWS_REGION, VITE_AWS_ANIMON_BUCKET);

// AWS SDK 설정 및 S3 클라이언트 생성
export const s3Client = new S3Client({
  region: VITE_AWS_REGION, // AWS S3 버킷이 있는 리전
  credentials: {
    accessKeyId: VITE_AWS_ACCESS_KEY,
    secretAccessKey: VITE_AWS_SECRET_KEY,
  },
});
console.log(s3Client);

// 이미지를 가져오는 함수
export async function generateSignedUrl(objectKey: string): Promise<string> {
  const expiration = 60 * 60;
  const params = {
    Bucket: VITE_AWS_ANIMON_BUCKET,
    Key: objectKey,
    Expires: expiration,
  };

  const command = new GetObjectCommand(params);
  const url = await s3Client.getSignedUrl(command, { expiresIn: expiration });

  return url;
}
